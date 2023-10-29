package dev.morazzer.cookiesmod.features.farming.garden;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

/**
 * Utility functions to work with plots.
 */
public enum Plot {

    BARN,
    INNER_EDGE_PLOT,
    INNER_CORNER_PLOT,
    OUTER_EDGE_PLOT,
    OUTER_CORNER_PLOT,
    NONE;

    /**
     * Get the plot the player currently is on.
     *
     * @return The plot.
     */
    public static Plot getCurrentPlot() {
        return Optional
                .ofNullable(MinecraftClient.getInstance().player)
                .map(Entity::getPos)
                .map(Plot::getPlotFromRealCoordinate)
                .orElse(Plot.NONE);
    }

    /**
     * Get the plot based on the coordinates.
     *
     * @param position The coordinates.
     * @return The plot.
     */
    public static Plot getPlotFromRealCoordinate(Vec3d position) {
        return getPlotFromPlotCoordinate(
                (int) (changeToPlotCenter(Math.abs(position.x)) + 48) / 96,
                (int) (changeToPlotCenter(Math.abs(position.z)) + 48) / 96
        );
    }

    /**
     * Get the plot based on plot coordinates.
     *
     * @param x The x coordinate of the plot.
     * @param z The y coordinate of the plot.
     * @return The plot.
     */
    public static Plot getPlotFromPlotCoordinate(int x, int z) {
        return getPlotFromAbsolutePlotCoordinate(Math.abs(x), Math.abs(z));
    }

    /**
     * Get the plot based on the absolute plot coordinates.
     *
     * @param absoluteX The absolute x coordinate.
     * @param absoluteY The absolute y coordinate.
     * @return The plot.
     */
    private static Plot getPlotFromAbsolutePlotCoordinate(int absoluteX, int absoluteY) {
        if (absoluteX == 2 && absoluteY == 2) {
            return OUTER_CORNER_PLOT;
        } else if (absoluteX == 1 && absoluteY == 1) {
            return INNER_CORNER_PLOT;
        } else if (Math.min(absoluteX, absoluteY) == 0 && Math.max(absoluteX, absoluteY) == 1) {
            return INNER_EDGE_PLOT;
        } else if (absoluteX + absoluteY == 0) {
            return BARN;
        } else if (absoluteX <= 2 && absoluteY <= 2) {
            return OUTER_EDGE_PLOT;
        } else {
            return NONE;
        }
    }

    /**
     * Modify the plot coordinate to be in the center of the plot.
     *
     * @param coordinate The coordinate.
     * @return The coordinate moved to the center.
     */
    private static double changeToPlotCenter(double coordinate) {
        return coordinate - coordinate % 48;
    }

    /**
     * Change the plot coordinate to be on the corner.
     *
     * @param coordinate The coordinate.
     * @return The coordinate moved to the center.
     */
    private static double changeToPlotCorner(double coordinate) {
        return coordinate - Math.abs(coordinate % 96);
    }

    /**
     * Get the center coordinate of the plot.
     *
     * @param position The coordinate.
     * @return The center coordinate.
     */
    public Vec3d getPlotCenter(Vec3d position) {
        return new Vec3d(changeToPlotCorner(position.x + 240) - 192, 0, changeToPlotCorner(position.z + 240) - 192);
    }

    /**
     * Check if the plot is a valid plot, or if it's out of bounds.
     *
     * @return If it's valid.
     */
    public boolean isValidPlot() {
        return ordinal() != 5;
    }

    /**
     * If the plot is in the inner circle.
     *
     * @return If the plot is in the inner circle.
     */
    public boolean isInnerCircle() {
        return ordinal() == 1 || ordinal() == 2;
    }

    /**
     * If the plot is in the outer circle.
     *
     * @return If the plot is in the outer circle.
     */
    public boolean isOuterCircle() {
        return ordinal() == 3 || ordinal() == 4;
    }

    /**
     * If the plot is the barn or not.
     *
     * @return If the plot is the barn.
     */
    public boolean isBarn() {
        return ordinal() == 0;
    }

    /**
     * If the plot is on the edge.
     *
     * @return The plot is on the edge.
     */
    public boolean isEdge() {
        return ordinal() == 1 || ordinal() == 3;
    }

    /**
     * If the plot is a corner plot.
     *
     * @return If the plot is a corner plot.
     */
    public boolean isCorner() {
        return ordinal() == 2 || ordinal() == 4;
    }
}