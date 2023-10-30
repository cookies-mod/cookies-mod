package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import net.minecraft.util.Identifier;

/**
 * The types of gemstone requirements.
 *
 * @param <T> The type.
 */
@SuppressWarnings("InstantiationOfUtilityClass")
public class GemstoneSlotRequirementType<T> {

    public static final GemstoneSlotRequirementType<Integer> COINS = new GemstoneSlotRequirementType<>();
    public static final GemstoneSlotRequirementType<Identifier> ITEM = new GemstoneSlotRequirementType<>();

}
