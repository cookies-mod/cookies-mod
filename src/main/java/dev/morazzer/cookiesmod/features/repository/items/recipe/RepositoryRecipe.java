package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonObject;

public abstract class RepositoryRecipe {

    private final JsonObject jsonObject;

    public abstract Ingredient getOutput();
    public abstract RecipeType getType();
    public abstract Ingredient[] getIngredients();

    public RepositoryRecipe(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
