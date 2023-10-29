package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonObject;

/**
 * A trade recipe.
 */
public class TradeRecipe extends RepositoryRecipe {

    /**
     * Create a trade recipe from a json object.
     *
     * @param jsonObject The json object.
     */
    public TradeRecipe(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public Ingredient getOutput() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RecipeType getType() {
        return RecipeType.TRADE;
    }

    @Override
    public Ingredient[] getIngredients() {
        return new Ingredient[0];
    }

}
