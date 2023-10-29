package dev.morazzer.cookiesmod.config.categories.mining;

import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import dev.morazzer.cookiesmod.features.mining.FetchurHud;
import dev.morazzer.cookiesmod.features.mining.FuelBarHud;
import net.minecraft.text.Text;

/**
 * Category that describes all settings related to mining.
 */
public class MiningCategory extends Category {

    public final BooleanOption showPuzzlerSolution = new BooleanOption(
            Text.literal("Puzzler solver"),
            Text.literal("Highlights the block you have to break for the puzzler"),
            false
    );

    public BooleanOption currentFetchurItem = new BooleanOption(
            Text.literal("Fetchur solver"),
            Text.literal("Show next fetchur item"),
            false
    ).withHudElement(new FetchurHud());

    public BooleanOption showDrillFuelBar = new BooleanOption(
            Text.literal("Drill fuel bar"),
            Text.literal("Shows your remaining drill fuel in a bar"),
            false
    ).withHudElement(new FuelBarHud());

    public final BooleanOption showDrillFuelPercentage = new BooleanOption(
            Text.literal("Fuel percentage"),
            Text.literal("Show the remaining fuel percentage in the fuel bar"),
            false
    ).withHiddenKeys("drill", "fuel", "bar", "drill fuel bar");

    public final BooleanOption scathaAlert = new BooleanOption(
            Text.literal("Scatha Alert"),
            Text.literal("Show an alert and play a sound when a worm approaches"),
            true
    );

    public final HotmFoldable heartOfTheMountain = new HotmFoldable();

    @Override
    public Text getName() {
        return Text.literal("Mining");
    }

    @Override
    public Text getDescription() {
        return Text.literal("All features related to mining");
    }

}
