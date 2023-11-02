package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

/**
 * The different item categories.
 */
public enum Category {
    ACCESSORY,
    ARROW,
    ARROW_POISON,
    AXE,
    BAIT,
    BELT,
    BOOTS,
    BOW,
    BRACELET,
    CHESTPLATE,
    CLOAK,
    COSMETIC,
    DEPLOYABLE,
    DRILL,
    DUNGEON_PASS,
    FISHING_ROD,
    FISHING_WEAPON,
    GAUNTLET,
    GLOVES,
    HELMET,
    HOE,
    LEGGINGS,
    LONGSWORD,
    MEMENTO,
    NECKLACE,
    NONE,
    PET_ITEM,
    PICKAXE,
    PORTAL,
    REFORGE_STONE,
    SHEARS,
    SPADE,
    SWORD,
    TRAVEL_SCROLL,
    WAND,
    POWER_STONE,
    UNKNOWN;

    /**
     * Gets the category by name or the default {@linkplain Category#UNKNOWN}.
     *
     * @param name The name of the category.
     * @return The category.
     */
    public static Category byName(String name) {
        for (Category repositoryItem : values()) {
            if (repositoryItem.name().equals(name)) {
                return repositoryItem;
            }
        }
        return UNKNOWN;
    }

}
