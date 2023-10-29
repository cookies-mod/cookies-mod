package dev.morazzer.cookiesmod.config.categories;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import net.minecraft.text.Text;

/**
 * Category that describes all settings that mostly are only important to developers.
 */
public class DevCategory extends Category {

    @Expose
    public final BooleanOption displayRepoOption = new BooleanOption(
            Text.literal("Show repo options"),
            Text.literal("Shows repo options in the /dev command"),
            true
    ).withHiddenKeys("repo", "dev options", "item list");

    @Expose
    public final BooleanOption hideSpam = new BooleanOption(
            Text.literal("Hide spam from console log"),
            Text.literal("Hides all packet spam from log"),
            true
    );

    @Override
    public Text getName() {
        return Text.literal("Dev");
    }

    @Override
    public Text getDescription() {
        return Text.literal("Development settings (Don't change without knowing what it does)");
    }

}
