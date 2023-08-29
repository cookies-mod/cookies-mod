package dev.morazzer.cookiesmod.mixin.repo;

import dev.morazzer.cookiesmod.features.repository.items.RepositoryItem;
import dev.morazzer.cookiesmod.mixin.ItemStackTooltip;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackTooltip {

	@Shadow public abstract Item getItem();

	@Unique
	public RepositoryItem cookies$skyblockItem;

	@Inject(method = "getTooltip", at = @At("HEAD"), cancellable = true)
	public void getTooltipData(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
		if (this.cookies$getSkyblockItem() != null) {
			cir.setReturnValue(this.cookies$getSkyblockItem().getTooltip(context));
		}
	}

	@Inject(method = "copy", at = @At("RETURN"))
	public void copy(CallbackInfoReturnable<ItemStack> cir) {
		ItemStack returnValue = cir.getReturnValue();
		if (this.cookies$getSkyblockItem() != null) {
			((ItemStackTooltip) (Object) returnValue).cookies$setSkyblockItem(this.cookies$getSkyblockItem());
		}
	}

	@Override
	public RepositoryItem cookies$getSkyblockItem() {
		return this.cookies$skyblockItem;
	}

	@Override
	public void cookies$setSkyblockItem(RepositoryItem repositoryItem) {
		this.cookies$skyblockItem = repositoryItem;
	}
}
