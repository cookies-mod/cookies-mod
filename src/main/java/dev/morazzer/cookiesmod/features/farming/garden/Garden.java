package dev.morazzer.cookiesmod.features.farming.garden;

import dev.morazzer.cookiesmod.features.farming.garden.debug.PlotOutlines;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.DevUtils;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import net.minecraft.util.Identifier;

/**
 * Various miscellaneous garden features.
 */
@LoadModule("garden")
public class Garden implements Module {

    private static final Identifier DISABLE_GARDEN_CHECK = DevUtils.createIdentifier("garden/disable_garden_check");

    /**
     * If the player is currently on the island
     *
     * @return If the player is on the garden.
     */
    public static boolean isOnGarden() {
        return DevUtils.isEnabled(DISABLE_GARDEN_CHECK) || LocationUtils.getCurrentIsland() == LocationUtils.Islands.GARDEN;
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
