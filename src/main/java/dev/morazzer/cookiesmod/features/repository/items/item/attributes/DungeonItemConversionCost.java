package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import dev.morazzer.cookiesmod.data.dungeons.EssenceType;

/**
 * The cost to convert one item to a dungeon item.
 *
 * @param essenceType The type of essence.
 * @param amount      The amount of essence.
 */
public record DungeonItemConversionCost(
    EssenceType essenceType,
    int amount
) {
}
