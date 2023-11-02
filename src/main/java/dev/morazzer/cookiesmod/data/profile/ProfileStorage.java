package dev.morazzer.cookiesmod.data.profile;

import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.data.player.PlayerStorage;
import dev.morazzer.cookiesmod.events.api.ProfileSwapEvent;
import dev.morazzer.cookiesmod.utils.DevUtils;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.general.CookiesUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Storage for the {@linkplain dev.morazzer.cookiesmod.data.profile.ProfileData} to always get the correct instance.
 */
public class ProfileStorage {

    private static final Path PROFILE_DATA_FOLDER = Path.of("config/cookiesmod/profiles");
    private static final Identifier SHOW_LOADING_SAVING_MESSAGES = DevUtils.createIdentifier(
        "storage/profiles/show_load_save");
    private static ProfileData profileData;

    static {
        ProfileSwapEvent.AFTER_SET_NO_UUID.register(() -> {
            saveCurrentProfile();
            loadCurrentProfile();
        });
    }

    /**
     * Save the current profile data instance to the file.
     */
    public static void saveCurrentProfile() {
        if (profileData == null) {
            return;
        }

        if (PlayerStorage.getCurrentPlayer().isEmpty() || SkyblockUtils.getLastProfileId().isEmpty()) {
            return;
        }

        if (DevUtils.isEnabled(SHOW_LOADING_SAVING_MESSAGES)) {
            CookiesUtils.sendMessage("Saving " + profileData.getProfileUuid());
        }

        Path playerDirectory = PROFILE_DATA_FOLDER.resolve(profileData.getPlayerUuid().toString());
        Path profileFile = playerDirectory.resolve(profileData.getProfileUuid() + ".json");

        ExceptionHandler.removeThrows(() -> Files.createDirectories(profileFile.getParent()));
        JsonObject jsonObject = JsonUtils.toJsonObject(profileData);
        ProfileDataMigrations.writeLatest(jsonObject);
        ExceptionHandler.removeThrows(() -> Files.writeString(
            profileFile,
            JsonUtils.CLEAN_GSON.toJson(jsonObject),
            StandardCharsets.UTF_8,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.CREATE
        ));
    }

    /**
     * Load the current profile data instance from the file.
     */
    private static void loadCurrentProfile() {
        if (PlayerStorage.getCurrentPlayer().isEmpty() || SkyblockUtils.getLastProfileId().isEmpty()) {
            return;
        }

        if (DevUtils.isEnabled(SHOW_LOADING_SAVING_MESSAGES)) {
            CookiesUtils.sendMessage("Loading " + SkyblockUtils.getLastProfileId().get());
        }

        Path playerDirectory = PROFILE_DATA_FOLDER.resolve(PlayerStorage.getCurrentPlayer().get().toString());
        Path profileFile = playerDirectory.resolve(SkyblockUtils.getLastProfileId().get() + ".json");

        if (!Files.exists(profileFile)) {
            profileData = new ProfileData(
                PlayerStorage.getCurrentPlayer().get(),
                SkyblockUtils.getLastProfileId().get()
            );
            saveCurrentProfile();
            return;
        }

        JsonObject jsonObject = JsonUtils.CLEAN_GSON.fromJson(ExceptionHandler.removeThrows(() -> Files.readString(
            profileFile,
            StandardCharsets.UTF_8
        )), JsonObject.class);
        ProfileDataMigrations.migrate(jsonObject);

        profileData = JsonUtils.fromJson(new ProfileData(
            PlayerStorage.getCurrentPlayer().get(),
            SkyblockUtils.getLastProfileId().get()
        ), jsonObject);
    }

    /**
     * Get the current profile data.
     *
     * @return The current profile data.
     */
    @NotNull
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

        return Optional.ofNullable(profileData);
    }

}
