package dev.morazzer.cookiesmod.mixin.repo;

import dev.morazzer.cookiesmod.mixin.ItemStackTooltip;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
	@Inject(
			method = "addStack(ILnet/minecraft/item/ItemStack;)I",
			at = @At("RETURN"),
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/entity/player/PlayerInventory;setStack(ILnet/minecraft/item/ItemStack;)V"
					)
			),
			locals = LocalCapture.CAPTURE_FAILSOFT
	)
	public void addStack(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir, Item item, int i, ItemStack copy) {
		((ItemStackTooltip) (Object) copy).cookies$setSkyblockItem(((ItemStackTooltip) (Object) stack).cookies$getSkyblockItem());
	}
}
