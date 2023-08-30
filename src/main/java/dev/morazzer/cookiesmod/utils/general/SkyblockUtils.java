package dev.morazzer.cookiesmod.utils.general;

import com.mojang.authlib.GameProfile;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.CachedValue;
import dev.morazzer.cookiesmod.utils.DevUtils;
import lombok.Getter;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@LoadModule
public class SkyblockUtils implements Module {
	private static final Identifier DISABLE_SKYBLOCK_CHECK = DevUtils.createIdentifier("disable_skyblock_check");

	private static final CachedValue<Boolean> isCurrentlyInSkyblock = new CachedValue<>(
			() -> ScoreboardUtils.getTitle().getString().matches("SK[YI]BLOCK.*"),
			5,
			TimeUnit.SECONDS
	);

	public static boolean isCurrentlyInSkyblock() {
		return isCurrentlyInSkyblock.getValue() || DevUtils.isEnabled(DISABLE_SKYBLOCK_CHECK);
	}


	@Getter
	private static UUID lastProfileId;

	private static void lookForProfileIdMessage(Text text,
	                                            SignedMessage signedMessage,
	                                            GameProfile gameProfile,
	                                            MessageType.Parameters parameters,
	                                            Instant instant) {
		if (text.getString().matches("Profile ID: .*")) {
			lastProfileId = UUID.fromString(text.getString().substring(12).trim());
		}
	}

	@Override
	public void load() {
		ClientReceiveMessageEvents.CHAT.register(SkyblockUtils::lookForProfileIdMessage);
	}

	@Override
	public String getIdentifierPath() {
		return "skyblock_utils";
	}
}
