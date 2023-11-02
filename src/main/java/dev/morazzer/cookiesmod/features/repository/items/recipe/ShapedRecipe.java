package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.utils.general.ItemUtils;
import java.util.Optional;

/**
 * A shaped crafting recipe.
 */
public class ShapedRecipe extends RepositoryRecipe {

    private final Ingredient result;
    private final Ingredient[] materials = new Ingredient[9];

    /**
     * Creates a shaped crating recipe from a {@linkplain com.google.gson.JsonObject}.
     *
     * @param jsonObject The json object.
     */
    public ShapedRecipe(JsonObject jsonObject) {
        super(jsonObject);

        JsonObject craft = jsonObject.getAsJsonObject("crafting");

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                this.materials[x * 3 + y] = new Ingredient(Optional
                    .ofNullable(craft.get("x%s;y%s".formatted(x, y)))
                    .map(JsonElement::getAsString)
                    .map(ItemUtils::withNamespace)
                    .orElse("minecraft:air:1"));
            }
        }

        this.result = new Ingredient("%s:%s".formatted(
            ItemUtils.withNamespace(jsonObject.get("result").getAsString()),
            jsonObject.get("count").getAsInt()
        ));
    }

    @Override
    public Ingredient getOutput() {
        return this.result;
    }

    @Override
    public RecipeType getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public Ingredient[] getIngredients() {
        return this.materials;
    }

}
