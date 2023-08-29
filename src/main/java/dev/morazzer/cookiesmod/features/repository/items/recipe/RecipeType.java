package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public enum RecipeType {

	SHAPED(ShapedRecipe::new),
	FORGE(ForgeRecipe::new),
	TRADE(TradeRecipe::new),
	SELL(SellRecipe::new),
	NPC(NpcRecipe::new);

	private final RecipeCreator constructor;

	RecipeType(RecipeCreator constructor) {
		this.constructor = constructor;
	}

	@FunctionalInterface
	public interface RecipeCreator {
		RepositoryRecipe create(JsonObject jsonObject);
	}

}
