package dev.morazzer.cookiesmod.utils;

import net.minecraft.client.MinecraftClient;

public class ChatUtils {

    public void sendCommand(String command) {
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand(command);
        }
    }

    public void sendPartyChatMessage(String message) {
        sendCommand("/pc %s".formatted(message));
    }

}
