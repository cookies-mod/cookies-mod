package dev.morazzer.cookiesmod.config.categories.about;

import net.minecraft.client.gui.tooltip.TooltipPositioner;
import org.joml.Vector2i;
import org.joml.Vector2ic;

/**
 * A custom tooltip positioner that has no overflow protection to suppress unwanted protection.
 */
public class AboutTooltipPositioner implements TooltipPositioner {

    public static final AboutTooltipPositioner INSTANCE = new AboutTooltipPositioner();

    /**
     * Modifies the position of a tooltip to be slightly offset from the cursor.
     *
     * @param screenWidth  The width of the window.
     * @param screenHeight The height of the window.
     * @param x            The current x coordinate.
     * @param y            The current y coordinate.
     * @param width        The width of the tooltip.
     * @param height       The height of the tooltip.
     * @return The modified coordinates.
     */
    @Override
    public Vector2ic getPosition(int screenWidth, int screenHeight, int x, int y, int width, int height) {
        return new Vector2i(x, y).add(12, -12);
    }

}
