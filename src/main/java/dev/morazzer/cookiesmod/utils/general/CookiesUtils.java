package dev.morazzer.cookiesmod.utils.general;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.Optional;

public class CookiesUtils {

	public static Optional<ClientPlayerEntity> getPlayer() {
		return Optional.ofNullable(MinecraftClient.getInstance()).map(client -> client.player);
	}

}
