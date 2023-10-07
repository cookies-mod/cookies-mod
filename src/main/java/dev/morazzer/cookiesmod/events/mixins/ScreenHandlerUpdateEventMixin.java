package dev.morazzer.cookiesmod.events.mixins;

import dev.morazzer.cookiesmod.events.accessors.ScreenHandlerUpdateEventAccessor;
import dev.morazzer.cookiesmod.events.api.InventoryContentUpdateEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ScreenHandler.class)
public class ScreenHandlerUpdateEventMixin implements ScreenHandlerUpdateEventAccessor {

    @Unique
    Event<InventoryContentUpdateEvent> cookies$inventoryUpdateEvent = EventFactory.createArrayBacked(
            InventoryContentUpdateEvent.class,
            inventoryUpdateEvents -> (slot, item) -> {
                for (InventoryContentUpdateEvent inventoryContentUpdateEvent : inventoryUpdateEvents) {
                    inventoryContentUpdateEvent.updateInventory(slot, item);
                }
            }
    );

    @Inject(method = "updateSlotStacks", at = @At("RETURN"))
    public void updateSlotStacks(int revision, List<ItemStack> stacks, ItemStack cursorStack, CallbackInfo ci) {
        for (int i = 0; i < stacks.size(); ++i) {
            cookies$inventoryUpdateEvent.invoker().updateInventory(i, stacks.get(i));
        }
    }

    @Inject(method = "setStackInSlot", at = @At("RETURN"))
    public void setStackInSlot(int slot, int revision, ItemStack stack, CallbackInfo ci) {
        cookies$inventoryUpdateEvent.invoker().updateInventory(slot, stack);
    }


    @Override
    public Event<InventoryContentUpdateEvent> cookies$inventoryUpdateEvent() {
        return cookies$inventoryUpdateEvent;
    }
}
