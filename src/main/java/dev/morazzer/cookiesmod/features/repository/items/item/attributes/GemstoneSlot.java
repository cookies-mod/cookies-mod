package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import dev.morazzer.cookiesmod.data.enums.GemstoneSlotTypes;

import java.util.List;

/**
 * A gemstone slot on an armor.
 *
 * @param slotType The slot type.
 * @param slots    The requirements to unlock the slot.
 */
public record GemstoneSlot(
        GemstoneSlotTypes slotType,
        List<GemstoneSlotRequirement<?>> slots
) {}
