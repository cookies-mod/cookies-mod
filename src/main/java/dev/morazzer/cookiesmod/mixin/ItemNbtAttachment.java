package dev.morazzer.cookiesmod.mixin;

import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

/**
 * Accessor to allow storing nbt directly in an item without modifying the original nbt.
 */
public interface ItemNbtAttachment {

    static NbtCompound getCookiesNbt(ItemStack itemStack) {
        return ((ItemNbtAttachment) (Object) itemStack).cookies$getCookiesNbt();
    }

    static NbtCompound getOrCreateCookiesNbt(ItemStack itemStack) {
        return ((ItemNbtAttachment) (Object) itemStack).cookies$getOrCreateCookiesNbt();
    }

    static void setCookiesNbt(ItemStack itemStack, NbtCompound nbtCompound) {
        ((ItemNbtAttachment) (Object) itemStack).cookies$setCookiesNbt(nbtCompound);
    }

    @Nullable
    NbtCompound cookies$getCookiesNbt();

    void cookies$setCookiesNbt(@NotNull NbtCompound nbt);

    @NotNull NbtCompound cookies$getOrCreateCookiesNbt();

}
