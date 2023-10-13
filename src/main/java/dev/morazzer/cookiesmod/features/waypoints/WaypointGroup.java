package dev.morazzer.cookiesmod.features.waypoints;

import dev.morazzer.cookiesmod.utils.LocationUtils;

import java.util.LinkedList;
import java.util.List;

public class WaypointGroup extends Waypoint {

    public List<Waypoint> waypoints = new LinkedList<>();
    public LocationUtils.Islands island;

}
