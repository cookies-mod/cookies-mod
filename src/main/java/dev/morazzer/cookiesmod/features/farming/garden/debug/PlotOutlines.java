package dev.morazzer.cookiesmod.features.farming.garden.debug;

import dev.morazzer.cookiesmod.features.farming.garden.Garden;
import dev.morazzer.cookiesmod.features.farming.garden.Plot;
import dev.morazzer.cookiesmod.utils.DevUtils;
import java.awt.Color;
import java.util.Optional;
import me.x150.renderer.render.Renderer3d;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

/**
 * Debug to show the calculated plot outlines on the garden.
 */
public class PlotOutlines {

    private static final Identifier RENDER_DEBUG_PLOT_OUTLINES = DevUtils.createIdentifier(
        "garden/plots/show_boundaries");
    private static final Identifier RENDER_DEBUG_PLOT_BORDERS = DevUtils.createIdentifier("garden/plots/show_borders");
    private static final Vec3d plotMiddle = new Vec3d(48, 0, 48);

    /**
     * Initializes/registers the debug to be enabled.
     */
    public static void initializePlotOutlinesDebug() {
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            renderPlotOverlays(context);
            renderCurrentPlotWalls(context);
        });
    }

    /**
     * Renders the calculated plot walls on the garden.
     *
     * @param context The current draw context.
     */
    private static void renderCurrentPlotWalls(WorldRenderContext context) {
        if (!DevUtils.isEnabled(RENDER_DEBUG_PLOT_BORDERS)) {
            return;
        }

        if (!Garden.isOnGarden()) {
            return;
        }

        Plot plot = Optional
            .ofNullable(MinecraftClient.getInstance().player)
            .map(Entity::getPos)
            .map(Plot::getPlotFromRealCoordinate)
            .orElse(Plot.NONE);

        if (!plot.isValidPlot()) {
            return;
        }

        Optional<Vec3d> vec3d = Optional
            .ofNullable(MinecraftClient.getInstance().player)
            .map(Entity::getPos)
            .map(plot::getPlotCenter);
        if (vec3d.isEmpty()) {
            return;
        }


        Vec3d topLeftPlot = vec3d.get();
        Renderer3d.renderFilled(context.matrixStack(), Color.BLACK, topLeftPlot.add(0, 200, 0), new Vec3d(1, 1, 1));
        Renderer3d.renderFilled(
            context.matrixStack(),
            Color.RED,
            topLeftPlot.add(48, 71.01, 48),
            plotMiddle.multiply(-2).add(0, 0, 0)
        );
    }

    /**
     * Renders an overlay on all plots, with the color based on their position.
     *
     * @param context The current draw context.
     */
    private static void renderPlotOverlays(WorldRenderContext context) {
        if (!DevUtils.isEnabled(RENDER_DEBUG_PLOT_OUTLINES)) {
            return;
        }

        if (!Garden.isOnGarden()) {
            return;
        }

        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                Color color;
                Plot plot = Plot.getPlotFromPlotCoordinate(x, y);

                if (plot.isOuterCircle() && plot.isCorner()) {
                    color = Color.RED;
                } else if (plot.isInnerCircle() && plot.isCorner()) {
                    color = Color.YELLOW;
                } else if (plot.isInnerCircle() && plot.isEdge()) {
                    color = Color.GREEN;
                } else if (plot.isBarn()) {
                    color = Color.BLUE;
                } else if (plot.isOuterCircle() && plot.isEdge()) {
                    color = Color.ORANGE;
                } else {
                    color = Color.BLACK;
                }

                color = Renderer3d.modifyColor(color, -1, -1, -1, 125);
                Vec3d movedPlotOrigin = plotMiddle.multiply(x * 2, 1, y * 2);
                Vec3d topLeftPlot = movedPlotOrigin.add(plotMiddle);

                Renderer3d.renderFilled(context.matrixStack(), color, topLeftPlot, new Vec3d(-96, 0, -96));
            }
        }
    }

}
