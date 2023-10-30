package dev.morazzer.cookiesmod.features.waypoints;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.generated.Area;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import me.x150.renderer.render.Renderer3d;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * The feature to render all waypoints into the world.
 */
@LoadModule("waypoints")
public class WaypointManager implements Module {

    private static final HashMap<UUID, WaypointGroup> waypointGroupHashMap = new LinkedHashMap<>();
    private static final WaypointGroup groupLess = new WaypointGroup();

    private static final Path waypointsFile = ConfigManager.getConfigFolder().resolve("waypoints.json");

    private static WaypointManager waypointManager;

    /**
     * Initialize waypoint rendering manager.
     */
    public WaypointManager() {
        waypointManager = this;
    }

    /**
     * Get the manager instance.
     *
     * @return The instance.
     */
    public static Optional<WaypointManager> getWaypointManager() {
        return Optional.ofNullable(waypointManager);
    }

    /**
     * Load all waypoints from a string json object.
     *
     * @param waypointsString The json object.
     */
    public void loadWaypoints(String waypointsString) {
        JsonObject jsonObject = GsonUtils.gsonClean.fromJson(waypointsString, JsonObject.class);
        if (jsonObject.isEmpty()) return;

        JsonArray categories = jsonObject.getAsJsonArray("categories");
        if (categories == null) return;
        for (JsonElement categoryElement : categories) {
            JsonObject category = categoryElement.getAsJsonObject();
            WaypointGroup group = new WaypointGroup();
            group.category = UUID.fromString(category.get("uuid").getAsString());
            group.enabled = category.has("enabled") && category.get("enabled").getAsBoolean();
            group.area = StreamSupport.stream(category.get("areas").getAsJsonArray().spliterator(), false)
                    .map(JsonElement::getAsString).map(Area::valueOf).toArray(Area[]::new);
            group.color = category.has("color") ? new Color(category.get("color")
                    .getAsInt()) : new Color(ColorUtils.mainColor);
            group.name = Text.Serializer.fromJson(category.get("name"));
            group.island = LocationUtils.Islands.valueOfOrUnknown(category.get("island").getAsString());
            waypointGroupHashMap.put(group.category, group);
        }

        JsonArray waypoints = jsonObject.getAsJsonArray("waypoints");
        if (waypoints == null) return;
        for (JsonElement jsonElement : waypoints) {
            JsonObject waypointElement = jsonElement.getAsJsonObject();
            Waypoint waypoint = new Waypoint();
            waypoint.position = GsonUtils.gsonClean.fromJson(waypointElement.get("position"), Vec3d.class);
            waypoint.area = waypointElement.has("areas") ? StreamSupport.stream(waypointElement.get("areas")
                            .getAsJsonArray().spliterator(), false).map(JsonElement::getAsString).map(Area::valueOf)
                    .toArray(Area[]::new) : new Area[] {};
            waypoint.enabled = waypointElement.has("enabled") && waypointElement.get("enabled").getAsBoolean();
            waypoint.category = waypointElement.has("group") ? UUID.fromString(waypointElement.get("group")
                    .getAsString()) : null;
            waypoint.name = waypointElement.has("name") ? Text.Serializer.fromJson(waypointElement.get("name")) : null;
            waypoint.color = waypointElement.has("color") ? new Color(waypointElement.get("color")
                    .getAsInt()) : new Color(ColorUtils.mainColor);

            if (waypoint.category == null) {
                groupLess.waypoints.add(waypoint);
            } else {
                waypointGroupHashMap.get(waypoint.category).waypoints.add(waypoint);
            }
        }
    }

    /**
     * Register all waypoint renderers and load the waypoints from the file.
     */
    @Override
    public void load() {
        try {
            if (Files.notExists(waypointsFile)) {
                Files.writeString(waypointsFile, "{}", StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
            }

            String content = Files.readString(waypointsFile, StandardCharsets.UTF_8);
            loadWaypoints(content);
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            for (WaypointGroup value : waypointGroupHashMap.values()) {
                if (!value.enabled) continue;
                if (LocationUtils.getCurrentIsland() != value.island) continue;
                if (value.area.length > 0) {
                    boolean match = false;
                    for (Area area : value.area) {
                        match = match || area == LocationUtils.getCurrentArea();
                    }
                    if (!match) {
                        continue;
                    }
                }
                renderGroup(context, value);
            }
            renderGroup(context, groupLess);
        });
    }

    @Override
    public String getIdentifierPath() {
        return "";
    }

    /**
     * Render one waypoint group into the world.
     *
     * @param context The world render context.
     * @param value   The group to render.
     */
    private void renderGroup(WorldRenderContext context, WaypointGroup value) {
        for (Waypoint waypoint : value.waypoints) {
            Color color = Objects.requireNonNullElse(waypoint.color, value.color);
            double v = waypoint.position.distanceTo(MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.getPos() : Vec3d.ZERO);
            if (v < 0) {
                Renderer3d.renderFilled(
                        context.matrixStack(),
                        new Color((color.getRGB() & 0xFFFFFF) | 0x99 << 24, true),
                        waypoint.position.add(0.001, 0.001, 0.001),
                        new Vec3d(0.998, 0.998, 0.998)
                );
            }

            RenderUtils.renderTextInWorld(
                    context.matrixStack(),
                    waypoint.position.add(0.5, 0.5, 0.5),
                    Objects.requireNonNullElse(waypoint.name, value.name),
                    context.consumers(),
                    Math.max(0.02f, (float) (0.02f * (v / 10f))),
                    true,
                    true,
                    color.getRGB() | 0xFF << 24
            );

            RenderUtils.renderTextInWorld(
                    context.matrixStack(),
                    waypoint.position.add(0.5, 0.25 - (0.11 * (v / 10f)), 0.5),
                    Text.literal("%.2f".formatted(v))
                            .append("m").formatted(Formatting.YELLOW),
                    context.consumers(),
                    Math.max(0.02f, (float) (0.02f * (v / 10f))),
                    true,
                    true,
                    -1
            );
        }
    }

}
