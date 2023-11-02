package dev.morazzer.cookiesmod.features.dungeons.solvers.terminals;

import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.events.api.ItemBackgroundRenderCallback;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.intellij.lang.annotations.Language;

/**
 * Solver for the "starts with" terminal.
 */
@LoadModule("dungeons/solvers/terminals/starts_with")
public class StartsWithSolver implements Module {

    private static final @Language("RegExp") String titleRegex = "What starts with:? ['\"](.+)['\"]\\?";

    @Override
    public void load() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof HandledScreen<?> handledScreen)) {
                return;
            }
            if (!LocationUtils.getCurrentArea().isCatacombsFloor7()
                && !LocationUtils.getCurrentArea().isCatacombsMasterFloor7()) {
                return;
            }
            if (!ConfigManager.getConfig()
                .dungeonCategory.solverFoldable.terminalFoldable.clickAllStartingWith.getValue()) {
                return;
            }
            if (!screen.getTitle().getString().matches(titleRegex)) {
                return;
            }

            String region = screen.getTitle().getString().replaceAll(titleRegex, "$1");


            ItemBackgroundRenderCallback.register(
                handledScreen,
                (drawContext, slot) -> draw(drawContext, slot, region)
            );
        });
    }

    private void draw(DrawContext drawContext, Slot slot, String prefix) {
        if (slot.getStack().getName().getString().toLowerCase().startsWith(prefix.toLowerCase())) {
            drawContext.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, ColorUtils.failColor | (0xff << 24));
        }
    }

    @Override
    public String getIdentifierPath() {
        return "dungeons/solvers/terminals/starts_with";
    }
}
