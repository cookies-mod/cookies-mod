package dev.morazzer.cookiesmod.features.repository.constants;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.repository.items.recipe.Ingredient;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.general.ItemUtils;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

@Getter
public class CompostUpgradeCost {
	@Getter
	private static CompostUpgradeCost instance;

	private List<CompostUpgrade> speed;
	private List<CompostUpgrade> multiDrop;
	private List<CompostUpgrade> fuelCap;
	private List<CompostUpgrade> organicMatterCap;
	private List<CompostUpgrade> costReduction;

	public CompostUpgradeCost(JsonObject jsonObject) {
		this.speed = getUpgrades(jsonObject.getAsJsonArray("speed"));
		this.multiDrop = getUpgrades(jsonObject.getAsJsonArray("multi_drop"));
		this.fuelCap = getUpgrades(jsonObject.getAsJsonArray("fuel_cap"));
		this.organicMatterCap = getUpgrades(jsonObject.getAsJsonArray("organic_matter_cap"));
		this.costReduction = getUpgrades(jsonObject.getAsJsonArray("cost_reduction"));
	}

	public static boolean loaded() {
		if (instance == null && Files.exists(RepositoryManager.getRepoRoot()
				.resolve("constants/compost_upgrades.json"))) {
			JsonObject jsonObject = GsonUtils.gson.fromJson(ExceptionHandler.removeThrows(() -> Files.readString(
					RepositoryManager.getRepoRoot()
							.resolve("constants/compost_upgrades.json"),
					StandardCharsets.UTF_8
			), "{}"), JsonObject.class);

			instance = new CompostUpgradeCost(jsonObject);
		}
		return instance != null;
	}

	private List<CompostUpgrade> getUpgrades(JsonArray jsonElements) {
		LinkedList<CompostUpgrade> list = new LinkedList<>();
		for (JsonElement jsonElement : jsonElements) {
			if (!jsonElement.isJsonObject()) {
				continue;
			}

			JsonObject jsonObject = jsonElement.getAsJsonObject();
			int copper = jsonObject.get("copper").getAsInt();
			LinkedList<Ingredient> upgradeList = new LinkedList<>();
			JsonObject costObject = jsonObject.getAsJsonObject("cost");
			for (String key : costObject.keySet()) {
				upgradeList.add(new Ingredient("%s:%s".formatted(
						ItemUtils.withNamespace(key),
						costObject.get(key).getAsInt()
				)));
			}
			list.add(new CompostUpgrade(copper, upgradeList));
		}
		return list;
	}

	public record CompostUpgrade(int copper, List<Ingredient> cost) {}

}
