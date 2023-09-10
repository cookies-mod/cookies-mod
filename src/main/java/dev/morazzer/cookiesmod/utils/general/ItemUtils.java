package dev.morazzer.cookiesmod.utils.general;

import net.minecraft.item.ItemStack;
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

}
