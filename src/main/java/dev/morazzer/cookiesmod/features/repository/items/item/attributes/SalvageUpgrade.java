package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

/**
 * A representation of either a salvage or upgrade result.
 *
 * @param salvageType The type of the value.
 * @param value       The value.
 * @param amount      The amount of the value.
 * @param <T>         The type.
 */
public record SalvageUpgrade<T>(
    SalvageType<T> salvageType,
    T value,
    int amount
) {
}
