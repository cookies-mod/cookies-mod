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
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Formatting;
import org.intellij.lang.annotations.Language;

/**
 * Solver for the "select all colors" terminal.
 */
@LoadModule("dungeons/solvers/terminals/select_color")
public class SelectColorSolver implements Module {

    private static final @Language("RegExp") String titleRegex = "Select all the ([A-Z_]+) .*";

    @Override
    public void load() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof HandledScreen<?> handledScreen)) {
                return;
            }
            if (!LocationUtils.getCurrentArea().isCatacombsFloor7() && !LocationUtils.getCurrentArea()
                .isCatacombsMasterFloor7()) {
                return;
            }
            if (!ConfigManager.getConfig().dungeonCategory.solverFoldable.terminalFoldable.clickAllColors.getValue()) {
                return;
            }
            if (!screen.getTitle().getString().matches(titleRegex)) {
                return;
            }
            String color = screen.getTitle().getString().replaceAll(titleRegex, "$1");
            Formatting formatting = Formatting.byName(color);
            if (formatting == null) {
                return;
            }
            ItemBackgroundRenderCallback.register(
                handledScreen,
                (drawContext, slot) -> this.draw(drawContext, slot, formatting)
            );
        });

    }

    private void draw(DrawContext drawContext, Slot slot, Formatting formatting) {
        if (!Registries.ITEM.getId(slot.getStack().getItem()).getPath().toLowerCase()
            .contains(formatting.name().toLowerCase())) {
            return;
        }
        drawContext.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, ColorUtils.failColor | (0xFF << 24));
    }

    @Override
    public String getIdentifierPath() {
        return "dungeons/solvers/terminals/select_color";
    }
}
