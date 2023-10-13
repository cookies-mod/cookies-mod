package dev.morazzer.cookiesmod.config.categories.waypoints;

import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class WaypointEditor extends ConfigOptionEditor<Object, WaypointOption> {
    LocationUtils.Islands islands = LocationUtils.Islands.HUB;

    public WaypointEditor(WaypointOption option) {
        super(option);
    }

    @Override
    public int getHeight(int optionWidth) {
        return 1000;
    }

    @Override
    public void init() {
        if (SkyblockUtils.isCurrentlyInSkyblock()) {
            this.islands = LocationUtils.getCurrentIsland();
        } else {
            this.islands = LocationUtils.Islands.HUB;
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
        RenderUtils.renderRectangle(drawContext, 0, 0, optionWidth - 2, getHeight(optionWidth) - 2, true);

        RenderUtils.renderTextCenteredScaled(drawContext, Text.literal("Waypoints"), 2, optionWidth / 2, 10, -1);


    }
}
