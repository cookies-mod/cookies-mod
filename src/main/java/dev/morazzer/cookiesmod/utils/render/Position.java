package dev.morazzer.cookiesmod.utils.render;

import net.minecraft.client.MinecraftClient;

/**
 * A position on the screen.
 *
 * @param x         The x coordinate.
 * @param y         The y coordinate.
 * @param centeredX If the origin is the center or the left side.
 * @param centeredY If the origin is the center ot the top.
 */
public record Position(
        double x,
        double y,
        boolean centeredX,
        boolean centeredY
) {

    /**
     * Creates a position with center top left.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Position(double x, double y) {
        this(x, y, false, false);
    }

    /**
     * Gets the x coordinate with overflow protection.
     *
     * @param objectWidth The width of the object.
     * @return The coordinate.
     */
    public int getFixedX(int objectWidth) {
        float width = MinecraftClient.getInstance().getWindow().getScaledWidth();
        if (this.centeredX) {
            return (int) Math.max(0, Math.min(width - objectWidth, width / 2 + x - (double) objectWidth / 2));
        }

        return (int) Math.max(0, Math.min(width - objectWidth, x));
    }

    /**
     * Gets the y coordinate with overflow protection.
     *
     * @param objectHeight The height of the object.
     * @return The coordinate.
     */
    public int getFixedY(int objectHeight) {
        float height = MinecraftClient.getInstance().getWindow().getScaledHeight();
        if (this.centeredY) {
            return (int) Math.max(0, Math.min(height - objectHeight, height / 2 + y - (double) objectHeight / 2));
        }
        return (int) Math.max(0, Math.min((height - objectHeight), y));
    }

    /**
     * Adds a value on top of the position.
     *
     * @param x The x to add.
     * @param y The y to add.
     * @return The new position.
     */
    public Position add(double x, double y) {
        return new Position(this.x + x, this.y + y, this.centeredX, this.centeredY);
    }

}
