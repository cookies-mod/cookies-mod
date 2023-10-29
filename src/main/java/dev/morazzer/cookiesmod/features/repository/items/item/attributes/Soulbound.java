package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

/**
 * Representation of all possible soulbound states.
 */
public enum Soulbound {
    SOLO,
    COOP,
    NONE;

    /**
     * Get the soulbound state from a string or none.
     *
     * @param value The value.
     * @return The state.
     */
    public static Soulbound byName(String value) {
        for (Soulbound soulbound : Soulbound.values()) {
            if (soulbound.name().equalsIgnoreCase(value)) {
                return soulbound;
            }
        }
        return NONE;
    }
}
