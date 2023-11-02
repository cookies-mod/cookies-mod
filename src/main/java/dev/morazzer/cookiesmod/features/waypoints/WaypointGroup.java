package dev.morazzer.cookiesmod.features.waypoints;

import dev.morazzer.cookiesmod.utils.LocationUtils;
import java.util.LinkedList;
import java.util.List;

/**
 * A group containing multiple waypoints.
 */
public class WaypointGroup extends Waypoint {

    public final List<Waypoint> waypoints = new LinkedList<>();
    public LocationUtils.Islands island;

}
