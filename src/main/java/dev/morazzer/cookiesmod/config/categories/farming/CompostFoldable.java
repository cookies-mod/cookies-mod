package dev.morazzer.cookiesmod.config.categories.farming;

import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import dev.morazzer.cookiesmod.config.system.options.EnumDropdownOption;
import dev.morazzer.cookiesmod.config.system.options.SliderOption;
import net.minecraft.text.Text;

/**
 * Foldable that contains all settings related to the composter.
 */
public class CompostFoldable extends Foldable {

    public final BooleanOption muteComposter = new BooleanOption(
            Text.literal("Mute Composter"),
            Text.literal("Mutes all composter sounds"),
            false
    );

    public final BooleanOption showWarnings = new BooleanOption(
            Text.literal("Show compost warnings"),
            Text.literal("Whether you should get notified for low fuel/organic matter"),
            false
    );

    public final SliderOption<Integer> organicMatterAmount = SliderOption.integerOption(
                    Text.literal("Organic Matter amount"),
                    Text.literal("The amount of organic matter to warn at"),
                    30000
            )
            .withMax(50000).withMin(0).withStep(1000);

    public final SliderOption<Integer> fuelWarningAmount = SliderOption.integerOption(
            Text.literal("Fuel amount"),
            Text.literal("The amount of fuel to warn at"),
            5000
    ).withMax(5000).withMin(3000).withStep(100);

    public final BooleanOption showUpgradeCost = new BooleanOption(
            Text.literal("Show upgrades"),
            Text.literal("Shows a breakdown of all remaining upgrade costs"),
            false
    );

    public final EnumDropdownOption<ItemSortMode> itemSort = new EnumDropdownOption<>(
            Text.literal("Upgrade item sort"),
            Text.literal("Whether the items should be from highest to lowest or the other way around"),
            ItemSortMode.DOWN
    )
            .withSupplier(value -> switch (value) {
                case UP -> Text.literal("Ascending");
                case DOWN -> Text.literal("Descending");
            });

    @Override
    public Text getName() {
        return Text.literal("Compost");
    }

    public enum ItemSortMode {
        UP,
        DOWN
    }

}
