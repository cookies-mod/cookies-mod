package dev.morazzer.cookiesmod.features.farming.jacob;

import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.events.api.ItemBackgroundRenderCallback;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import java.util.List;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Text;

/**
 * Highlights the unclaimed jacobs contest in the jacob contest gui.
 */
@LoadModule("farming/unclaimed_contests")
public class UnclaimedJacobsRewards implements Module {

    @Override
    public void load() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof HandledScreen<?> handledScreen) {
                if (handledScreen.getTitle().getString().equals("Your Contests")
                    && ConfigManager.getConfig().gardenCategory.jacobFoldable.highlightUnclaimedContests.getValue()) {
                    ItemBackgroundRenderCallback.register(handledScreen, (drawContext, slot) -> {
                        if (slot.getStack().isEmpty()) {
                            return;
                        }

                        List<Text> tooltip = slot.getStack()
                            .getTooltip(MinecraftClient.getInstance().player, TooltipContext.BASIC);
                        Text lastLine = tooltip.get(tooltip.size() - 1);
                        if (!lastLine.getString().equals("Click to claim reward!")) {
                            return;
                        }

                        drawContext.fill(
                            slot.x,
                            slot.y,
                            slot.x + 16,
                            slot.y + 16,
                            ColorUtils.failColor | 0xFF << 24
                        );
                    });
                }
            }
        });
    }

    @Override
    public String getIdentifierPath() {
        return "farming/unclaimed_contests";
    }

}
