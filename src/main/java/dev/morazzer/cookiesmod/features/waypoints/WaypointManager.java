package dev.morazzer.cookiesmod.features.waypoints;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.generated.Area;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.DevUtils;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
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
    private static final Identifier SHOW_DEBUG_INFO = DevUtils.createIdentifier("waypoints/show_debug_info");

    private static final HashMap<UUID, WaypointGroup> WAYPOINT_GROUP_HASH_MAP = new LinkedHashMap<>();
    private static final HashMap<UUID, WaypointGroup> CODING_GROUPS = new LinkedHashMap<>();
    private static final WaypointGroup GROUP_LESS = new WaypointGroup();

    private static final Path WAYPOINTS_FILE = ConfigManager.getConfigFolder().resolve("waypoints.json");

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
        JsonObject jsonObject = JsonUtils.CLEAN_GSON.fromJson(waypointsString, JsonObject.class);
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
            WAYPOINT_GROUP_HASH_MAP.put(group.category, group);
        }

        JsonArray waypoints = jsonObject.getAsJsonArray("waypoints");
        if (waypoints == null) return;
        for (JsonElement jsonElement : waypoints) {
            JsonObject waypointElement = jsonElement.getAsJsonObject();
            Waypoint waypoint = new Waypoint();
            waypoint.position = JsonUtils.CLEAN_GSON.fromJson(waypointElement.get("position"), Vec3d.class);
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
                GROUP_LESS.waypoints.add(waypoint);
            } else {
                WAYPOINT_GROUP_HASH_MAP.get(waypoint.category).waypoints.add(waypoint);
            }
        }
    }

    /**
     * Register all waypoint renderers and load the waypoints from the file.
     */
    @Override
    public void load() {
        try {
            if (Files.notExists(WAYPOINTS_FILE)) {
                Files.writeString(WAYPOINTS_FILE, "{}", StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
            }

            String content = Files.readString(WAYPOINTS_FILE, StandardCharsets.UTF_8);
            loadWaypoints(content);
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            MinecraftClient.getInstance().getProfiler().swap("cookiesmod");
            MinecraftClient.getInstance().getProfiler().push("waypoints");
            for (WaypointGroup value : WAYPOINT_GROUP_HASH_MAP.values()) {
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
            for (WaypointGroup value : CODING_GROUPS.values()) {
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
            renderGroup(context, GROUP_LESS);
            MinecraftClient.getInstance().getProfiler().pop();
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
        MinecraftClient.getInstance().getProfiler()
                .push(GROUP_LESS == value ? "groupLess" : String.valueOf(value.category));
        for (Waypoint waypoint : value.waypoints) {

            Text text = Objects.requireNonNullElse(waypoint.name, value.name);
            MinecraftClient.getInstance().getProfiler().push(text.getString());
            Color color = Objects.requireNonNullElse(waypoint.color, value.color);
            double v = waypoint.position.distanceTo(MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.getPos() : Vec3d.ZERO);

            if (v < 10) {
                DebugRenderer.drawBox(
                        context.matrixStack(),
                        context.consumers(),
                        new BlockPos((int) waypoint.position.x, (int) waypoint.position.y, (int) waypoint.position.z),
                        0,
                        255,
                        255,
                        255,
                        0x99
                );
            }

            Vec3d position = waypoint.position;

            if (DevUtils.isEnabled(SHOW_DEBUG_INFO)) {
                Vec3d debugLocation = position.add(0.5, 3, 0.5);
                DebugRenderer.drawString(
                        context.matrixStack(),
                        context.consumers(),
                        "Category: " + waypoint.category,
                        debugLocation.x,
                        debugLocation.y,
                        debugLocation.z,
                        -1
                );
                debugLocation = debugLocation.subtract(0, 0.2, 0);
                DebugRenderer.drawString(
                        context.matrixStack(),
                        context.consumers(),
                        "Group name: " + value.name,
                        debugLocation.x,
                        debugLocation.y,
                        debugLocation.z,
                        -1
                );
                debugLocation = debugLocation.subtract(0, 0.2, 0);
                DebugRenderer.drawString(
                        context.matrixStack(),
                        context.consumers(),
                        "Group island: " + value.island,
                        debugLocation.x,
                        debugLocation.y,
                        debugLocation.z,
                        -1
                );
                debugLocation = debugLocation.subtract(0, 0.2, 0);
                DebugRenderer.drawString(context.matrixStack(), context.consumers(), "Areas: " + Arrays.toString(
                        waypoint.area), debugLocation.x, debugLocation.y, debugLocation.z, -1);


            }

            RenderUtils.renderTextInWorld(
                    context.matrixStack(),
                    position.add(0.5, 0.5, 0.5),
                    text,
                    context.consumers(),
                    Math.max(0.02f, (float) (0.02f * (v / 10f))),
                    true,
                    true,
                    color.getRGB() | 0xFF << 24
            );

            RenderUtils.renderTextInWorld(
                    context.matrixStack(),
                    position.add(0.5, 0.25 - (0.11 * (v / 10f)), 0.5),
                    Text.literal("%.2f".formatted(v))
                            .append("m").formatted(Formatting.YELLOW),
                    context.consumers(),
                    Math.max(0.02f, (float) (0.02f * (v / 10f))),
                    true,
                    true,
                    -1
            );

            MinecraftClient.getInstance().getProfiler().pop();
        }
        MinecraftClient.getInstance().getProfiler().pop();
    }

    public static void addAnonymousWaypoints(Text name, Vec3d vec3d) {
        Waypoint waypoint = new Waypoint();
        waypoint.name = name;
        waypoint.position = vec3d;
        waypoint.color = Color.RED;
        GROUP_LESS.waypoints.add(waypoint);
    }

    public static void addCodingWaypoint(UUID uuid, Waypoint waypoint) {
        CODING_GROUPS.get(uuid).waypoints.add(waypoint);
    }

    public static void addCodingGroup(WaypointGroup waypointGroup) {
        CODING_GROUPS.put(waypointGroup.category, waypointGroup);
    }

    public static boolean hasCodingGroup(UUID uuid) {
        return CODING_GROUPS.containsKey(uuid);
    }

    public static void clearCodingGroup(UUID uuid) {
        CODING_GROUPS.get(uuid).waypoints.clear();
    }
}
