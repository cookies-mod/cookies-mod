package dev.morazzer.cookiesmod.data.player;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlayerData {

	@SerializedName("player_uuid")
	private UUID playerUUID;
	private String test;

	public PlayerData(UUID player) {
		this.playerUUID = player;
	}

	@Override
	public String toString() {
		return "PlayerData{" +
				"playerUUID=" + playerUUID +
				'}';
	}
}
