package dev.morazzer.cookiesmod.features.mining.commissions;

import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.repository.constants.Commissions;
import dev.morazzer.cookiesmod.features.repository.constants.Constants;
import dev.morazzer.cookiesmod.features.waypoints.Waypoint;
import dev.morazzer.cookiesmod.features.waypoints.WaypointGroup;
import dev.morazzer.cookiesmod.features.waypoints.WaypointManager;
import dev.morazzer.cookiesmod.generated.Area;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import java.awt.Color;
import java.util.Map;
import java.util.UUID;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/**
 * Commission manager.
 */
@LoadModule("mining/commissions")
public class CommissionManager implements Module {

    public static CommissionManager instance;
    private final UUID waypointGroup = UUID.randomUUID();

    {
        instance = this;
    }

    /**
     * Gets the internal key from the display name.
     *
     * @param commissionName The display name.
     * @return The internal key.
     */
    public String getCommissionKey(String commissionName) {
        return Commissions.getInstance().namesToKey().get(commissionName);
    }

    /**
     * Gets the amount required to complete the commission or 1 if not specified.
     *
     * @param key The internal key.
     * @return The amount required.
     */
    public int getCommissionAmount(String key) {
        return Commissions.getInstance().commissions().getOrDefault(key, 1).intValue();
    }

    @Override
    public void load() {
        WaypointGroup waypointGroup = new WaypointGroup();
        waypointGroup.name = Text.literal("Dwarven Mines");
        waypointGroup.color = Color.RED;
        waypointGroup.category = this.waypointGroup;
        waypointGroup.island = LocationUtils.Islands.DWARVEN_MINES;
        waypointGroup.enabled = true;
        WaypointManager.addCodingGroup(waypointGroup);
        RepositoryManager.addReloadCallback(this::reload);
        if (RepositoryManager.isFinishedLoading()) {
            reload();
        }
    }

    @Override
    public void reload() {
        WaypointManager.clearCodingGroup(this.waypointGroup);
        for (Map.Entry<Area, Vec3i> areaVec3iEntry : Constants.getLocationToCoordinates().entrySet()) {
            Waypoint waypoint = new Waypoint();
            waypoint.position = Vec3d.of(areaVec3iEntry.getValue());
            waypoint.name = Text.literal(areaVec3iEntry.getKey().scoreboard);
            WaypointManager.addCodingWaypoint(this.waypointGroup, waypoint);
        }
    }

    @Override
    public String getIdentifierPath() {
        return "mining/commissions";
    }

}
