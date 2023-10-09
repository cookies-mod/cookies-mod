package dev.morazzer.cookiesmod.features.hud;

import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import lombok.Getter;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.concurrent.ConcurrentHashMap;

@LoadModule("hud/manager")
public class HudManager implements Module {

    @Getter
    private static HudManager instance;
    private static final ConcurrentHashMap<Identifier, HudElement> elementMap = new ConcurrentHashMap<>();

    public Iterable<HudElement> getElements() {
        return elementMap.values();
    }

    public static void registerHudElement(HudElement hudElement) {
        HudElement put = elementMap.put(hudElement.getIdentifier(), hudElement);
        if (put != null) {
            throw new IllegalArgumentException("Two hud elements for id %s".formatted(hudElement.getIdentifier()));
        }
    }

    public HudManager() {
        instance = this;
    }

    @Override
    public void load() {
        HudRenderCallback.EVENT.register(this::renderElements);
    }

    private void renderElements(DrawContext drawContext, double tickDelta) {
        if (MinecraftClient.getInstance().currentScreen instanceof HudEditor) {
            return;
        }
        getElements().forEach(hudElement -> hudElement.renderWithTests(drawContext, (float) tickDelta));
    }

    public static void init() {
        getInstance().getElements().forEach(HudElement::init);
    }

    @Override
    public String getIdentifierPath() {
        return "hud/manager";
    }
}
