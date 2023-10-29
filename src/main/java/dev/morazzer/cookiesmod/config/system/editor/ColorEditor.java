package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.options.ColorOption;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * Editor to select a color value.
 */
public class ColorEditor extends ConfigOptionEditor<Color, ColorOption> {

    private static final Identifier COLOR_BUTTON = Identifier.of("cookiesmod", "gui/config/color_button.png");
    private static final Identifier COLOR_BAR_OVERLAY = Identifier.of("cookiesmod", "gui/config/color_bar_overlay.png");
    private static final Identifier COLOR_CIRCLE = Identifier.of("cookiesmod", "gui/config/color_circle.png");
    private static final Identifier COLOR_SATURATION = Identifier.of("cookiesmod", "gui/config/color_saturation.png");
    int clickedComponent = -1;
    private boolean renderOverlay = false;
    private int overlayX = 0, overlayY = 0;
    private int overlayWidth = 104;
    private TextFieldWidget textFieldWidget;
    private float wheelAngle = 0;
    private float wheelRadius = 0;
    private float[] hsb;

    public ColorEditor(ColorOption option) {
        super(option);
        if (!option.isAllowAlpha()) overlayWidth -= 15;
        this.recalculateHsb();
    }

    @Override
    public void init() {
        this.textFieldWidget = new TextFieldWidget(getTextRenderer(), 0, -10, 48, 10, Text.literal("#000000"));
        this.renderOverlay = false;
        this.overlayY = 0;
        this.overlayX = 0;
        this.textFieldWidget.setRenderTextProvider((textFieldString, integer) -> {
            String s = StringUtils.leftPad("", 6 - textFieldString.length(), '0');
            return Text.literal("#").append(s).append(Text.literal(textFieldString).formatted(Formatting.WHITE))
                    .formatted(Formatting.WHITE).asOrderedText();
        });
    }

    @Override
    public void render(@NotNull DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
        super.render(drawContext, mouseX, mouseY, tickDelta, optionWidth);

        Color value = this.option.getValue();
        drawContext.setShaderColor(value.getRed() / 255f, value.getGreen() / 255f, value.getBlue() / 255f, 1);
        drawContext.drawTexture(COLOR_BUTTON, optionWidth / 6 - 24, getHeight() - 7 - 14, 0, 0, 48, 16, 48, 16);
        drawContext.setShaderColor(1, 1, 1, 1);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int optionWidth) {
        if (this.renderOverlay
                && (mouseX >= this.overlayX) && (mouseX < this.overlayX + this.overlayWidth)
                && (mouseY >= this.overlayY) && (mouseY < this.overlayY + 89)) {
            if ((mouseX >= (overlayX + 75)) && (mouseX < (overlayX + 85))
                    && (mouseY >= (overlayY + 5)) && (mouseY < (overlayY + 69))) {
                this.clickedComponent = 1;
            }

            this.handleClickOrDragged(mouseX, mouseY, button, optionWidth);
            return true;
        }

        int buttonLeft = optionWidth / 6 - 24;
        int buttonTop = this.getHeight() - 21;
        if ((mouseX > buttonLeft) && (mouseX < (buttonLeft + 48))
                && (mouseY > buttonTop) && (mouseY < (buttonTop + 16))) {
            this.renderOverlay = true;
            this.overlayX = (int) mouseX;
            this.overlayY = (int) mouseY;
            return false;
        }
        this.renderOverlay = false;
        this.overlayY = 0;
        this.overlayX = 0;

        return false;
    }

    @Override
    public void renderOverlay(DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
        if (!renderOverlay) {
            return;
        }

        RenderUtils.renderRectangle(drawContext, overlayX, overlayY, overlayWidth, 89, true);

        Color value = this.option.getValue();
        drawContext.setShaderColor(value.getRed() / 255f, value.getGreen() / 255f, value.getBlue() / 255f, 1);
        drawContext.drawTexture(COLOR_SATURATION, overlayX + 75, overlayY + 5, 0, 0, 10, 64, 10, 64);

        if (this.option.isAllowAlpha()) {
            drawContext.setShaderColor(value.getRed() / 255f, value.getGreen() / 255f, value.getBlue() / 255f, 1);
            //context.drawTexture(COLOR_OPACITY, overlayX + 90, overlayY + 5, 0, 0, 10, 64, 10, 64);
            drawContext.fillGradient(
                    overlayX + 90,
                    overlayY + 6,
                    overlayX + 100,
                    overlayY + 69,
                    0xFFFFFFFF,
                    0x00FFFFFF
            );
            drawContext.fill(overlayX + 91, overlayY + 5, overlayX + 98, overlayY + 7, 0xFFFFFFFF);
        }
        drawContext.setShaderColor(1, 1, 1, 1);

        drawContext.drawTexture(COLOR_BAR_OVERLAY, overlayX + 75, overlayY + 5, 0, 0, 10, 64, 10, 64);
        if (this.option.isAllowAlpha()) {
            drawContext.drawTexture(COLOR_BAR_OVERLAY, overlayX + 90, overlayY + 5, 0, 0, 10, 64, 10, 64);
        }

        float shaderColor = (hsb[2] * 100) / 100F;
        drawContext.setShaderColor(shaderColor, shaderColor, shaderColor, 1);
        drawContext.drawTexture(COLOR_CIRCLE, overlayX + 1, overlayY + 1, 0, 0, 72, 72, 72, 72);
        drawContext.setShaderColor(1, 1, 1, 1);

        RenderUtils.renderCenteredTextWithMaxWidth(
                drawContext,
                Text.literal(String.valueOf(Math.round(hsb[2] * 100)).formatted(Formatting.GRAY)),
                13,
                overlayX + 83,
                overlayY + 79,
                -1,
                true
        );

        if (this.option.isAllowAlpha()) {
            RenderUtils.renderCenteredTextWithMaxWidth(
                    drawContext,
                    Text.literal(String.valueOf((int) (this.option.getValue().getAlpha() / 255F) * 100))
                            .formatted(Formatting.GRAY),
                    13,
                    overlayX + 98,
                    overlayY + 79,
                    -1,
                    true
            );
        }
        if (!this.textFieldWidget.isFocused()) {
            this.textFieldWidget.setText(Integer.toHexString(this.option.getValue().getRed() & 0xFFFFFF).toUpperCase());
        }

        textFieldWidget.setPosition(overlayX + 13, overlayY + 74);
        textFieldWidget.render(drawContext, mouseX, mouseY, tickDelta);

    }

    /**
     * Updates the value in the {@linkplain dev.morazzer.cookiesmod.config.system.editor.ColorEditor#hsb} variable.
     */
    private void recalculateHsb() {
        Color color = this.option.getValue();
        this.hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getRed(), null);
        this.wheelRadius = hsb[1];
        this.wheelAngle = hsb[0] * 360;
    }

    /**
     * Called whenever a mouse button was clicked.
     *
     * @param mouseX      The current x position of the mouse.
     * @param mouseY      The current y position of the mouse.
     * @param button      The button that was clicked.
     * @param optionWidth The width the option is rendered at.
     */
    @SuppressWarnings("EmptyMethod")
    private void handleClickOrDragged(double mouseX, double mouseY, int button, int optionWidth) {}

}
