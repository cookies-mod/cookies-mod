package dev.morazzer.cookiesmod.features.repository.items.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

public class ShapedRecipe extends RepositoryRecipe {
	private final Ingredient result;
	private final Ingredient[] materials = new Ingredient[9];

	@Override
	public Ingredient getOutput() {
		return this.result;
	}

	@Override
	public RecipeType getType() {
		return RecipeType.CRAFTING;
	}

	@Override
	public Ingredient[] getIngredients() {
		return this.materials;
	}

	public ShapedRecipe(JsonObject jsonObject) {
		super(jsonObject);

		JsonObject craft = jsonObject.getAsJsonObject("crafting");

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				this.materials[x * 3 + y] = new Ingredient(Optional.ofNullable(craft.get("x%s;y%s".formatted(x, y))).map(JsonElement::getAsString).map("skyblock:item/%s"::formatted).orElse("minecraft:air:1"));
			}
		}

		this.result = new Ingredient("skyblock:item/%s:%s".formatted(jsonObject.get("result").getAsString(), jsonObject.get("count").getAsInt()));
	}
}
