package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonObject;

/**
 * A npc recipe.
 */
public class NpcRecipe extends RepositoryRecipe {

    /**
     * Create a npc recipe from a json object.
     * @param jsonObject The json object.
     */
    public NpcRecipe(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public Ingredient getOutput() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RecipeType getType() {
        return RecipeType.NPC;
    }

    @Override
    public Ingredient[] getIngredients() {
        return new Ingredient[0];
    }

}
