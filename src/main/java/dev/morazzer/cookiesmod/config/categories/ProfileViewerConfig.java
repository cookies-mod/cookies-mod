package dev.morazzer.cookiesmod.config.categories;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import net.minecraft.text.Text;

/**
 * Category that describes all settings related to the profile viewer.
 */
public class ProfileViewerConfig extends Category {

    @Expose
    public BooleanOption keepLastProfileOpen = new BooleanOption(
            Text.of("Keep last search"),
            Text.of("Keeps the last viewed profile open when you open the profile viewer again"),
            false
    );

    @Override
    public Text getName() {
        return Text.literal("Profile Viewer");
    }

    @Override
    public Text getDescription() {
        return Text.literal("Settings related to the profile viewer");
    }

}
