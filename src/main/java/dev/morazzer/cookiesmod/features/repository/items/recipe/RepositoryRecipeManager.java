package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.repository.files.RepositoryFileAccessor;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Manger to handle all recipes in the repository.
 */
public class RepositoryRecipeManager {

    static final Path recipes = RepositoryManager.getRepoRoot().resolve("recipes");
    private static final ConcurrentHashMap<Identifier, List<RepositoryRecipe>> map = new ConcurrentHashMap<>();

    /**
     * Loads all recipes from the repository.
     */
    public static void loadRecipes() {
        for (JsonElement element : RepositoryFileAccessor.getInstance().getDirectory(recipes)) {
            if (!element.isJsonObject()) {
                return;
            }
            JsonObject jsonObject = element.getAsJsonObject();
            RecipeType type = RecipeType.valueOf(jsonObject.get("type").getAsString().toUpperCase());
            RepositoryRecipe repositoryRecipe = type.getConstructor().create(jsonObject);

            map.computeIfAbsent(repositoryRecipe.getOutput(), identifier -> new ArrayList<>()).add(repositoryRecipe);
        }
        System.out.printf("Loaded %s recipes%n", map.size());
    }

    /**
     * Resolves all recipes to the lowest for a single item.
     *
     * @param identifier The item.
     * @return The lowest single ingredient (if present).
     */
    public static Optional<Ingredient> resolveToLowestSingleIngredient(Identifier identifier) {
        return resolveToLowestSingleIngredient(identifier, new ArrayList<>());
    }

    /**
     * Recursively resolves the lowest ingredient.
     *
     * @param identifier The root ingredient.
     * @param visited    A list of all visited ingredients to prevent cyclic behaviour.
     * @return The single lowest ingredient.
     */
    public static Optional<Ingredient> resolveToLowestSingleIngredient(
            Identifier identifier,
            ArrayList<Identifier> visited
    ) {
        visited.add(identifier);
        List<Ingredient> ingredients = getRecipe(identifier, RecipeType.CRAFTING)
                .map(RepositoryRecipe::getIngredients)
                .map(Arrays::asList)
                .map(Ingredient::mergeToList)
                .orElse(Collections.emptyList());

        ingredients.removeIf(ingredient -> ingredient.getPath().equals("air"));

        if (ingredients.size() != 1) {
            return Optional.empty();
        }

        Ingredient lower = ingredients.get(0);
        if (visited.contains(lower)) {
            return Optional.empty();
        }
        return resolveToLowestSingleIngredient(lower, visited)
                .map(craft -> craft.withAmount(craft.getAmount() * lower.getAmount()))
                .or(() -> Optional.of(lower));
    }

    /**
     * Gets a list of all ingredients sorted from the highest amount to the lowest.
     *
     * @param identifier The root item.
     * @return The ingredients required.
     */
    public static List<Ingredient> getIngredientListSorted(Identifier identifier) {
        Optional<RepositoryRecipe> recipe = getRecipe(identifier, RecipeType.CRAFTING);

        return recipe
                .map(repositoryRecipe -> Ingredient
                        .mergeIngredients(Arrays.asList(repositoryRecipe.getIngredients()), Collectors.toList())
                        .stream()
                        .filter(Predicate.not(Ingredient::isAir))
                        .sorted(Comparator.comparingInt(Ingredient::getAmount))
                        .toList())
                .orElse(Collections.emptyList());

    }

    /**
     * Gets the first recipe of any type for the item.
     *
     * @param identifier The item.
     * @return The recipe.
     */
    @SuppressWarnings("unused")
    public static Optional<RepositoryRecipe> getRecipe(Identifier identifier) {
        return map
                .getOrDefault(identifier, Collections.emptyList())
                .stream()
                .findFirst();
    }

    /**
     * Gets the first recipe of a specific type for the item.
     *
     * @param identifier The item.
     * @param type       The type.
     * @return The recipe.
     */
    public static Optional<RepositoryRecipe> getRecipe(Identifier identifier, RecipeType type) {
        return map
                .getOrDefault(identifier, Collections.emptyList())
                .stream()
                .filter(repositoryRecipe -> repositoryRecipe.getType() == type)
                .findFirst();
    }

    /**
     * Gets all recipes of a specific type for an item.
     *
     * @param identifier The item.
     * @param type       The type.
     * @return The list of recipes.
     */
    @SuppressWarnings("unused")
    public static List<RepositoryRecipe> getRecipes(Identifier identifier, RecipeType type) {
        return map
                .getOrDefault(identifier, Collections.emptyList())
                .stream()
                .filter(repositoryRecipe -> repositoryRecipe.getType() == type)
                .toList();
    }

}
