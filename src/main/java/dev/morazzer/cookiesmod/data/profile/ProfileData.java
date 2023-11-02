package dev.morazzer.cookiesmod.data.profile;

import dev.morazzer.cookiesmod.data.player.PlayerStorage;
import dev.morazzer.cookiesmod.data.profile.mining.DwarvenMinesData;
import dev.morazzer.cookiesmod.data.profile.mining.HeartOfTheMountainData;
import dev.morazzer.cookiesmod.utils.general.ScoreboardUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;
import dev.morazzer.cookiesmod.utils.json.Save;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * The data associated with a profile of a player. Note that the same profile on different accounts has different data.
 */
@Getter
@Setter
public class ProfileData {

    @Setter(AccessLevel.PRIVATE)
    private UUID playerUuid;
    @Setter(AccessLevel.PRIVATE)
    private UUID profileUuid;
    @Setter(AccessLevel.PRIVATE)
    private GameMode gameMode = GameMode.UNKNOWN;
    @Save
    private DwarvenMinesData dwarvenMinesData = new DwarvenMinesData();
    @Save
    private HeartOfTheMountainData heartOfTheMountainData = new HeartOfTheMountainData();

    /**
     * Create a profile.
     *
     * @param playerUuid  The uuid of the owner.
     * @param profileUuid The uuid of the profile.
     */
    public ProfileData(UUID playerUuid, UUID profileUuid) {
        this.playerUuid = playerUuid;
        this.profileUuid = profileUuid;
    }

    /**
     * Get the game mode of the profile.
     *
     * @return The game mode.
     */
    public GameMode getGameMode() {
        if (gameMode == GameMode.UNKNOWN && SkyblockUtils.isCurrentlyInSkyblock()) {
            gameMode = ScoreboardUtils.getCurrentGameMode();
            ProfileStorage.saveCurrentProfile();
        }
        return gameMode;
    }

    /**
     * If the profile is currently active or not.
     *
     * @return If the profile is active.
     */
    public boolean isActive() {
        return PlayerStorage.getCurrentPlayer().map(uuid -> uuid.equals(this.playerUuid)).orElse(false) && SkyblockUtils
            .getLastProfileId()
            .map(uuid -> uuid.equals(this.profileUuid))
            .orElse(false);
    }

    /**
     * The profile in string representation.
     *
     * @return The profile.
     */
    @Override
    public String toString() {
        return "ProfileData{"
            + "playerUuid=" + playerUuid
            + ", profileUuid=" + profileUuid
            + '}';
    }

}
