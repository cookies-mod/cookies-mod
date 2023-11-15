package dev.morazzer.cookiesmod.features.repository.constants;

import com.google.gson.annotations.SerializedName;
import dev.morazzer.cookiesmod.features.repository.files.RepositoryFileAccessor;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import java.util.Collection;
import java.util.Map;
import net.minecraft.util.Identifier;

/**
 * All mining related data in object form.
 */
public record MiningData(@SerializedName("drill_ids") Collection<Identifier> drills,
                         @SerializedName("tank_upgrades") Map<Identifier, Integer> fuelTankUpgrades) {

    private static MiningData instance;

    public static MiningData getInstance() {
        return instance;
    }

    /**
     * Loads all mining-related constants.
     */
    public static void load() {
        instance = JsonUtils.GSON
            .fromJson(RepositoryFileAccessor.getInstance().getFile("constants/drills"), MiningData.class);
    }

}
