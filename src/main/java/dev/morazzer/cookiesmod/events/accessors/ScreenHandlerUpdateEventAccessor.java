package dev.morazzer.cookiesmod.events.accessors;

import dev.morazzer.cookiesmod.events.api.InventoryContentUpdateEvent;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.screen.ScreenHandler;

/**
 * Accessor to get the inventory content update event for a screen.
 */
public interface ScreenHandlerUpdateEventAccessor {

    static ScreenHandlerUpdateEventAccessor getInventoryUpdateEventAccessor(ScreenHandler screen) {
        return (ScreenHandlerUpdateEventAccessor) screen;
    }

    Event<InventoryContentUpdateEvent> cookies$inventoryUpdateEvent();

}
