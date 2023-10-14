package dev.morazzer.cookiesmod.utils.general;

import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.commands.dev.subcommands.TestEntrypoint;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.TextUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.util.Base64;
import java.util.Optional;

public class ItemUtils {
    private static final Identifier NAMESPACE = new Identifier("skyblock", "items/");

    public static Optional<NbtCompound> getSkyblockAttributes(ItemStack itemStack) {
        return Optional.ofNullable(itemStack.getSubNbt("ExtraAttributes"));
    }

    public static Optional<String> getSkyblockId(ItemStack itemStack) {
        return getSkyblockAttributes(itemStack)
                .map(nbtCompound -> nbtCompound.getString("id"));
    }

    public static Optional<Identifier> skyblockIdToIdentifier(String skyblockId) {
        return Optional.ofNullable(NAMESPACE.withSuffixedPath(skyblockId.toLowerCase().replace(":", "_")));
    }

    public static String withNamespace(String identifier) {
        return "%s:%s%s".formatted(NAMESPACE.getNamespace(), NAMESPACE.getPath(), identifier.toLowerCase());
    }

    public static Optional<Identifier> getSkyblockIdAsIdentifier(ItemStack itemStack) {
        return getSkyblockId(itemStack).flatMap(ItemUtils::skyblockIdToIdentifier);
    }

    public static boolean doesCurrentItemHaveEnchantments(String... enchantments) {
        return getMainHand().flatMap(ItemUtils::getSkyblockAttributes)
                .map(extraAttributes -> extraAttributes.getCompound("enchantments")).map(enchantmentsContainer -> {
                    for (String enchantment : enchantments) {
                        if (enchantmentsContainer.contains(enchantment)) {
                            return true;
                        }
                    }
                    return false;
                }).orElse(false);
    }

    public static boolean hasSkyblockItemInMainHand() {
        return Optional.ofNullable(MinecraftClient.getInstance()).map(client -> client.player)
                .map(PlayerEntity::getMainHandStack).flatMap(ItemUtils::getSkyblockId).isPresent();
    }

    public static Optional<NbtCompound> getNbtFromMainHand() {
        return getMainHand().map(ItemStack::getNbt);
    }

    public static Optional<ItemStack> getMainHand() {
        return Optional.ofNullable(MinecraftClient.getInstance()).map(client -> client.player)
                .map(PlayerEntity::getMainHandStack);
    }

    @TestEntrypoint("get_texture_from_item")
    public static void getTextureFromCurrentItem() {
        if (getMainHand().isEmpty()) return;
        NbtCompound orCreateNbt = getMainHand().get().getOrCreateNbt();
        NbtCompound properties = orCreateNbt.getCompound("SkullOwner").getCompound("Properties");
        NbtList textures = properties.getList("textures", NbtElement.COMPOUND_TYPE);
        for (NbtElement texture : textures) {
            if (texture.getType() == NbtElement.COMPOUND_TYPE) {
                NbtCompound textureValue = (NbtCompound) texture;

                String s = new String(Base64.getDecoder().decode(textureValue.getString("Value")));
                JsonObject jsonObject = GsonUtils.gsonClean.fromJson(s, JsonObject.class);
                CookiesUtils.sendMessage(TextUtils.prettyPrintJson(jsonObject));
            }
        }
    }

}
