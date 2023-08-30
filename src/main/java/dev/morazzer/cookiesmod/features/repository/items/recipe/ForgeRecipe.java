package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonObject;

public class ForgeRecipe extends RepositoryRecipe{
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

	public ForgeRecipe(JsonObject jsonObject) {
		super(jsonObject);
	}
}
