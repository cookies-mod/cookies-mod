package dev.morazzer.cookiesmod.utils.general;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ItemUtils {
    private static final Identifier NAMESPACE = new Identifier("skyblock", "items/");

    public static Optional<String> getSkyblockId(ItemStack itemStack) {
        return Optional.ofNullable(itemStack.getSubNbt("ExtraAttributes"))
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
        return getNbtFromMainHand().map(nbt -> nbt.getCompound("ExtraAttributes"))
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

}
