package dev.morazzer.cookiesmod.config.categories.dungeons;

import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import net.minecraft.text.Text;

public class TerminalFoldable extends Foldable {

    public BooleanOption clickAllStartingWith = new BooleanOption(
            Text.literal("Click all starting"),
            Text.literal("Enable the click all items starting with solver"),
            false
    );

    public BooleanOption clickAllColors = new BooleanOption(
            Text.literal("Click all colors"),
            Text.literal("Enable the click all items with the color solver"),
            false
    );

    @Override
    public Text getName() {
        return Text.literal("Terminals");
    }
}
