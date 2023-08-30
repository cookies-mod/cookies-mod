package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public enum RecipeType {

	CRAFTING(ShapedRecipe::new),
	FORGE(ForgeRecipe::new),
	TRADE(TradeRecipe::new),
	NPC(NpcRecipe::new),
	MOB_LOOT(jsonObject -> null);

	private final RecipeCreator constructor;

	RecipeType(RecipeCreator constructor) {
		this.constructor = constructor;
	}

	@FunctionalInterface
	public interface RecipeCreator {
		RepositoryRecipe create(JsonObject jsonObject);
	}

}
