package dev.morazzer.cookiesmod.data.profile;

import dev.morazzer.cookiesmod.data.player.PlayerStorage;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class ProfileStorage {
	static Path profileDataFolder = Path.of("config/cookiesmod/profiles");

	private static ProfileData profileData;

	public static void saveCurrentProfile() {
		if (profileData == null) {
			return;
		}

		if (PlayerStorage.getCurrentPlayer().isEmpty() || SkyblockUtils.getLastProfileId().isEmpty()) {
			return;
		}

		Path playerDirectory = profileDataFolder.resolve(profileData.getPlayerUuid().toString());
		Path profileFile = playerDirectory.resolve(profileData.getProfileUuid() + ".json");

		ExceptionHandler.removeThrows(() -> Files.writeString(profileFile, GsonUtils.gson.toJson(profileData), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE));
	}

	private static void loadCurrentProfile() {
		if (PlayerStorage.getCurrentPlayer().isEmpty() || SkyblockUtils.getLastProfileId().isEmpty()) {
			return;
		}

		Path playerDirectory = profileDataFolder.resolve(PlayerStorage.getCurrentPlayer().get().toString());
		Path profileFile = playerDirectory.resolve(SkyblockUtils.getLastProfileId().get() + ".json");

		if (!Files.exists(profileFile)) {
			profileData = new ProfileData(PlayerStorage.getCurrentPlayer().get(), SkyblockUtils.getLastProfileId().get());
			saveCurrentProfile();
			return;
		}

		profileData = GsonUtils.gson.fromJson(ExceptionHandler.removeThrows(() -> Files.readString(profileFile, StandardCharsets.UTF_8)), ProfileData.class);
	}

	public static Optional<ProfileData> getCurrentProfile() {
		if (!SkyblockUtils.isCurrentlyInSkyblock() || PlayerStorage.getCurrentPlayer().isEmpty()) {
			return Optional.empty();
		}

		// Currently loaded profile is still the active one
		if (Optional.ofNullable(profileData).map(ProfileData::isActive).orElse(false)) {
			return Optional.of(profileData);
		}

		saveCurrentProfile();
		loadCurrentProfile();

		return Optional.of(profileData);
	}

}
