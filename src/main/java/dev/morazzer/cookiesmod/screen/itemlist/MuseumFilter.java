package dev.morazzer.cookiesmod.screen.itemlist;

import lombok.Getter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

/**
 * All museum filter states with their respective icons.
 */
@Getter
public enum MuseumFilter {

    UNFILTERED(
        Identifier.of("cookiesmod", "gui/itemlist/museum/default.png"),
        Text.literal("> All Items").formatted(Formatting.WHITE)
    ),
    MUSEUM(
        Identifier.of("cookiesmod", "gui/itemlist/museum/enabled.png"),
        Text.literal("> Museum Items").formatted(Formatting.GREEN)
    ),
    NON_MUSEUM(
        Identifier.of("cookiesmod", "gui/itemlist/museum/disabled.png"),
        Text.literal("> Non Museum Items").formatted(Formatting.RED)
    );

    private final Identifier identifier;
    private final MutableText text;

    MuseumFilter(Identifier identifier, MutableText text) {
        this.identifier = identifier;
        this.text = text;
    }
}
