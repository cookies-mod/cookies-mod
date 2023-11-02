package dev.morazzer.cookiesmod.features.repository.items.recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.Getter;
import net.minecraft.util.Identifier;

/**
 * An ingredient used in a crafting recipe or in a different type of recipe.
 */
@Getter
public class Ingredient extends Identifier {

    private int amount = -1;

    /**
     * Creates an ingredient which is an identifier with a fixed amount.
     *
     * @param namespace The namespace of the identifier.
     * @param path      The path of the identifier.
     * @param amount    The amount.
     */
    public Ingredient(String namespace, String path, int amount) {
        super(namespace.toLowerCase(), path.toLowerCase());
        this.amount = amount;
    }

    /**
     * Creates an ingredient from its string representation. The string has to be formatted like the following:
     * namespace:path:amount.
     * <br>
     * For example {@code skyblock:items/wheat:10} represents a wheat item with amount 10.
     *
     * @param id The string representation.
     */
    public Ingredient(String id) {
        super(id.substring(0, id.lastIndexOf(':')).toLowerCase().replace(";", "_").replace("-", "_"));
        if (id.split(":").length >= 2) {
            String[] split = id.split(":");
            String amountString = split[split.length - 1];
            this.amount = Integer.parseInt(amountString);
        }
    }

    /**
     * Creates an ingredient from an existing identifier and an amount.
     *
     * @param identifier The identifier.
     * @param amount     The amount.
     */
    public Ingredient(Identifier identifier, int amount) {
        super(identifier.getNamespace(), identifier.getPath());
        this.amount = amount;
    }

    /**
     * Merges an iterable of ingredients to a set.
     *
     * @param ingredients The ingredients.
     * @return The merged set.
     */
    public static Set<Ingredient> mergeToSet(Iterable<Ingredient> ingredients) {
        return mergeIngredients(ingredients, Collectors.toSet());
    }

    /**
     * Merges an iterable of ingredients to a list.
     *
     * @param ingredients The ingredients.
     * @return The merged list.
     */
    public static List<Ingredient> mergeToList(Iterable<Ingredient> ingredients) {
        return mergeIngredients(ingredients, Collectors.toList());
    }

    /**
     * Merges any given Iterable of Ingredients to not contain duplicated keys.
     *
     * @param ingredients The original list of ingredients
     * @param collector   The collector to create the returned value
     * @param <T>         The return t ype of the collector
     * @return The collected ingredients
     */
    public static <T> T mergeIngredients(Iterable<Ingredient> ingredients, Collector<Ingredient, ?, T> collector) {
        HashMap<String, Ingredient> ingredientMap = new HashMap<>();
        ingredients.forEach(ingredient ->
            ingredientMap.merge(
                ingredient.toString(),
                ingredient,
                (ingredient1, ingredient2) -> ingredient1.withAmount(ingredient1.getAmount() + ingredient2.getAmount())
            )
        );

        return ingredientMap.values().stream().collect(collector);
    }

    /**
     * Creates a new instance of the ingredient with a different amount.
     *
     * @param amount The new amount.
     * @return The new ingredient.
     */
    public Ingredient withAmount(int amount) {
        return new Ingredient(this.getNamespace(), this.getPath(), amount);
    }

    /**
     * Checks if the ingredient is air or a valid item.
     *
     * @return If the item is air or not.
     */
    public boolean isAir() {
        return this.getNamespace().equals("minecraft") && this.getPath().equals("air");
    }

}
