package dev.morazzer.cookiesmod.features.farming.garden;

import dev.morazzer.cookiesmod.features.farming.garden.debug.PlotOutlines;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.CachedValue;
import dev.morazzer.cookiesmod.utils.DevUtils;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;
import net.minecraft.util.Identifier;

import java.util.concurrent.TimeUnit;

@LoadModule
public class Garden implements Module {
	private static final Identifier DISABLE_GARDEN_CHECK = DevUtils.createIdentifier("garden/disable_garden_check");
	static CachedValue<Boolean> isOnGarden = new CachedValue<>(Garden::isOnGardenForce, 2, TimeUnit.SECONDS);

	public static boolean isOnGarden() {
		if (SkyblockUtils.getLastServerSwap() + 5000 > System.currentTimeMillis()) {
			isOnGarden.updateNow();
		}
		return (SkyblockUtils.isCurrentlyInSkyblock() && isOnGarden.getValue());
	}

	public static boolean isOnGardenForce() {
		return SkyblockUtils.isCurrentlyInSkyblock()
				&& (LocationUtils.getCurrentLocation().matches(". (:?The Garden|Plot: .+)")
				|| DevUtils.isEnabled(DISABLE_GARDEN_CHECK)
		);
	}

	@Override
	public void load() {
		if (DevUtils.isDevEnvironment()) {
			PlotOutlines.initializePlotOutlinesDebug();
		}
	}

	@Override
	public String getIdentifierPath() {
		return "garden";
	}
}
