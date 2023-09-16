package dev.morazzer.cookiesmod.events.mixins;

import dev.morazzer.cookiesmod.events.accessors.ItemBackgroundAccessor;
import dev.morazzer.cookiesmod.events.api.ItemBackgroundRenderCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class ItemBackgroundRenderMixin implements ItemBackgroundAccessor {
	@Inject(method = "init", at = @At("HEAD"))
	private void init(CallbackInfo ci) {
		this.cookies$init();
	}

	@Inject(method = "drawSlot", at = @At("INVOKE"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItem(Lnet/minecraft/item/ItemStack;III)V", shift = At.Shift.BEFORE)))
	public void renderBackground(DrawContext context, Slot slot, CallbackInfo ci) {
		this.backgroundCallbacks.invoker().renderBackground(context, slot);
	}

	Event<ItemBackgroundRenderCallback> backgroundCallbacks;

	@Override
	public Event<ItemBackgroundRenderCallback> cookies$itemRenderCallback() {
		return this.backgroundCallbacks;
	}

	@Unique
	private void cookies$init() {
		this.backgroundCallbacks = EventFactory.createArrayBacked(ItemBackgroundRenderCallback.class, itemBackgroundRenderCallbacks -> (drawContext, slot) -> {
			for (ItemBackgroundRenderCallback itemBackgroundRenderCallback : itemBackgroundRenderCallbacks) {
				itemBackgroundRenderCallback.renderBackground(drawContext, slot);
			}
		});
	}
}
