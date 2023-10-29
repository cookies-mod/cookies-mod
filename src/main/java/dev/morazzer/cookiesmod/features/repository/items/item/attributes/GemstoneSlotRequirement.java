package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

/**
 * One requirement to unlock a gemstone slot.
 * For requirementType = COINS value is null.
 *
 * @param requirementType The requirement type.
 * @param value           The item it needs.
 * @param amount          The amount it needs.
 * @param <T>             The parameter to either have integer or identifier types.
 */
public record GemstoneSlotRequirement<T>(
        GemstoneSlotRequirementType<T> requirementType,
        T value,
        int amount
) {}
