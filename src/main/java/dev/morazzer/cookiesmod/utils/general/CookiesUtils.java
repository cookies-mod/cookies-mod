package dev.morazzer.cookiesmod.utils.general;

import dev.morazzer.cookiesmod.CookiesMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;

public class CookiesUtils {

	public static Optional<ClientPlayerEntity> getPlayer() {
		return Optional.ofNullable(MinecraftClient.getInstance()).map(client -> client.player);
	}

	public static void sendMessage(String text, int color) {
		sendMessage(CookiesMod.createPrefix(color).append(text));
	}

	public static void sendMessage(String text) {
		sendMessage(CookiesMod.createPrefix().append(text));
	}

	public static void sendMessage(Text text) {
		sendMessage(text, false);
	}

	public static void sendMessage(Text text, boolean overlay) {
		CookiesUtils.getPlayer().ifPresent(clientPlayerEntity -> clientPlayerEntity.sendMessage(text, overlay));
	}

}
