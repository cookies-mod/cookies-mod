package dev.morazzer.cookiesmod.features.garden;

import dev.morazzer.cookiesmod.utils.CachedValue;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;

import java.util.concurrent.TimeUnit;

public class Garden {
	static CachedValue<Boolean> isOnGarden = new CachedValue<>(Garden::isOnGardenForce, 2, TimeUnit.SECONDS);

	public static void loadGardenFeatures() {
		CropMilestoneTracker.initializeCropMilestoneTracker();
	}

	public static boolean isOnGarden() {
		return SkyblockUtils.isCurrentlyInSkyblock() && isOnGarden.getValue();
	}

	public static boolean isOnGardenForce() {
		return SkyblockUtils.isCurrentlyInSkyblock() && LocationUtils.getCurrentLocation().matches(". (:?The Garden|Plot: .+)");
	}
}
