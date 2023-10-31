package dev.morazzer.cookiesmod.data.profile.migrations;

import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.data.migrations.Migration;

/**
 * Replace snake case with camel case.
 */
public class ProfileDataMigration_0001 implements Migration<JsonObject> {

    @Override
    public long getNumber() {
        return 1;
    }

    @Override
    public void apply(JsonObject value) {
        value.add("playerUuid", value.remove("player_uuid"));
        value.add("profileUuid", value.remove("profile_uuid"));
        value.add("gameMode", value.remove("game_mode"));
        JsonObject dwarvenMinesData = value.remove("dwarven_mines_data").getAsJsonObject();
        dwarvenMinesData.add("lastPuzzlerTime", dwarvenMinesData.remove("last_puzzler_time"));
        dwarvenMinesData.add("lastFetchurTime", dwarvenMinesData.remove("last_fetchur_time"));
        value.add("dwarvenMinesData", dwarvenMinesData);
    }

}
