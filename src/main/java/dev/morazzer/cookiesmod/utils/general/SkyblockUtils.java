package dev.morazzer.cookiesmod.utils.general;

import dev.morazzer.cookiesmod.utils.CachedValue;

import java.util.concurrent.TimeUnit;

public class SkyblockUtils {

	private static final CachedValue<Boolean> isCurrentlyInSkyblock = new CachedValue<>(
			() -> ScoreboardUtils.getTitle().getString().matches("SK[YI]BLOCK.*"),
			5,
			TimeUnit.SECONDS
	);

	public static boolean isCurrentlyInSkyblock() {
		return isCurrentlyInSkyblock.getValue();
	}


}
