package dev.morazzer.cookiesmod.data.player;

import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;

/**
 * Storage for the {@linkplain dev.morazzer.cookiesmod.data.player.PlayerData} to always get the correct instance.
 */
public class PlayerStorage {

    static final Path playerDataFolder = Path.of("config/cookiesmod/players");

    private static PlayerData playerData;

    /**
     * Return the currently active players uuid.
     *
     * @return The uuid.
     */
    public static Optional<UUID> getCurrentPlayer() {
        return Optional
                .of(MinecraftClient.getInstance())
                .map(minecraftClient -> minecraftClient.player)
                .map(Entity::getUuid);
    }

    /**
     * Save the current player data to the file.
     */
    public static void savePlayerData() {
        if (playerData == null) {
            return;
        }

        if (getCurrentPlayer().isEmpty()) {
            return;
        }

        UUID player = getCurrentPlayer().get();
        Path playerDataFile = playerDataFolder.resolve(player + ".json");

        ExceptionHandler.removeThrows(() -> Files.writeString(
                playerDataFile,
                JsonUtils.GSON.toJson(playerData),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        ));
    }

    /**
     * Load the current player data from the file.
     */
    private static void loadPlayerData() {
        if (getCurrentPlayer().isEmpty()) {
            return;
        }
        UUID player = getCurrentPlayer().get();
        Path playerDataFile = playerDataFolder.resolve(player + ".json");

        if (!Files.exists(playerDataFolder)) {
            ExceptionHandler.removeThrows(() -> Files.createDirectories(playerDataFolder));
        }

        if (!Files.exists(playerDataFile)) {
            playerData = new PlayerData(player);
            savePlayerData();
        }

        playerData = JsonUtils.GSON.fromJson(
                ExceptionHandler.removeThrows(() -> Files.readString(playerDataFile)),
                PlayerData.class
        );
    }

    /**
     * Get the current player data or load it.
     *
     * @return The player data.
     */
    @NotNull
    public static Optional<PlayerData> getPlayerData() {
        if (!getCurrentPlayer()
                .flatMap(player -> Optional.ofNullable(playerData).map(data -> data.getPlayerUUID().equals(player)))
                .orElse(false)) {
            savePlayerData();
            loadPlayerData();
        }

        return Optional.ofNullable(playerData);
    }

}
