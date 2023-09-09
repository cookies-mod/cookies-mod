package dev.morazzer.cookiesmod.data.player;

import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;

public class PlayerStorage {
	static Path playerDataFolder = Path.of("config/cookiesmod/players");

	private static PlayerData playerData;

	public static Optional<UUID> getCurrentPlayer() {
		return Optional.of(MinecraftClient.getInstance()).map(minecraftClient -> minecraftClient.player).map(Entity::getUuid);
	}

	public static void savePlayerData() {
		if (playerData == null) {
			return;
		}

		if (getCurrentPlayer().isEmpty()) {
			return;
		}

		UUID player = getCurrentPlayer().get();
		Path playerDataFile = playerDataFolder.resolve(player + ".json");

		ExceptionHandler.removeThrows(() -> Files.writeString(playerDataFile, GsonUtils.gson.toJson(playerData), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
	}

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

		playerData = GsonUtils.gson.fromJson(ExceptionHandler.removeThrows(() -> Files.readString(playerDataFile)), PlayerData.class);
	}

	public static Optional<PlayerData> getPlayerData() {
		if (!getCurrentPlayer().flatMap(player -> Optional.ofNullable(playerData).map(data -> data.getPlayerUUID().equals(player))).orElse(false)) {
			savePlayerData();
			loadPlayerData();
		}

		return Optional.ofNullable(playerData);
	}

}
