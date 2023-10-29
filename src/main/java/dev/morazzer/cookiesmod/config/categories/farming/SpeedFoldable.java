package dev.morazzer.cookiesmod.config.categories.farming;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import dev.morazzer.cookiesmod.config.system.options.EnumDropdownOption;
import dev.morazzer.cookiesmod.features.farming.garden.speed.SpeedPresets;
import dev.morazzer.cookiesmod.features.farming.garden.speed.Speeds;
import net.minecraft.text.Text;

/**
 * Foldable that contains options to change the speed for the rancher boots overlay.
 */
public class SpeedFoldable extends Foldable {

    @Expose
    public final BooleanOption showSpeeds = new BooleanOption(
            Text.literal("Show speeds"),
            Text.literal("Shows the speeds in the rancher boots for specific crops"),
            true
    );

    @Expose
    public final BooleanOption showNames = new BooleanOption(
            Text.literal("Show crop names"),
            Text.literal("Shows the crop names next to the icons"),
            true
    );

    @Expose
    public final BooleanOption mergeEqualSpeeds = new BooleanOption(
            Text.literal("Merge Equal Speeds"),
            Text.literal("If two crops with the same speed should be merged into one line"),
            true
    );

    @Expose
    public final EnumDropdownOption<SpeedPresets> speedPresets = new EnumDropdownOption<>(
            Text.literal("Use speed preset"),
            Text.literal("Change the preset that is used for speeds"),
            SpeedPresets.NORMAL
    );

    @Expose
    public final Speeds speeds = new Speeds();

    @Override
    public Text getName() {
        return Text.literal("Speeds");
    }

}