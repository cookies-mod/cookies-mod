package dev.morazzer.cookiesmod.config.categories.about;

import net.minecraft.client.gui.tooltip.TooltipPositioner;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class AboutTooltipPositioner implements TooltipPositioner {
	public static final AboutTooltipPositioner INSTANCE = new AboutTooltipPositioner();

	@Override
	public Vector2ic getPosition(int screenWidth, int screenHeight, int x, int y, int width, int height) {
		return new Vector2i(x, y).add(12, -12);
	}
}
