package dev.morazzer.cookiesmod.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The gemstone slots and their respective icons.
 */
@RequiredArgsConstructor
@Getter
public enum GemstoneSlotTypes {
    COMBAT("⚔"),
    DEFENSIVE("☤"),
    AMETHYST("❈"),
    JADE("☘"),
    JASPER("❁"),
    OFFENSIVE("☠"),
    SAPPHIRE("✎"),
    AMBER("⸕"),
    TOPAZ("✧"),
    RUBY("❤"),
    UNIVERSAL("❂"),
    MINING("✦"),
    OPAL("❂");

    private final String icon;
}
