package dev.morazzer.cookiesmod.data.profile;

import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.data.migrations.Migration;
import dev.morazzer.cookiesmod.data.profile.migrations.ProfileDataMigration_0001;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ProfileDataMigrations {

    private static final String KEY = "migration";
    private static final List<Migration<JsonObject>> MIGRATIONS = new LinkedList<>();
    private static final long LATEST;

    static {
        MIGRATIONS.add(new ProfileDataMigration_0001());
        LATEST = MIGRATIONS
                .stream()
                .min(Comparator.comparingLong(Migration::getNumber))
                .map(Migration::getNumber)
                .orElse(-1L);
    }

    /**
     * Applies all missing migrations to the {@linkplain com.google.gson.JsonObject}.
     *
     * @param jsonObject The config object.
     */
    public static void migrate(JsonObject jsonObject) {
        if (!jsonObject.has(KEY)) {
            jsonObject.addProperty(KEY, 0);
        }

        long lastApplied = jsonObject.get(KEY).getAsLong();
        for (Migration<JsonObject> migration : MIGRATIONS
                .stream()
                .sorted(Comparator.comparingLong(Migration::getNumber))
                .toList()) {
            if (migration.getNumber() > lastApplied) {
                migration.apply(jsonObject);
            }
        }
    }

    /**
     * Writes the latest migration number to the {@linkplain com.google.gson.JsonObject}.
     *
     * @param jsonObject The config object.
     */
    public static void writeLatest(JsonObject jsonObject) {
        jsonObject.addProperty(KEY, LATEST);
    }

}
