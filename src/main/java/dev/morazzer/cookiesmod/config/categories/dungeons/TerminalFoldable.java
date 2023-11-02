package dev.morazzer.cookiesmod.config.categories.dungeons;

import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import net.minecraft.text.Text;

/**
 * Foldable that contains settings for the terminals.
 */
public class TerminalFoldable extends Foldable {

    public final BooleanOption clickAllStartingWith = new BooleanOption(
        Text.literal("Click all starting"),
        Text.literal("Enable the click all items starting with solver"),
        false
    );

    public final BooleanOption clickAllColors = new BooleanOption(
        Text.literal("Click all colors"),
        Text.literal("Enable the click all items with the color solver"),
        false
    );

    @Override
    public Text getName() {
        return Text.literal("Terminals");
    }

}
