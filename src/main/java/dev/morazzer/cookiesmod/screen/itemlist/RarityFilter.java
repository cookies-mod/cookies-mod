package dev.morazzer.cookiesmod.screen.itemlist;

import lombok.Getter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

/**
 * All different rarity filter states with their respective icons.
 */
@Getter
public enum RarityFilter {
    NO_FILTER(
        Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"),
        Text.literal("> No Filter").formatted(Formatting.WHITE)
    ),
    COMMON(
        Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/common.png"),
        Text.literal("> Common").formatted(Formatting.WHITE)
    ),
    UNCOMMON(
        Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/uncommon.png"),
        Text.literal("> Uncommon").formatted(Formatting.GREEN)
    ),
    RARE(
        Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/rare.png"),
        Text.literal("> Rare").formatted(Formatting.BLUE)
    ),
    EPIC(
        Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/epic.png"),
        Text.literal("> Epic").formatted(Formatting.DARK_PURPLE)
    ),
    LEGENDARY(
        Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/legendary.png"),
        Text.literal("> Legendary").formatted(Formatting.GOLD)
    ),
    MYTHIC(
        Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/mythic.png"),
        Text.literal("> Mythic").formatted(Formatting.LIGHT_PURPLE)
    ),
    SPECIAL(
        Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/special.png"),
        Text.literal("> (Very) Special").formatted(Formatting.RED)
    ),
    ADMIN(
        Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/admin.png"),
        Text.literal("> Admin").formatted(Formatting.DARK_RED)
    ),
    UNOBTAINABLE(
        Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/admin.png"),
        Text.literal("> Unobtainable").formatted(Formatting.DARK_RED)
    );

    private final Identifier identifier;
    private final MutableText text;

    RarityFilter(Identifier identifier, MutableText text) {
        this.identifier = identifier;
        this.text = text;
    }
}
