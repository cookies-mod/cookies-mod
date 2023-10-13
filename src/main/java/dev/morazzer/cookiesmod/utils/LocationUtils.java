package dev.morazzer.cookiesmod.utils;

import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.commands.dev.subcommands.TestEntrypoint;
import dev.morazzer.cookiesmod.generated.Area;
import dev.morazzer.cookiesmod.utils.general.CookiesUtils;
import dev.morazzer.cookiesmod.utils.general.ScoreboardUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;

import java.time.Duration;
import java.util.function.Function;


public class LocationUtils {

    public static String getCurrentLocation() {
        return ScoreboardUtils.getCurrentLocation();
    }

    @TestEntrypoint("print_current_area")
    public static void printCurrentArea() {
        CookiesUtils.sendMessage(CookiesMod.createPrefix(-1).append(currentArea.getValue().name()));
        CookiesUtils.sendMessage(CookiesMod.createPrefix(-1).append(Islands.getIsland(currentArea.getValue()).name()));
    }

    public static Area getCurrentArea() {
        if (SkyblockUtils.getLastServerSwap() + 2000 < System.currentTimeMillis()) {
            currentArea.updateNow();
        }
        return currentArea.getValue();
    }

    public static Islands getCurrentIsland() {
        return Islands.getIsland(getCurrentArea());
    }

    public static CachedValue<Area> currentArea = new CachedValue<>(() -> {
        String currentLocation = getCurrentLocation().trim();
        for (Area value : Area.values()) {
            if (!value.regex) {
                if (currentLocation.equals(value.scoreboard)) {
                    return value;
                } else {
                    continue;
                }
            }
            if (currentLocation.matches(value.scoreboard)) {
                return value;
            }
        }
        return Area.CATACOMBS_FLOOR_7;
    }, Duration.ofSeconds(5));

    public enum Islands {
        PRIVATE_ISLAND(Area::isIsland),
        GARDEN(Area::isGarden),
        HUB(Area::isHub),
        THE_BARN(Area::isTheBarn),
        MUSHROOM_DESERT(Area::isMushroomDesert),
        THE_PARK(Area::isThePark),
        SPIDERS_DEN(Area::isSpidersDen),
        THE_END(Area::isTheEnd),
        CRIMSON_ISLE(Area::isCrimsonIsle),
        GOLD_MINE(Area::isGoldMines),
        DEEP_CAVERNS(Area::isDeepCaverns),
        DWARVEN_MINES(Area::isDwarvenMines),
        CRYSTAL_HOLLOWS(Area::isCrystalHollows),
        WINTER_ISLAND(Area::isWinterIsland),
        DUNGEON_HUB(Area::isDungeonHub),
        RIFT_DIMENSION(Area::isRift),
        CATACOMBS(Area::isCatacombs),
        UNKNOWN_ISLAND(Area::isUnknownIsland);

        public final Function<Area, Boolean> isIsland;

        Islands(Function<Area, Boolean> isIsland) {
            this.isIsland = isIsland;
        }

        public boolean isIsland(Area areas) {
            return this.isIsland.apply(areas);
        }

        public static Islands valueOfOrUnknown(String value) {
            return ExceptionHandler.removeThrowsSilent(() -> Enum.valueOf(Islands.class, value), UNKNOWN_ISLAND);
        }


        public static Islands getIsland(Area area) {
            for (Islands value : Islands.values()) {
                if (value.isIsland(area)) {
                    return value;
                }
            }
            return UNKNOWN_ISLAND;
        }
    }

}
