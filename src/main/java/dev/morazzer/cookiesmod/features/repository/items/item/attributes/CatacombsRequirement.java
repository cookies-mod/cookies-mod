package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

/**
 * Catacombs requirement on an item.
 *
 * @param type        The Type of catacombs.
 * @param dungeonType The Type of dungeons.
 * @param level       The level.
 */
public record CatacombsRequirement(
    CatacombsRequirementType type,
    CatacombsRequirementDungeonType dungeonType,
    int level
) {
}
