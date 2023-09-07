package dev.morazzer.cookiesmod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.config.system.parsed.ConfigProcessor;
import dev.morazzer.cookiesmod.config.system.parsed.ConfigReader;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

@Slf4j
public class ConfigManager {

	@Getter
	private static final Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.excludeFieldsWithoutExposeAnnotation()
			.serializeSpecialFloatingPointValues()
			.enableComplexMapKeySerialization()
			.create();

	private static CookiesConfig config;
	@Getter
	private static ConfigReader configReader;

	public static CookiesConfig getConfig() {
		if (config == null) {
			processConfig();
		}

		return config;
	}

	private static final Path configFolder = Path.of("config/cookiesmod");
	private static final Path configFile = configFolder.resolve("config.json");

	public static void saveConfig(boolean createBackup, String reason) {
		if (!Files.exists(configFolder)) {
			try {
				Files.createDirectories(configFolder);
			} catch (IOException e) {
				ExceptionHandler.handleException(e);
			}
		}

		if (createBackup) {
			try {
				Files.copy(configFile, configFile.resolveSibling("config.backup.json"), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				ExceptionHandler.handleException(e);
			}
		}

		try {
			Files.writeString(configFile, gson.toJson(config.save()), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
		}
		log.info("Saving config with with reason: {}", reason);
	}

	private static JsonObject loadConfig() {
		if (Files.exists(configFile)) {
			return gson.fromJson(ExceptionHandler.removeThrows(() -> Files.readString(configFile), "{}"), JsonObject.class);
		} else {
			saveConfig(false, "first-save");
		}

		return new JsonObject();
	}

	public static void processConfig() {
		config = new CookiesConfig().load(loadConfig());
		configReader = new ConfigReader();
		ConfigProcessor.processConfig(config, configReader);
	}

}
