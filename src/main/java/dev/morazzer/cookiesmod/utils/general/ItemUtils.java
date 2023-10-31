package dev.morazzer.cookiesmod.utils.general;

import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.commands.dev.subcommands.TestEntrypoint;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
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
    public static final String NBT_KEY = "ExtraAttributes";

    /**
     * Gets the skyblock attribute tag of an item.
     *
     * @param itemStack The item.
     * @return The tag.
     */
    public static Optional<NbtCompound> getSkyblockAttributes(ItemStack itemStack) {
        return Optional.ofNullable(itemStack.getSubNbt(NBT_KEY));
    }

    /**
     * Gets the skyblock id of an item.
     *
     * @param itemStack The item.
     * @return The skyblock id.
     */
    public static Optional<String> getSkyblockId(ItemStack itemStack) {
        return getSkyblockAttributes(itemStack)
                .map(nbtCompound -> nbtCompound.getString("id"));
    }

    /**
     * Parses the skyblock id to an internal skyblock id.
     *
     * @param skyblockId The skyblock id.
     * @return The internal skyblock id.
     */
    public static Optional<Identifier> skyblockIdToIdentifier(String skyblockId) {
        return Optional.ofNullable(NAMESPACE.withSuffixedPath(skyblockId.toLowerCase().replace(":", "_")));
    }

    /**
     * Gets the identifier with the internal namespace.
     *
     * @param identifier The identifier.
     * @return The formatted identifier.
     */
    public static String withNamespace(String identifier) {
        return "%s:%s%s".formatted(NAMESPACE.getNamespace(), NAMESPACE.getPath(), identifier.toLowerCase());
    }

    /**
     * Gets the skyblock id as internal identifier.
     *
     * @param itemStack The item.
     * @return The id.
     */
    public static Optional<Identifier> getSkyblockIdAsIdentifier(ItemStack itemStack) {
        return getSkyblockId(itemStack).flatMap(ItemUtils::skyblockIdToIdentifier);
    }

    /**
     * Whether the item in main hand has any enchantment of any level.
     *
     * @param enchantments The enchantments.
     * @return Whether at least one enchantment is present.
     */
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

    /**
     * Whether the item in the main hand is a skyblock item.
     *
     * @return Whether the item is a skyblock item.
     */
    public static boolean hasSkyblockItemInMainHand() {
        return getMainHand().flatMap(ItemUtils::getSkyblockId).isPresent();
    }

    /**
     * Gets the nbt from the main hand.
     *
     * @return The nbt.
     */
    public static Optional<NbtCompound> getNbtFromMainHand() {
        return getMainHand().map(ItemStack::getNbt);
    }

    /**
     * Gets the item in the main hand.
     *
     * @return The item.
     */
    public static Optional<ItemStack> getMainHand() {
        return Optional.ofNullable(MinecraftClient.getInstance()).map(client -> client.player)
                .map(PlayerEntity::getMainHandStack);
    }

    /**
     * Prints the skull texture of the current item into the chat.
     */
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
                JsonObject jsonObject = JsonUtils.gsonClean.fromJson(s, JsonObject.class);
                CookiesUtils.sendMessage(TextUtils.prettyPrintJson(jsonObject));
            }
        }
    }

}
