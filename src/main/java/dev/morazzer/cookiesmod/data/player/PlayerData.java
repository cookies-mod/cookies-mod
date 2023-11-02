package dev.morazzer.cookiesmod.data.player;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Data associated with a player/user of the mod.
 */
@Getter
@Setter
public class PlayerData {

    @SerializedName("player_uuid")
    private UUID playerUuid;
    private String test;

    public PlayerData(UUID player) {
        this.playerUuid = player;
    }

    @Override
    public String toString() {
        return "PlayerData{"
            + "playerUUID=" + playerUuid
            + '}';
    }

}
