package dev.morazzer.cookiesmod.config.categories.mining;

import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HotmFoldable extends Foldable {

    public BooleanOption showTotalPowder = new BooleanOption(
            Text.literal("Show total powder"),
            Text.literal("Show total powder cost for a perk"),
            false
    ).withHiddenKeys("gemstone", "mithril");

    public BooleanOption showNextTenPowder = new BooleanOption(
            Text.literal("Show next 10 cost"),
            Text.literal("Show cost for next 10 level of a perk"),
            false
    ).withHiddenKeys("gemstone", "mithril");

    public BooleanOption showLevelInCount = new BooleanOption(
            Text.literal("Show perk level as count"),
            Text.literal("Show the current perk level as count"),
            false
    );

    @Override
    public Text getName() {
        return Text.literal("Heart of the Mountain").formatted(Formatting.DARK_PURPLE);
    }
}
