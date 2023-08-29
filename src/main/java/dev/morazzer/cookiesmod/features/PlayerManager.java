package dev.morazzer.cookiesmod.features;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerManager {
    private static final Logger logger = LoggerFactory.getLogger("cookies-player-manager");

    private static final LoadingCache<String, UUID> usernameUUIDCache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(CacheLoader.from(PlayerManager::requestUUID));

    private static final LoadingCache<UUID, String> UUIDUsernameCache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(CacheLoader.from(PlayerManager::requestPlayerName));

    private static UUID requestUUID(String playerName) {
        AtomicReference<UUID> atomicUUID = new AtomicReference<>(null);
        HttpUtils.getResponse(URI.create("https://api.mojang.com/users/profiles/minecraft/" + playerName), response -> {
            if (response == null) {
                logger.warn("Response object from Mojang api equals null");
                return;
            }

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                switch (statusCode) {
                    case 204 -> logger.warn("No uuid found for player {}", playerName);
                    case 429 -> logger.warn("To many requests executed");
                    default -> {
                        try {
                            logger.warn("Error while executing uuid to username request (statusCode={}, body={})", statusCode, new String(response.getEntity().getContent().readAllBytes()));
                        } catch (IOException e) {
                            ExceptionHandler.handleException(e);
                        }
                    }
                }
                return;
            }

            JsonObject fromJson = ExceptionHandler.removeThrows(
                    () -> GsonUtils.gson.fromJson(
                            new String(
                                    response.getEntity()
                                            .getContent()
                                            .readAllBytes()
                            ),
                            JsonObject.class
                    ),
                    GsonUtils.emptyObject
            );

            if (fromJson == GsonUtils.emptyObject) return;
            JsonElement jsonElement = fromJson.get("id");
            String uuidAsString = jsonElement.getAsString();
            if (uuidAsString.length() == 32) {
                uuidAsString = uuidAsString.replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5");
            }
            UUID uuid = UUID.fromString(uuidAsString);
            UUIDUsernameCache.put(uuid, playerName);
            atomicUUID.set(uuid);
        });

        return atomicUUID.get();
    }

    private static String requestPlayerName(UUID uuid) {
        AtomicReference<String> atomicReference = new AtomicReference<>(null);
        HttpUtils.getResponse(URI.create("https://api.mojang.com/user/profile/" + uuid), response -> {
            if (response == null) {
                logger.warn("Response object from Mojang api equals null");
                return;
            }

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                switch (statusCode) {
                    case 204 -> logger.warn("No uuid found for uuid {}", uuid);
                    case 429 -> logger.warn("To many requests executed");
                    default -> {
                        try {
                            logger.warn("Error while executing uuid to username request (statusCode={}, body={})", statusCode, new String(response.getEntity().getContent().readAllBytes()));
                        } catch (IOException e) {
                            ExceptionHandler.handleException(e);
                        }
                    }
                }
                return;
            }

            try {
                JsonObject fromJson = GsonUtils.gson.fromJson(new String(response.getEntity().getContent().readAllBytes()), JsonObject.class);
                JsonElement jsonElement = fromJson.get("name");
                String name = jsonElement.getAsString();
                usernameUUIDCache.put(name, uuid);
                atomicReference.set(name);
            } catch (Exception e) {
                ExceptionHandler.handleException(e);
            }
        });
        return atomicReference.get();
    }

    public static String getUserName(UUID uuid) {
        return ExceptionHandler.removeThrows(() -> UUIDUsernameCache.get(uuid));
    }

    public static UUID getUUID(String playerName) {
        return ExceptionHandler.removeThrows(() -> usernameUUIDCache.get(playerName));
    }
}
