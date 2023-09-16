package dev.morazzer.cookiesmod.events.accessors;

import dev.morazzer.cookiesmod.events.api.ItemBackgroundRenderCallback;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

@FunctionalInterface
public interface ItemBackgroundAccessor {

	static ItemBackgroundAccessor getItemBackgroundAccessor(HandledScreen<?> screen) {
		return (ItemBackgroundAccessor) screen;
	}

	Event<ItemBackgroundRenderCallback> cookies$itemRenderCallback();

}
