package dev.morazzer.cookiesmod.events.api;

import dev.morazzer.cookiesmod.events.accessors.ItemBackgroundAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;

@FunctionalInterface
public interface ItemBackgroundRenderCallback {

	public static void register(HandledScreen<?> screen, ItemBackgroundRenderCallback callback) {
		ItemBackgroundAccessor.getItemBackgroundAccessor(screen).cookies$itemRenderCallback().register(callback);
	}

	void renderBackground(DrawContext drawContext, Slot slot);

}
