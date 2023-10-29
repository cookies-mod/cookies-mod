package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonObject;

/**
 * Generic repository recipe.
 */
public abstract class RepositoryRecipe {

    private final JsonObject jsonObject;

    /**
     * Create a repository recipe from a json object.
     *
     * @param jsonObject The json object.
     */
    public RepositoryRecipe(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * Get the item that will be the output of the recipe.
     *
     * @return The output.
     */
    public abstract Ingredient getOutput();

    /**
     * Get the {@linkplain dev.morazzer.cookiesmod.features.repository.items.recipe.RecipeType}
     * associated with the recipe.
     *
     * @return The type.
     */
    public abstract RecipeType getType();

    public abstract Ingredient[] getIngredients();

}
