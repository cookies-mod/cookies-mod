package dev.morazzer.cookiesmod.events.api;

import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import java.util.List;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;

/**
 * Event to check for updates in inventories.
 */
@FunctionalInterface
public interface InventoryUpdateEvent {

    Event<InventoryUpdateEvent> EVENT = EventFactory.createArrayBacked(
        InventoryUpdateEvent.class,
        inventoryUpdateEvents -> ExceptionHandler.wrap((slot, items) -> {
                for (InventoryUpdateEvent inventoryUpdateEvent : inventoryUpdateEvents) {
                    inventoryUpdateEvent.update(slot, items);
                }
            }
        )
    );

    /**
     * Called when the items in an inventory change.
     *
     * @param syncId The sync id of the inventory.
     * @param items  The new item list.
     */
    void update(int syncId, List<ItemStack> items);

}
