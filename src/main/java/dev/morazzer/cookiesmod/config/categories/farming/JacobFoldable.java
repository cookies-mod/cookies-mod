package dev.morazzer.cookiesmod.config.categories.farming;

import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import dev.morazzer.cookiesmod.config.system.options.EnumDropdownOption;
import dev.morazzer.cookiesmod.config.system.options.SliderOption;
import dev.morazzer.cookiesmod.features.farming.jacob.NextJacobContestHud;
import net.minecraft.text.Text;

/**
 * Foldable that contains all settings related to jacobs farming contests.
 */
public class JacobFoldable extends Foldable {

    public final SliderOption<Integer> showContestAmount = SliderOption.integerOption(
        Text.literal("Show contest amount"),
        Text.literal("Configure how many contests you want to see"),
        1
    ).withMin(1).withMax(5).withStep(1);
    public final EnumDropdownOption<TimestampFormat> nextContestTimestampFormat = new EnumDropdownOption<>(
        Text.literal("Timestamp format"),
        Text.literal("How to format the timestamp for the next contest"),
        TimestampFormat.RELATIVE
    ).withSupplier(value -> switch (value) {
        case ABSOLUTE_AM_PM -> Text.literal("1:15pm");
        case ABSOLUTE -> Text.literal("13:15");
        case RELATIVE -> Text.literal("33min 21sec");
    });
    public final BooleanOption onlyShowOnGarden = new BooleanOption(
        Text.literal("Always show contests"),
        Text.literal("Configures if contests are only shown on garden or all over skyblock"),
        false
    );
    public final BooleanOption highlightUnclaimedContests = new BooleanOption(
        Text.literal("Highlight unclaimed contests"),
        Text.literal("Highlights unclaimed jacob contests in the Rewards screen"),
        false
    );
    public BooleanOption showNextContest = new BooleanOption(
        Text.literal("Show next contest"),
        Text.literal("Shows when and what jacob contests are"),
        false
    ).withHudElement(new NextJacobContestHud());

    @Override
    public Text getName() {
        return Text.literal("Jacob's Farming Contests");
    }

    /**
     * Timestamp formats used in the HUD.
     */
    public enum TimestampFormat {
        RELATIVE,
        ABSOLUTE_AM_PM,
        ABSOLUTE
    }

}
