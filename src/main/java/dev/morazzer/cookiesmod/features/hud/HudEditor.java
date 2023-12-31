package dev.morazzer.cookiesmod.features.hud;

import dev.morazzer.cookiesmod.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * Editor to move hud elements around on the screen.
 */
public class HudEditor extends Screen {

    private final Screen screen;
    private HudElement selected = null;

    public HudEditor() {
        super(Text.literal("Hud editor"));
        this.screen = MinecraftClient.getInstance().currentScreen;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        HudManager.getInstance().getElements().forEach(hudElement -> hudElement.render(context, delta));
    }

    @Override
    public void close() {
        ConfigManager.saveConfig(true, "hud-positions-changed");

        if (screen != null) {
            MinecraftClient.getInstance().setScreen(screen);
            return;
        }
        super.close();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), 0xAB << 24);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (HudElement element : HudManager.getInstance().getElements()) {
            if (mouseX >= element.getX() && mouseX < element.getX() + element.getWidth()
                && mouseY >= element.getY() && mouseY < element.getY() + element.getHeight()) {
                this.selected = element;
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.selected = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.selected != null) {
            this.selected.setPosition(this.selected.getPosition().add(
                deltaX,
                deltaY
            ));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

}
