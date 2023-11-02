package dev.morazzer.cookiesmod.features.hud;

import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

/**
 * The hud manager to render all hud elements.
 */
@LoadModule("hud/manager")
public class HudManager implements Module {

    private static final ConcurrentHashMap<Identifier, HudElement> elementMap = new ConcurrentHashMap<>();
    @Getter
    private static HudManager instance;

    public HudManager() {
        instance = this;
    }

    /**
     * Registers an hud element to be rendered.
     *
     * @param hudElement The hud element to add.
     */
    public static void registerHudElement(HudElement hudElement) {
        HudElement put = elementMap.put(hudElement.getIdentifier(), hudElement);
        if (put != null) {
            throw new IllegalArgumentException("Two hud elements for id %s".formatted(hudElement.getIdentifier()));
        }
    }

    /**
     * Runs the {@linkplain HudElement#init()} method for all registered elements.
     */
    public static void init() {
        getInstance().getElements().forEach(HudElement::init);
    }

    /**
     * Gets all currently registered elements.
     *
     * @return An iterable list of all hud elements.
     */
    public Iterable<HudElement> getElements() {
        return elementMap.values();
    }

    @Override
    public void load() {
        HudRenderCallback.EVENT.register(this::renderElements);
    }

    @Override
    public String getIdentifierPath() {
        return "hud/manager";
    }

    /**
     * Renders all hud elements onto the screen, if the {@linkplain dev.morazzer.cookiesmod.features.hud.HudEditor} is
     * not open.
     *
     * @param drawContext The current draw context.
     * @param tickDelta   The difference in time between the last tick and now.
     */
    private void renderElements(DrawContext drawContext, double tickDelta) {
        if (MinecraftClient.getInstance().currentScreen instanceof HudEditor) {
            return;
        }
        MinecraftClient.getInstance().getProfiler().push("cookiesmod");
        MinecraftClient.getInstance().getProfiler().push("hud");
        getElements().forEach(hudElement -> hudElement.renderWithTests(drawContext, (float) tickDelta));
        MinecraftClient.getInstance().getProfiler().pop();
        MinecraftClient.getInstance().getProfiler().pop();
    }

}
