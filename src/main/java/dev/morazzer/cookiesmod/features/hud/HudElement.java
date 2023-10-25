package dev.morazzer.cookiesmod.features.hud;

import dev.morazzer.cookiesmod.utils.DevUtils;
import dev.morazzer.cookiesmod.utils.TimeUtils;
import dev.morazzer.cookiesmod.utils.render.Position;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

/**
 * A hud element that can be rendered on the screen.
 * For this to be rendered, you have to register it with an option in the config.
 */
@Getter
public abstract class HudElement {

    private static final Identifier SHOW_RENDER_TIMINGS = DevUtils.createIdentifier("hud/show_timings");
    private static final Identifier SHOW_AVERAGED_TIMINGS = DevUtils.createIdentifier("hud/show_averaged_timings");

    private static final Identifier HUD_NAMESPACE = new Identifier("cookiesmod", "hud/");
    private Position position;
    private boolean enabled;

    /**
     * Creates a new hud element that will not be registered.
     *
     * @param position The position where to render the element.
     */
    public HudElement(Position position) {
        this.position = position;
    }

    /**
     * Called once the config has finished loading, put all the logic that depends on it here.
     */
    public void init() {}

    /**
     * @return The width.
     */
    public abstract int getWidth();

    /**
     * @return The height.
     */
    public abstract int getHeight();

    /**
     * @return The identifier.
     */
    public abstract String getIdentifierPath();

    /**
     * Whether the element should be rendered. Use this instead of if checks in the render call.
     *
     * @return Whether the element should render.
     */
    public abstract boolean shouldRender();

    /**
     * Gets the x coordinate of the element.
     *
     * @return The x coordinate.
     */
    public final int getX() {
        return this.position.getFixedX(this.getWidth());
    }

    /**
     * Gets the y coordinate of the element.
     *
     * @return The y coordinate.
     */
    public final int getY() {
        return this.position.getFixedY(this.getHeight());
    }

    /**
     * Gets the {@linkplain dev.morazzer.cookiesmod.features.hud.HudElement#getIdentifierPath()} as {@linkplain net.minecraft.util.Identifier} with a namespace.
     *
     * @return The identifier.
     */
    public Identifier getIdentifier() {
        return HUD_NAMESPACE.withSuffixedPath(getIdentifierPath());
    }

    /**
     * Renders the element with the {@linkplain dev.morazzer.cookiesmod.features.hud.HudElement#shouldRender()} check.
     *
     * @param drawContext The current draw context.
     * @param tickDelta   The difference in time between the last tick and now.
     */
    public void renderWithTests(DrawContext drawContext, float tickDelta) {
        MinecraftClient.getInstance().getProfiler().push(this.identifier.toString());
        if (!(this.enabled && shouldRender())) {
            MinecraftClient.getInstance().getProfiler().pop();
            return;
        }
        render(drawContext, tickDelta);
        MinecraftClient.getInstance().getProfiler().pop();
    }

    private final long[] lastTimings = new long[10];
    private TimeUtils.Timer timer = null;

    /**
     * Renders the element without extra checks.
     *
     * @param drawContext The current draw context.
     * @param tickDelta   The difference in time between the last tick and now.
     */
    public void render(DrawContext drawContext, float tickDelta) {
        boolean showTimings = DevUtils.isEnabled(SHOW_RENDER_TIMINGS) || DevUtils.isEnabled(SHOW_AVERAGED_TIMINGS);
        if (showTimings && timer == null) {
            timer = new TimeUtils.Timer();
        }
        if (showTimings) {
            timer.start();
        }

        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(position.getFixedX(getWidth()), position.getFixedY(getHeight()), 1);
        renderOverlay(drawContext, tickDelta);

        if (showTimings) {
            timer.stop();
            if (DevUtils.isEnabled(SHOW_RENDER_TIMINGS)) {
                drawContext.drawText(
                        MinecraftClient.getInstance().textRenderer,
                        timer.elapsed(),
                        0,
                        getHeight() + 10,
                        -1,
                        true
                );
            }
        }

        drawContext.getMatrices().pop();
    }

    /**
     * If the {@linkplain dev.morazzer.cookiesmod.features.hud.HudEditor} is currently open.
     *
     * @return If the hud is being edited.
     */
    public boolean isCurrentlyEditing() {
        return MinecraftClient.getInstance().currentScreen != null && MinecraftClient.getInstance().currentScreen instanceof HudEditor;
    }

    /**
     * Toggles the hud element on or off. This method should only be used by the config option.
     *
     * @param toggle The new value.
     */
    public void toggle(boolean toggle) {
        this.enabled = toggle;
    }

    /**
     * @param position The new position.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Renders the overlay for the element.
     *
     * @param drawContext The current draw context.
     * @param tickDelta   The difference in time between the last tick and now.
     */
    protected abstract void renderOverlay(DrawContext drawContext, float tickDelta);

}
