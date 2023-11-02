package dev.morazzer.cookiesmod.features.repository.constants;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.features.repository.files.RepositoryFileAccessor;
import lombok.Getter;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class MiningData {

    @Getter
    private static MiningData instance;
    private final Collection<Identifier> drills = new ArrayList<>();
    private final Map<Identifier, Integer> parts = new HashMap<>();

    public MiningData() {
        Optional.ofNullable(RepositoryFileAccessor.getInstance().getFile("constants/drills"))
                .ifPresent(this::parseDrillsAndParts);
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
     * @param jsonElements The json object.
     */
    private void parseDrillsAndParts(JsonElement jsonElements) {
        if (!jsonElements.isJsonObject()) return;

        JsonObject jsonObject = jsonElements.getAsJsonObject();
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
