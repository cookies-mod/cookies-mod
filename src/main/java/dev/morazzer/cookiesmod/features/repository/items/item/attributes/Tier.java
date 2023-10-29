package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import lombok.Getter;
import net.minecraft.util.Formatting;

/**
 * A representation of all tiers/rarities in SkyBlock.
 */
@Getter
public enum Tier {
    COMMON(Formatting.WHITE),
    UNCOMMON(Formatting.GREEN),
    RARE(Formatting.BLUE),
    EPIC(Formatting.DARK_PURPLE),
    LEGENDARY(Formatting.GOLD),
    MYTHIC(Formatting.LIGHT_PURPLE),
    SPECIAL(Formatting.RED),
    VERY_SPECIAL(Formatting.RED),
    ADMIN(Formatting.DARK_RED),
    UNOBTAINABLE(Formatting.DARK_RED);

    private final Formatting formatting;

    Tier(Formatting formatting) {
        this.formatting = formatting;
    }

    /**
     * Get the tier by name or common.
     *
     * @param name The name.
     * @return The tier.
     */
    public static Tier byName(String name) {
        for (Tier value : values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return COMMON;
    }

}
