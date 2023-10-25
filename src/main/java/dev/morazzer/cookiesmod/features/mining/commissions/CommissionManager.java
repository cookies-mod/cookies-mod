package dev.morazzer.cookiesmod.features.mining.commissions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.waypoints.Waypoint;
import dev.morazzer.cookiesmod.features.waypoints.WaypointGroup;
import dev.morazzer.cookiesmod.features.waypoints.WaypointManager;
import dev.morazzer.cookiesmod.generated.Area;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@LoadModule("mining/commissions")
public class CommissionManager implements Module {
    public static CommissionManager instance;

    {
        instance = this;
    }

    private final Map<Area, Vec3d> locations = new LinkedHashMap<>();
    private final Map<String, Integer> commissions = new LinkedHashMap<>();
    private final Map<String, String> namesToValue = new LinkedHashMap<>();
    private final UUID waypointGroup = UUID.randomUUID();

    @Override
    public void load() {
        WaypointGroup waypointGroup = new WaypointGroup();
        waypointGroup.name = Text.literal("Dwarven Mines");
        waypointGroup.color = Color.RED;
        waypointGroup.category = this.waypointGroup;
        waypointGroup.island = LocationUtils.Islands.DWARVEN_MINES;
        waypointGroup.enabled = true;
        WaypointManager.addCodingGroup(waypointGroup);
        RepositoryManager.addReloadCallback(this::loadRepo);
        if (RepositoryManager.isFinishedLoading()) this.loadRepo();
    }

    private void loadRepo() {
        RepositoryManager.getResource("constants/dwarven_locations_to_coordinates.json").ifPresent(this::loadLocations);
        RepositoryManager.getResource("constants/commissions.json").ifPresent(this::loadCommissions);
    }

    private void loadCommissions(byte[] bytes) {
        this.commissions.clear();
        JsonObject jsonObject = GsonUtils.gsonClean.fromJson(
                new String(bytes, StandardCharsets.UTF_8),
                JsonObject.class
        );

        this.commissions.putAll(GsonUtils.gsonClean.fromJson(jsonObject.get("values"), new TypeToken<>() {}));
        this.namesToValue.putAll(GsonUtils.gsonClean.fromJson(jsonObject.get("name_to_value"), new TypeToken<>() {}));
    }

    private void loadLocations(byte[] bytes) {
        this.locations.clear();
        JsonObject jsonObject = GsonUtils.gsonClean.fromJson(
                new String(bytes, StandardCharsets.UTF_8),
                JsonObject.class
        );
        WaypointManager.clearCodingGroup(this.waypointGroup);

        for (String key : jsonObject.keySet()) {
            JsonArray entry = jsonObject.getAsJsonArray(key);
            int x = entry.get(0).getAsInt();
            int y = entry.get(1).getAsInt();
            int z = entry.get(2).getAsInt();
            Vec3d coordinates = new Vec3d(x, y, z);
            Area area = Area.valueOf(key);
            this.locations.put(area, coordinates);
            Waypoint waypoint = new Waypoint();
            waypoint.position = coordinates;
            waypoint.name = Text.literal(area.scoreboard);
            WaypointManager.addCodingWaypoint(waypointGroup, waypoint);
        }
    }

    public String getCommissionKey(String commissionName) {
        return this.namesToValue.get(commissionName);
    }

    public int getCommissionAmount(String key) {
        return this.commissions.getOrDefault(key, 1);
    }

    @Override
    public String getIdentifierPath() {
        return "mining/commissions";
    }
}
