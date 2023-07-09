package dev.morazzer.morassbmod.gui.screen.element;

import dev.morazzer.morassbmod.utils.DevUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.Optional;

public class ClickableTextWidget extends TextWidget {

    public static final Identifier SHOW_CLICKABLE_TEXT_BOUNDING_BOX = new Identifier("morassbmod", "dev/clickable_text_bounding_box");

    private final Runnable onClick;
    boolean debug;

    public ClickableTextWidget(Text message, TextRenderer textRenderer, Runnable onClick, int width, int height) {
        super(message, textRenderer);
        this.onClick = onClick;
        this.active = true;
        this.visible = true;
        this.debug = DevUtils.isEnabled(SHOW_CLICKABLE_TEXT_BOUNDING_BOX);
        this.width = width;
        this.height = height;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        onClick.run();
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        if (debug) {
            context.drawBorder(this.getX(), this.getY(), this.width, this.height, 0xFFFFFFFF);
        }
        Text text = this.getMessage();
        TextRenderer textRenderer = this.getTextRenderer();
        Objects.requireNonNull(textRenderer);
        context.drawTextWithShadow(textRenderer,
                text,
                this.getX() + this.width / 2 - (textRenderer.getWidth(text) / 2),
                this.getY() + this.height / 2 - 4,
                Optional.ofNullable(text.getStyle().getColor()).map(TextColor::getRgb).orElse(0xFFFFFFFF)
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        System.out.println(mouseX + " - " + mouseY);
        System.out.println(this.getX() + " - " + this.getY());
        System.out.println(this.getX() + this.width + " - " +(this.getY() + this.height));
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
