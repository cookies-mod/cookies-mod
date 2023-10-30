package dev.morazzer.cookiesmod.config.categories;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.options.ButtonOption;
import dev.morazzer.cookiesmod.features.hud.HudEditor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

/**
 * Category that describes all GUI related settings.
 */
public class GuiCategory extends Category {

    @Expose
    public ButtonOption buttonOption = new ButtonOption(
            Text.literal("Edit Locations"),
            Text.literal("Lets you move all HUD elements"),
            () -> MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(new HudEditor())),
            Text.literal("Edit")
    );

    @Override
    public Text getName() {
        return Text.literal("GUI Settings");
    }

    @Override
    public Text getDescription() {
        return Text.literal("Edit settings related to the HUD/GUI");
    }

}
