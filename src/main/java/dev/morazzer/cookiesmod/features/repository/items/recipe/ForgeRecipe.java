package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonObject;

/**
 * A forge recipe.
 */
public class ForgeRecipe extends RepositoryRecipe {

    public ForgeRecipe(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public Ingredient getOutput() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RecipeType getType() {
        return RecipeType.FORGE;
    }

    @Override
    public Ingredient[] getIngredients() {
        return new Ingredient[0];
    }

}
