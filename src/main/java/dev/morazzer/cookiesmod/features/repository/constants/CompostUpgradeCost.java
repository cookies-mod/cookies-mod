package dev.morazzer.cookiesmod.features.repository.constants;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.repository.items.recipe.Ingredient;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.general.ItemUtils;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;

/**
 * Repository item for compost upgrades.
 */
@Getter
public class CompostUpgradeCost {

    @Getter
    private static CompostUpgradeCost instance;

    private final List<CompostUpgrade> speed;
    private final List<CompostUpgrade> multiDrop;
    private final List<CompostUpgrade> fuelCap;
    private final List<CompostUpgrade> organicMatterCap;
    private final List<CompostUpgrade> costReduction;

    /**
     * Loads the compost data from a json object.
     *
     * @param jsonObject The json object.
     */
    public CompostUpgradeCost(JsonObject jsonObject) {
        this.speed = getUpgrades(jsonObject.getAsJsonArray("speed"));
        this.multiDrop = getUpgrades(jsonObject.getAsJsonArray("multi_drop"));
        this.fuelCap = getUpgrades(jsonObject.getAsJsonArray("fuel_cap"));
        this.organicMatterCap = getUpgrades(jsonObject.getAsJsonArray("organic_matter_cap"));
        this.costReduction = getUpgrades(jsonObject.getAsJsonArray("cost_reduction"));
    }

    /**
     * Whether the data was loaded, if not try to load it.
     *
     * @return Whether the data was successfully loaded.
     */
    public static boolean loaded() {
        if (instance == null && Files.exists(RepositoryManager.getRepoRoot()
            .resolve("constants/compost_upgrades.json"))) {
            JsonObject jsonObject = JsonUtils.GSON.fromJson(ExceptionHandler.removeThrows(() -> Files.readString(
                RepositoryManager.getRepoRoot()
                    .resolve("constants/compost_upgrades.json"),
                StandardCharsets.UTF_8
            ), "{}"), JsonObject.class);

            instance = new CompostUpgradeCost(jsonObject);
        }
        return instance != null;
    }

    /**
     * Parse a {@linkplain com.google.gson.JsonArray} to a list of
     * {@linkplain dev.morazzer.cookiesmod.features.repository.constants.CompostUpgradeCost.CompostUpgrade}.
     *
     * @param jsonElements The json array.
     * @return The list of compost upgrades.
     */
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

    /**
     * A compost upgrade.
     *
     * @param copper The amount of copper required.
     * @param cost   The items required.
     */
    public record CompostUpgrade(
        int copper,
        List<Ingredient> cost
    ) {
    }

}
