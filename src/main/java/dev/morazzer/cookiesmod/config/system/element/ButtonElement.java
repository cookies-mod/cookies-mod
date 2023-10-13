package dev.morazzer.cookiesmod.config.system.element;

import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ButtonElement {

    private static final Identifier BUTTON = Identifier.of("cookiesmod", "gui/config/button.png");
    private final Runnable runnable;
    private final Text text;

    public ButtonElement(Runnable runnable, Text text) {
        this.runnable = runnable;
        this.text = text;
    }

    public void render(DrawContext drawContext) {
        drawContext.drawTexture(BUTTON, 0, 0, 0, 0, 48, 16, 48, 16);
        RenderUtils.renderCenteredTextWithMaxWidth(drawContext, text, 48, 24, 8, ~0, true);
    }

    public boolean mouseClicked(double mouseX, double mouseY) {
        if ((mouseX > 0) && (mouseX < (48))
                && (mouseY > 0) && (mouseY < (16))) {
            this.runnable.run();
            return true;
        }
        return false;
    }

}
