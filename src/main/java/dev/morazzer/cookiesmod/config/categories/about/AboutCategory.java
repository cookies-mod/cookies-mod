package dev.morazzer.cookiesmod.config.categories.about;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Category;
import net.minecraft.text.Text;

/**
 * The config category that will show the {@linkplain dev.morazzer.cookiesmod.config.categories.about.credits.Credits}.
 */
public class AboutCategory extends Category {

    @Expose
    public AboutOption aboutOption = new AboutOption();

    @Override
    public Text getName() {
        return Text.literal("About/Credits");
    }

    @Override
    public Text getDescription() {
        return Text.literal("Information about the mod and credits");
    }

}
