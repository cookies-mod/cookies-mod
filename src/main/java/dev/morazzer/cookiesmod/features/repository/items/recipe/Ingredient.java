package dev.morazzer.cookiesmod.features.repository.items.recipe;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Ingredient extends Identifier {

	private int amount = -1;

	public Ingredient(String namespace, String path, Identifier.ExtraData extraData) {
		super(namespace, path, extraData);
	}

	public Ingredient(String namespace, String path, int amount) {
		super(namespace.toLowerCase(), path.toLowerCase());
		this.amount = amount;
	}

	public Ingredient(String id) {
		super(id.substring(0, id.lastIndexOf(':')).toLowerCase().replace(";", "_").replace("-", "_"));
		if (id.split(":").length >= 2) {
			String[] split = id.split(":");
			String amountString = split[split.length - 1];
			this.amount = Integer.parseInt(amountString);
		}
	}

	public Ingredient(Identifier identifier, int amount) {
		super(identifier.getNamespace(), identifier.getPath());
		this.amount = amount;
	}

	public Ingredient withAmount(int amount) {
		return new Ingredient(this.getNamespace(), this.getPath(), amount);
	}

	public int getAmount() {
		return amount;
	}


	public static Set<Ingredient> mergeToSet(Iterable<Ingredient> ingredients) {
		return mergeIngredients(ingredients, Collectors.toSet());
	}

	public static List<Ingredient> mergeToList(Iterable<Ingredient> ingredients) {
		return mergeIngredients(ingredients, Collectors.toList());
	}

	/**
	 * Merges any given Iterable of Ingredients to not contain duplicated keys
	 *
	 * @param ingredients The original list of ingredients
	 * @param collector   The collector to create the returned value
	 * @param <T>         The return t ype of the collector
	 * @return The collected ingredients
	 */
	public static <T> T mergeIngredients(Iterable<Ingredient> ingredients, Collector<Ingredient, ?, T> collector) {
		HashMap<String, Ingredient> ingredientMap = new HashMap<>();
		ingredients.forEach(ingredient ->
				ingredientMap.merge(
						ingredient.toString(),
						ingredient,
						(ingredient1, ingredient2) -> ingredient1.withAmount(ingredient1.getAmount() + ingredient2.getAmount())
				)
		);

		return ingredientMap.values().stream().collect(collector);
	}

	/**
	 * Checks if the ingredient is air or a valid item
	 *
	 * @return Rather the ingredient is air or not
	 */
	public boolean isAir() {
		return this.getNamespace().equals("minecraft") && this.getPath().equals("air");
	}

}
