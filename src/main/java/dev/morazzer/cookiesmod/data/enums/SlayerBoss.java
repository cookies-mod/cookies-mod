package dev.morazzer.cookiesmod.data.enums;

import org.jetbrains.annotations.NotNull;

/**
 * All slayer bosses.
 */
public enum SlayerBoss {
    ZOMBIE,
    WOLF,
    ENDERMAN,
    SPIDER,
    BLAZE,
    VAMPIRE;

    public static SlayerBoss valueOfIgnore(@NotNull String value) {
        return valueOf(value.toUpperCase());
    }

}
