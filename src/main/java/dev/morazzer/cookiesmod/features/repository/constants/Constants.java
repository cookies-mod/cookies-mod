package dev.morazzer.cookiesmod.features.repository.constants;

import dev.morazzer.cookiesmod.features.repository.files.RepositoryFileAccessor;
import dev.morazzer.cookiesmod.features.repository.items.recipe.Ingredient;
import dev.morazzer.cookiesmod.generated.Area;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import net.minecraft.util.math.Vec3i;

/**
 * Utility class to handle constants.
 */
public class Constants {

    @Getter
    private static Map<String, List<Integer>> hotmPerks;
    @Getter
    private static List<Ingredient> fetchurItems;
    @Getter
    private static Map<Area, Vec3i> locationToCoordinates;


    /**
     * Loads all constant values.
     */
    public static void load() {
        MiningData.load();
        PlotCostData.load();
        CompostUpgradeCost.load();
        Commissions.load();
        loadHotmPerks();
        loadFetchurItems();
        loadLocationToCoordinates();
    }

    private static void loadFetchurItems() {
        fetchurItems =
            JsonUtils.fromJson(RepositoryFileAccessor.getInstance().getFile("constants/fetchur_items"), ArrayList::new,
                Ingredient::fromJson);
    }

    private static void loadHotmPerks() {
        hotmPerks = JsonUtils.CLEAN_GSON.fromJson(RepositoryFileAccessor.getInstance().getFile("constants/hotm_perks"),
            JsonUtils.getTypeToken());
    }

    private static void loadLocationToCoordinates() {
        locationToCoordinates = JsonUtils.fromJson(
            RepositoryFileAccessor.getInstance().getFile("constants/dwarven_locations_to_coordinates"), HashMap::new,
            Area::valueOf, jsonElement ->
                new Vec3i(jsonElement.getAsJsonArray().get(0).getAsInt(),
                    jsonElement.getAsJsonArray().get(1).getAsInt(),
                    jsonElement.getAsJsonArray().get(2).getAsInt())
        );
    }

}
