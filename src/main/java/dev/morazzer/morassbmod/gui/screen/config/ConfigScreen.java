package dev.morazzer.morassbmod.gui.screen.config;

import dev.morazzer.morassbmod.utils.DevUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ConfigScreen extends Screen {

    public static final Identifier DEBUG_CONFIG_SCREEN = new Identifier("morassbmod", "dev/config_screen_debug");

    private final boolean debug;

    int halfWidth;
    int quarterWidth;
    int eighthWidth;
    int halfHeight;
    int quarterHeight;
    int eighthHeight;

    ConfigScreenCategoryListWidget configScreenCategoryListWidget = null;

    public ConfigScreen() {
        // The parameter is the title of the screen,
        // which will be narrated when you enter the screen.
        super(Text.literal("Msbm config"));
        this.debug = DevUtils.isEnabled(DEBUG_CONFIG_SCREEN);
    }

    @Override
    public void renderBackground(DrawContext context) {
        int bottomRightX = eighthWidth * 7;
        int bottomRightY = eighthHeight * 7;

        context.fill(eighthWidth, eighthHeight, bottomRightX, bottomRightY, 0xCC2F2F2F);

        context.fill(eighthWidth, eighthHeight, eighthWidth * 2, this.height - eighthHeight, 0xFF4F4F4F);
        context.fill(eighthWidth * 2, eighthHeight, bottomRightX, (int) Math.nextUp(eighthHeight + eighthHeight * 0.33), 0xFF4F4F4F);
        context.drawHorizontalLine(eighthWidth, bottomRightX + 10, (int) Math.nextUp(eighthHeight + eighthHeight * 0.33), 0xFF000000);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (debug) {
            for (int i = 0; i < 8; i++) {
                context.drawVerticalLine(eighthWidth * i + eighthWidth, 0, this.height, 0xFF000000);
                context.drawHorizontalLine(0, this.width, eighthHeight * i + eighthHeight, 0xFF000000);
            }
        }

        context.enableScissor(eighthWidth, eighthHeight, this.width - eighthWidth, this.height - eighthHeight);
        renderBackground(context);
        context.disableScissor();

        this.configScreenCategoryListWidget.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        calculateSizes();
        this.configScreenCategoryListWidget.updateSize();
    }

    private void calculateSizes() {
        halfWidth = (int) Math.nextUp(this.width * 0.5);
        quarterWidth = (int) Math.nextUp(halfWidth * 0.5);
        eighthWidth = (int) Math.nextUp(quarterWidth * 0.5);

        halfHeight = (int) Math.nextUp(this.height * 0.5);
        quarterHeight = (int) Math.nextUp(halfHeight * 0.5);
        eighthHeight = (int) Math.nextUp(quarterHeight * 0.5);
    }

    @Override
    protected void init() {
        calculateSizes();
        if (configScreenCategoryListWidget == null) {
            configScreenCategoryListWidget = new ConfigScreenCategoryListWidget(this, this.client);
        }

        addSelectableChild(configScreenCategoryListWidget);
    }
}
