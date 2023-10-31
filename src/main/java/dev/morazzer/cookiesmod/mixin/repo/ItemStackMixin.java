package dev.morazzer.cookiesmod.mixin.repo;

import dev.morazzer.cookiesmod.features.repository.items.item.SkyblockItem;
import dev.morazzer.cookiesmod.mixin.ItemNbtAttachment;
import dev.morazzer.cookiesmod.mixin.ItemStackTooltip;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackTooltip, ItemNbtAttachment {

    @Unique
    public SkyblockItem cookies$skyblockItem;
    @Unique
    private NbtCompound cookies$nbt;

    @Shadow
    public abstract Item getItem();

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
    public void cookies$setSkyblockItem(SkyblockItem repositoryItem) {
        this.cookies$skyblockItem = repositoryItem;
    }

    @Override
    public SkyblockItem cookies$getSkyblockItem() {
        return this.cookies$skyblockItem;
    }

    @Nullable
    @Override
    public NbtCompound cookies$getCookiesNbt() {
        return this.cookies$nbt;
    }

    @Override
    public @NotNull NbtCompound cookies$getOrCreateCookiesNbt() {
        return this.cookies$nbt != null ? this.cookies$nbt : (this.cookies$nbt = new NbtCompound());
    }

    @Override
    public void cookies$setCookiesNbt(@NotNull NbtCompound nbt) {
        this.cookies$nbt = nbt;
    }

}
