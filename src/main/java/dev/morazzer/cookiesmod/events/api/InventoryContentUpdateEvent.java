package dev.morazzer.cookiesmod.events.api;

import dev.morazzer.cookiesmod.events.accessors.ScreenHandlerUpdateEventAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public interface InventoryContentUpdateEvent {

    static void register(ScreenHandler screenHandler, InventoryContentUpdateEvent event) {
        ScreenHandlerUpdateEventAccessor.getInventoryUpdateEventAccessor(screenHandler).cookies$inventoryUpdateEvent().register(event);
    }

    void updateInventory(int slot, ItemStack item);

}
