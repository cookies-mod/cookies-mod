package dev.morazzer.cookiesmod.features.hud;

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

    private static final Identifier HUD_NAMESPACE = new Identifier("cookiesmod", "hud/");
    private Position position;
    private boolean enabled;

    /**
     * Create a new hud element that will not be registered.
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
     * Get the width of a hud element.
     *
     * @return The width.
     */
    public abstract int getWidth();

    /**
     * Get the height of a hud element.
     *
     * @return The height.
     */
    public abstract int getHeight();

    /**
     * Get the identifier of the element.
     *
     * @return The identifier.
     */
    public abstract String getIdentifierPath();

    /**
     * If the element should be rendered or not, use this instead of if checks in the render call.
     *
     * @return If the element should render.
     */
    public abstract boolean shouldRender();

    /**
     * Get the x coordinate of the element.
     *
     * @return The x coordinate.
     */
    public final int getX() {
        return this.position.getFixedX(this.getWidth());
    }

    /**
     * Get the y coordinate of the element.
     *
     * @return The y coordinate.
     */
    public final int getY() {
        return this.position.getFixedY(this.getHeight());
    }

    /**
     * Get the {@linkplain dev.morazzer.cookiesmod.features.hud.HudElement#getIdentifierPath()} as {@linkplain net.minecraft.util.Identifier} with a namespace.
     *
     * @return The identifier.
     */
    public Identifier getIdentifier() {
        return HUD_NAMESPACE.withSuffixedPath(getIdentifierPath());
    }

    /**
     * Render the element with the {@linkplain dev.morazzer.cookiesmod.features.hud.HudElement#shouldRender()} check.
     *
     * @param drawContext The current draw context.
     * @param tickDelta   The difference in time between the last tick and now.
     */
    public void renderWithTests(DrawContext drawContext, float tickDelta) {
        if (!(this.enabled && shouldRender())) {
            return;
        }
        render(drawContext, tickDelta);
    }

    /**
     * Render the element without extra checks.
     *
     * @param drawContext The current draw context.
     * @param tickDelta   The difference in time between the last tick and now.
     */
    public void render(DrawContext drawContext, float tickDelta) {
        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(position.getFixedX(getWidth()), position.getFixedY(getHeight()), 1);
        renderOverlay(drawContext, tickDelta);
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
     * Toggle the hud element on or off. This method should only be used by the config option.
     *
     * @param toggle The new value.
     */
    public void toggle(boolean toggle) {
        this.enabled = toggle;
    }

    /**
     * Set a new position for the element.
     *
     * @param position The new position.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Render the overlay for the element.
     *
     * @param drawContext The current draw context.
     * @param tickDelta   The difference in time between the last tick and now.
     */
    protected abstract void renderOverlay(DrawContext drawContext, float tickDelta);

}
