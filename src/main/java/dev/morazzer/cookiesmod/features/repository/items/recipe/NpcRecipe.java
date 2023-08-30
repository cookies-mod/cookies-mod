package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonObject;

public class NpcRecipe extends RepositoryRecipe {
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

	public NpcRecipe(JsonObject jsonObject) {
		super(jsonObject);
	}
}
