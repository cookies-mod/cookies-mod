package dev.morazzer.cookiesmod.utils.general;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.HttpUtils;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Various miscellaneous helpers.
 */
public class CookiesUtils {

    private static final Logger logger = LoggerFactory.getLogger("cookies-utils");

    /**
     * Gets the current player.
     *
     * @return The current player.
     */
    public static Optional<ClientPlayerEntity> getPlayer() {
        return Optional.ofNullable(MinecraftClient.getInstance()).map(client -> client.player);
    }

    /**
     * Adds a message to the chat.
     *
     * @param text  The message.
     * @param color The color the message should be in.
     */
    public static void sendMessage(String text, int color) {
        sendMessage(CookiesMod.createPrefix(color).append(text));
    }

    /**
     * Adds a message to the chat.
     *
     * @param text The message.
     */
    public static void sendMessage(String text) {
        sendMessage(CookiesMod.createPrefix().append(text));
    }

    /**
     * Adds a text message to the chat.
     *
     * @param text The message.
     */
    public static void sendMessage(Text text) {
        sendMessage(text, false);
    }

    /**
     * Adds a message to the chat.
     *
     * @param text    The message.
     * @param overlay If the message should be sent as overlay or not.
     */
    public static void sendMessage(Text text, boolean overlay) {
        CookiesUtils.getPlayer().ifPresent(clientPlayerEntity -> clientPlayerEntity.sendMessage(text, overlay));
    }

    /**
     * Gets the uuid of a username.
     *
     * @param username The username.
     * @return The uuid.
     */
    public static Optional<UUID> getUuid(String username) {
        AtomicReference<UUID> atomicUuid = new AtomicReference<>(null);
        HttpUtils.getResponse(URI.create("https://api.mojang.com/users/profiles/minecraft/" + username), response -> {
            if (response == null) {
                logger.warn("Response object from Mojang api equals null");
                return;
            }

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                switch (statusCode) {
                    case 204 -> logger.warn("No uuid found for player {}", username);
                    case 429 -> logger.warn("To many requests executed");
                    default -> {
                        try {
                            logger.warn(
                                "Error while executing uuid to username request (statusCode={}, body={})",
                                statusCode,
                                new String(response.getEntity().getContent().readAllBytes())
                            );
                        } catch (IOException e) {
                            ExceptionHandler.handleException(e);
                        }
                    }
                }
                return;
            }

            JsonObject fromJson = ExceptionHandler.removeThrows(
                () -> JsonUtils.GSON.fromJson(
                    new String(
                        response.getEntity()
                            .getContent()
                            .readAllBytes()
                    ),
                    JsonObject.class
                ),
                new JsonObject()
            );

            if (fromJson.isEmpty()) {
                return;
            }
            JsonElement jsonElement = fromJson.get("id");
            String uuidAsString = jsonElement.getAsString();
            if (uuidAsString.length() == 32) {
                uuidAsString = uuidAsString.replaceFirst(
                    "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)",
                    "$1-$2-$3-$4-$5"
                );
            }

            atomicUuid.set(UUID.fromString(uuidAsString));
        });

        return Optional.ofNullable(atomicUuid.get());
    }

    /**
     * Gets the username of an uuid.
     *
     * @param uuid The uuid.
     * @return The username.
     */
    public static Optional<String> getUsername(UUID uuid) {
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
                            logger.warn(
                                "Error while executing uuid to username request (statusCode={}, body={})",
                                statusCode,
                                new String(response.getEntity().getContent().readAllBytes())
                            );
                        } catch (IOException e) {
                            ExceptionHandler.handleException(e);
                        }
                    }
                }
                return;
            }

            try {
                JsonObject fromJson = JsonUtils.GSON.fromJson(new String(response
                    .getEntity()
                    .getContent()
                    .readAllBytes()), JsonObject.class);
                JsonElement jsonElement = fromJson.get("name");

                atomicReference.set(jsonElement.getAsString());
            } catch (Exception e) {
                ExceptionHandler.handleException(e);
            }
        });
        return Optional.ofNullable(atomicReference.get());
    }

}
