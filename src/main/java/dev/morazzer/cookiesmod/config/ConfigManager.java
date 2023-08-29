package dev.morazzer.cookiesmod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import io.github.moulberry.moulconfig.processor.BuiltinMoulConfigGuis;
import io.github.moulberry.moulconfig.processor.ConfigProcessorDriver;
import io.github.moulberry.moulconfig.processor.MoulConfigProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public class ConfigManager {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeSpecialFloatingPointValues()
            .enableComplexMapKeySerialization()
            .create();

    private static CookiesConfig config;
    private static MoulConfigProcessor<CookiesConfig> processedConfig;

    public static CookiesConfig getConfig() {
        if (config == null) {
            config = loadConfig();
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
                Files.copy(configFile, configFile.resolve("config.backup.json"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                ExceptionHandler.handleException(e);
            }
        }

        try {
            Files.write(configFile, gson.toJson(config).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private static CookiesConfig loadConfig() {
        if (!Files.exists(configFile)) {
            config = new CookiesConfig();
            saveConfig(false, "first-save");
            CookiesMod.getInstance().setFirstStart();
        }


        try {
            return gson.fromJson(Files.readString(configFile), CookiesConfig.class);
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
            throw new RuntimeException(e);
        }
    }

    public static void processConfig() {
        if (processedConfig != null) {
            throw new IllegalStateException("Config initialize was called multiple times");
        }
        CookiesConfig config = getConfig();
        System.out.println(config);

        MoulConfigProcessor<CookiesConfig> configProcessor = new MoulConfigProcessor<>(config);
        BuiltinMoulConfigGuis.addProcessors(configProcessor);
        ConfigProcessorDriver.processConfig(CookiesConfig.class, config, configProcessor);

        processedConfig = configProcessor;
    }

    public static MoulConfigProcessor<CookiesConfig> getProcessedConfig() {
        return processedConfig;
    }
}
