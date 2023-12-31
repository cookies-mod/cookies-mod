package dev.morazzer.cookiesmod.features.waypoints;

import dev.morazzer.cookiesmod.generated.Area;
import java.awt.Color;
import java.util.UUID;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

/**
 * A waypoint that can be rendered into the world.
 */
public class Waypoint {

    public Area[] area = new Area[] {};
    public Vec3d position;
    public Text name;
    public Color color;
    public UUID category;
    public boolean enabled;

}
