package dev.morazzer.cookiesmod.features.repository.constants;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import lombok.Getter;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MiningData {

    @Getter
    private static MiningData instance;
    private final Collection<Identifier> drills = new ArrayList<>();
    private final Map<Identifier, Integer> parts = new HashMap<>();

    public MiningData() {
        RepositoryManager.getResource("constants/drills.json").ifPresent(this::parseDrillsAndParts);
    }

    /**
     * Loads all mining-related constants.
     */
    public static void load() {
        instance = new MiningData();
    }

    /**
     * Loads the drill and drill parts from a {@linkplain com.google.gson.JsonObject} formatted as byte[].
     *
     * @param bytes The json object as a byte array.
     */
    private void parseDrillsAndParts(byte[] bytes) {
        JsonObject jsonObject = JsonUtils.CLEAN_GSON.fromJson(
                new String(bytes, StandardCharsets.UTF_8),
                JsonObject.class
        );
        this.drills.clear();

        for (JsonElement jsonElement : jsonObject.getAsJsonArray("drill_ids")) {
            if (!jsonElement.isJsonPrimitive()) {
                continue;
            }
            this.drills.add(new Identifier(jsonElement.getAsString()));
        }

        this.parts.clear();
        JsonObject tankUpgrades = jsonObject.getAsJsonObject("tank_upgrades");
        for (String key : tankUpgrades.keySet()) {
            this.parts.put(new Identifier(key), tankUpgrades.get(key).getAsInt());
        }
    }

}