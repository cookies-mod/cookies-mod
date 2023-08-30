package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonObject;

public class TradeRecipe extends RepositoryRecipe {
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

	public TradeRecipe(JsonObject jsonObject) {
		super(jsonObject);
	}
}
