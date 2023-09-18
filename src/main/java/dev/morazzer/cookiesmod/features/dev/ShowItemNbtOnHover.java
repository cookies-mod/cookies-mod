package dev.morazzer.cookiesmod.features.dev;

import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.DevUtils;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@LoadModule
public class ShowItemNbtOnHover implements Module {
	private static final Identifier RENDER_NBT = DevUtils.createIdentifier("render_item_nbt");

	@Override
	public void load() {
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof HandledScreen<?> handledScreen) {
				if (!DevUtils.isEnabled(RENDER_NBT)) {
					return;
				}
				ScreenEvents.afterRender(screen).register(this::afterRender);
			}
		});
	}

	private void afterRender(Screen screen, DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
		HandledScreen<?> handledScreen = (HandledScreen<?>) screen;

		for (Slot slot : handledScreen.getScreenHandler().slots) {
			if (handledScreen.isPointOverSlot(slot, mouseX, mouseY)) {
				NbtCompound orCreateNbt = slot.getStack()
						.getNbt();
				if (orCreateNbt == null || orCreateNbt.isEmpty()) return;
				Text prettyPrintedText = NbtHelper.toPrettyPrintedText(orCreateNbt);
				List<OrderedText> orderedTexts = new ArrayList<>(MinecraftClient.getInstance().textRenderer.wrapLines(
						prettyPrintedText,
						200
				));
				orderedTexts.add(Text.literal("Index: ").append(String.valueOf(slot.getIndex())).asOrderedText());
				drawContext.drawTooltip(
						MinecraftClient.getInstance().textRenderer,
						orderedTexts,
						HoveredTooltipPositioner.INSTANCE,
						mouseX - 216 + (200 - Math.min(MinecraftClient.getInstance().textRenderer.getWidth(
								prettyPrintedText), 200)),
						mouseY
				);
			}
		}
	}

	@Override
	public String getIdentifierPath() {
		return "dev/show_nbt";
	}
}
