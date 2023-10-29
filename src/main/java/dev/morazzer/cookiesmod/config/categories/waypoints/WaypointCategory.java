package dev.morazzer.cookiesmod.config.categories.waypoints;

import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Category that describes all settings related to waypoints.
 */
public class WaypointCategory extends Category {

    public BooleanOption enableWaypoints = new BooleanOption(
            Text.literal("Enable Waypoints"),
            Text.literal("Enable or disable ").append(Text.literal("all").formatted(Formatting.BOLD))
                    .append(" waypoints"),
            true
    );

    public WaypointOption waypointOption = new WaypointOption().withHiddenKeys("waypoint", "waypoints");

    @Override
    public Text getName() {
        return Text.literal("Waypoints");
    }

    @Override
    public Text getDescription() {
        return Text.literal("All settings related to waypoints");
    }

}
