package dev.morazzer.cookiesmod.features.mining;

import dev.morazzer.cookiesmod.events.api.ServerSwapEvent;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.CachedValue;
import dev.morazzer.cookiesmod.utils.DevUtils;
import dev.morazzer.cookiesmod.utils.general.ScoreboardUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MiningUtils implements Module {
    private static final Identifier DISABLE_DWARVEN_CHECK = DevUtils.createIdentifier("mining/disable_dwarven_check");
    private static final Identifier DISABLE_HOLLOW_CHECK = DevUtils.createIdentifier("mining/disable_hollow_check");

    private static final List<String> DWARVEN_LOCATIONS = List.of(
            "⏣ Dwarven Mines",
            "⏣ Aristocrat Passage",
            "⏣ Barracks Of Heroes",
            "⏣ C&C Minecarts Co.",
            "⏣ Cliffside Veins",
            "⏣ Divan's Gateway",
            "⏣ Dwarven Tavern",
            "⏣ Dwarven Village",
            "⏣ Far Reserve",
            "⏣ Forge Basin",
            "⏣ Gates To The Mines",
            "⏣ Goblin Burrows",
            "⏣ Grand Library",
            "⏣ Great Ice Wall",
            "⏣ Hanging Court",
            "⏣ Lava Springs",
            "⏣ Miner's Guild",
            "⏣ Palace Bridge",
            "⏣ Rampart's Quarry",
            "⏣ Royal Mines",
            "⏣ Royal Palace",
            "⏣ Royal Quarters",
            "⏣ The Forge",
            "⏣ The Lift",
            "⏣ The Mist",
            "⏣ Upper Mines"
    );

    private static final List<String> HOLLOWS_LOCATIONS = List.of(
            "⏣ Crystal Nucleus",
            "⏣ Dragon's Lair",
            "⏣ Fairy Grotto",
            "⏣ Goblin Holdout",
            "⏣ Goblin Queen's Den",
            "⏣ Jungle Temple",
            "⏣ Jungle",
            "⏣ Khazad-dûm",
            "⏣ Lost Precursor City",
            "⏣ Magma Fields",
            "⏣ Mines of Divan",
            "⏣ Mithril Deposits",
            "⏣ Precursor Remnants"
    );

    private static final CachedValue<Boolean> isInDwarven = new CachedValue<>(() -> DWARVEN_LOCATIONS.contains(
            ScoreboardUtils.getCurrentLocation()) || DevUtils.isEnabled(DISABLE_DWARVEN_CHECK), 5, TimeUnit.SECONDS);

    private static final CachedValue<Boolean> isInHollows = new CachedValue<>(() -> HOLLOWS_LOCATIONS.contains(
            ScoreboardUtils.getCurrentLocation()) || DevUtils.isEnabled(DISABLE_HOLLOW_CHECK), 5, TimeUnit.SECONDS);

    public static boolean isInDwarven() {
        return SkyblockUtils.isCurrentlyInSkyblock() && isInDwarven.getValue();
    }

    public static boolean isInHollows() {
        return SkyblockUtils.isCurrentlyInSkyblock() && isInHollows.getValue();
    }

    @Override
    public void load() {
        ServerSwapEvent.SERVER_SWAP.register(isInDwarven::updateNow);
    }

    @Override
    public String getIdentifierPath() {
        return "mining/utils";
    }
}
