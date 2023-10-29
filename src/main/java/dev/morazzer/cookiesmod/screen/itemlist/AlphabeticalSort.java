package dev.morazzer.cookiesmod.screen.itemlist;

import lombok.Getter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

/**
 * Alphabetical sort with their respective icon path.
 */
@Getter
public enum AlphabeticalSort {

    NORMAL(
            Identifier.of("cookiesmod", "gui/itemlist/alphabetical_sort_normal.png"),
            Text.literal("> Sort: A-Z").formatted(Formatting.GREEN)
    ),
    REVERSED(
            Identifier.of("cookiesmod", "gui/itemlist/alphabetical_sort_reversed.png"),
            Text.literal("> Sort: Z-A").formatted(Formatting.GREEN)
    );

    private final Identifier identifier;
    private final MutableText text;

    AlphabeticalSort(Identifier identifier, MutableText text) {
        this.identifier = identifier;
        this.text = text;
    }

}
