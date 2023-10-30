package dev.morazzer.cookiesmod.events.accessors;

import dev.morazzer.cookiesmod.events.api.ItemBackgroundRenderCallback;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

/**
 * Accessor to get the item background render callback for a screen.
 */
@FunctionalInterface
public interface ItemBackgroundAccessor {

    static ItemBackgroundAccessor getItemBackgroundAccessor(HandledScreen<?> screen) {
        return (ItemBackgroundAccessor) screen;
    }

    Event<ItemBackgroundRenderCallback> cookies$itemRenderCallback();

}
