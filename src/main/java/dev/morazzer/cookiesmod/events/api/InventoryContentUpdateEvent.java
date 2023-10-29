package dev.morazzer.cookiesmod.events.api;

import dev.morazzer.cookiesmod.events.accessors.ScreenHandlerUpdateEventAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

/**
 * Called whenever the inventory contents of a specific screen handler update.
 */
public interface InventoryContentUpdateEvent {

    static void register(ScreenHandler screenHandler, InventoryContentUpdateEvent event) {
        ScreenHandlerUpdateEventAccessor
                .getInventoryUpdateEventAccessor(screenHandler)
                .cookies$inventoryUpdateEvent()
                .register(event);
    }

    /**
     * Called when the inventory contents update.
     *
     * @param slot The slot the item is in.
     * @param item The new item in the slot.
     */
    void updateInventory(int slot, ItemStack item);

}
