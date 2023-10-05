package dev.morazzer.cookiesmod.events.api;

import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;

import java.util.List;


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

	void update(int syncId, List<ItemStack> items);

}
