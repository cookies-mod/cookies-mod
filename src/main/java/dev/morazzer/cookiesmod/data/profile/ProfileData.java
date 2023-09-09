package dev.morazzer.cookiesmod.data.profile;

import dev.morazzer.cookiesmod.data.player.PlayerStorage;
import dev.morazzer.cookiesmod.utils.general.ScoreboardUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProfileData {
	@Setter(AccessLevel.PRIVATE)
	private UUID playerUuid;
	@Setter(AccessLevel.PRIVATE)
	private UUID profileUuid;
	@Setter(AccessLevel.PRIVATE)
	private GameMode gameMode = GameMode.UNKNOWN;

	public GameMode getGameMode() {
		if (gameMode == GameMode.UNKNOWN && SkyblockUtils.isCurrentlyInSkyblock()) {
			gameMode = ScoreboardUtils.getCurrentGameMode();
			ProfileStorage.saveCurrentProfile();
		}
		return gameMode;
	}

	public ProfileData(UUID playerUuid, UUID profileUuid) {
		this.playerUuid = playerUuid;
		this.profileUuid = profileUuid;
	}

	public boolean isActive() {
		return PlayerStorage.getCurrentPlayer().map(uuid -> uuid == this.playerUuid).orElse(false) && SkyblockUtils.getLastProfileId().map(uuid -> uuid == this.profileUuid).orElse(false);
	}

	@Override
	public String toString() {
		return "ProfileData{" +
				"playerUuid=" + playerUuid +
				", profileUuid=" + profileUuid +
				'}';
	}
}
