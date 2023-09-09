package dev.morazzer.cookiesmod.utils.general;

import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.CachedValue;
import dev.morazzer.cookiesmod.utils.DevUtils;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;
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

	private static UUID lastProfileId;

	public static Optional<UUID> getLastProfileId() {
		return Optional.ofNullable(lastProfileId);
	}

	private static void lookForProfileIdMessage(Text text, boolean overlay) {
		if (text.getString().matches("Profile ID: .*")) {
			DevUtils.log("profile.switch", "Found new profile id was {} is {}", lastProfileId, text.getString().substring(12).trim());
			lastProfileId = UUID.fromString(text.getString().substring(12).trim());
		}
	}

	@Override
	public void load() {
		ClientReceiveMessageEvents.GAME.register(SkyblockUtils::lookForProfileIdMessage);
	}
	@Override
	public String getIdentifierPath() {
		return "skyblock_utils";
	}
}
