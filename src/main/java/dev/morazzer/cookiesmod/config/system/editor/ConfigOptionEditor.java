package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Generic option editor.
 *
 * @param <T> Type of the value.
 * @param <O> Type of the option.
 */
public abstract class ConfigOptionEditor<T, O extends Option<T, O>> {

    protected final O option;
    private boolean isDragging;

    /**
     * Creates a new option editor.
     *
     * @param option The option the editor belongs to.
     */
    public ConfigOptionEditor(@NotNull O option) {
        this.option = option;
    }

    /**
     * Gets the height of the option.
     *
     * @param optionWidth The width of the option.
     * @return The height of the option.
     */
    @Contract(pure = true)
    public int getHeight(int optionWidth) {
        return getHeight();
    }

    /**
     * Gets the height of the option.
     *
     * @return The height of the option.
     */
    @Contract(pure = true)
    public int getHeight() {
        return 45;
    }

    /**
     * Called whenever the screen is resized or the config menu is opened.
     */
    public void init() {
    }

    /**
     * Renders the editor.
     *
     * @param drawContext The current draw context.
     * @param mouseX      The current x position of the mouse.
     * @param mouseY      The current y position of the mouse.
     * @param tickDelta   The time difference between the last and the current tick.
     * @param optionWidth The width the option has to be rendered at.
     */
    public void render(
        @NotNull DrawContext drawContext,
        int mouseX,
        int mouseY,
        float tickDelta,
        int optionWidth
    ) {
        RenderUtils.renderRectangle(
            drawContext,
            0,
            0,
            optionWidth - 2,
            getHeight(optionWidth) - 2,
            true
        );

        RenderUtils.renderCenteredTextWithMaxWidth(
            drawContext,
            this.option.getName(),
            optionWidth / 3 - 10,
            optionWidth / 6 + 2,
            13,
            -1,
            true
        );

        int lineCount = this.getTextRenderer().wrapLines(
            this.option.getDescription(),
            optionWidth * 2 / 3 - 10
        ).size();
        if (lineCount == 0) {
            return;
        }


        drawContext.drawTextWrapped(
            this.getTextRenderer(),
            this.option.getDescription(),
            5 + optionWidth / 3,
            getHeight() / 2 - (lineCount * 9) / 2,
            (optionWidth * 2 / 3 - 10),
            ~0
        );
    }

    /**
     * Checks whether the option matches a user-defined search parameter.
     *
     * @param search The search parameter.
     * @return Whether the option matches the parameter.
     */
    public boolean doesMatchSearch(@NotNull @NotBlank String search) {
        return this.option.getName().getString().contains(search) || this.option.getDescription().getString()
            .contains(search) || this.option.getTags()
            .stream()
            .anyMatch(
                key -> key.contains(
                    search));
    }

    /**
     * Called whenever a key is pressed.
     *
     * @param keyCode   The key code.
     * @param scanCode  The scan code.
     * @param modifiers The modifiers for the key.
     * @return If the action was consumed.
     */
    public boolean keyPressed(
        final int keyCode,
        final int scanCode,
        final int modifiers
    ) {
        return false;
    }

    /**
     * Called whenever a mouse button was clicked.
     *
     * @param mouseX      The current x position of the mouse.
     * @param mouseY      The current y position of the mouse.
     * @param button      The button that was clicked.
     * @param optionWidth The width the option is rendered at.
     * @return If the action was consumed.
     */
    public boolean mouseClicked(
        double mouseX,
        double mouseY,
        int button,
        int optionWidth
    ) {
        return false;
    }

    /**
     * Called whenever a mouse button was released.
     *
     * @param mouseX The current x position of the mouse.
     * @param mouseY The current y position of the mouse.
     * @param button The button that was clicked.
     * @return If the action was consumed.
     */
    public boolean mouseReleased(
        double mouseX,
        double mouseY,
        int button
    ) {
        return false;
    }

    /**
     * Called whenever the mouse is dragged while having a button clicked.
     *
     * @param mouseX      The current x position of the mouse.
     * @param mouseY      The current y position of the mouse.
     * @param button      The button that is clicked.
     * @param deltaX      The difference on the x-axis between the last call and now.
     * @param deltaY      The difference on the y-axis between the last call and now.
     * @param optionWidth The width the option is rendered at.
     * @return If the action was consumed.
     */
    public boolean mouseDragged(
        double mouseX,
        double mouseY,
        int button,
        double deltaX,
        double deltaY,
        int optionWidth
    ) {
        return false;
    }

    /**
     * Whether there is currently an element that is being dragged in the editor.
     *
     * @return Whether there is a dragged element.
     */
    public boolean isDragging() {
        return isDragging;
    }

    /**
     * Change the current dragging state for the editor.
     *
     * @param dragging If there is something being dragged.
     */
    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    /**
     * Called whenever the mouse wheel is scrolled.
     *
     * @param mouseX           The current x position of the mouse.
     * @param mouseY           The current y position of the mouse.
     * @param horizontalAmount The amount on the x-axis that has been scrolled.
     * @param verticalAmount   The amount on the y-axis that has been scrolled.
     */
    @SuppressWarnings("EmptyMethod")
    public void mouseScrolled(
        double mouseX,
        double mouseY,
        double horizontalAmount,
        double verticalAmount
    ) {
    }

    /**
     * Called whenever a key was released.
     *
     * @param keyCode   The key code.
     * @param scanCode  The scan code.
     * @param modifiers The modifiers for the key.
     */
    @SuppressWarnings("EmptyMethod")
    public void keyReleased(
        int keyCode,
        int scanCode,
        int modifiers
    ) {
    }

    /**
     * Called whenever a character was typed.
     *
     * @param character The character that was typed.
     * @param modifiers The modifiers that apply to the character.
     */
    public void charTyped(
        char character,
        int modifiers
    ) {
    }

    /**
     * Called to render the overlay for the current editor.
     *
     * @param drawContext The current draw context.
     * @param mouseX      The current x position of the mouse.
     * @param mouseY      The current y position of the mouse.
     * @param tickDelta   The time difference between the last and the current tick.
     * @param optionWidth The width the option has to be rendered at.
     */
    public void renderOverlay(
        DrawContext drawContext,
        int mouseX,
        int mouseY,
        float tickDelta,
        int optionWidth
    ) {
    }

    /**
     * Gets the {@linkplain net.minecraft.client.font.TextRenderer} instance.
     *
     * @return The {@linkplain net.minecraft.client.font.TextRenderer}.
     */
    @Contract(pure = true)
    protected TextRenderer getTextRenderer() {
        return MinecraftClient.getInstance().textRenderer;
    }

}
