package dev.morazzer.cookiesmod.screen.itemlist;

import lombok.Getter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

@Getter
public enum RaritySort {

	LOWEST_FIRST(Identifier.of("cookiesmod", "gui/itemlist/rarity_sort_common.png"), Text.literal("> Common first").formatted(Formatting.GREEN)),
	HIGHEST_FIRST(Identifier.of("cookiesmod", "gui/itemlist/rarity_sort_rare.png"), Text.literal("> Rarest first").formatted(Formatting.GREEN)),
	UNSORTED(Identifier.of("cookiesmod", "gui/itemlist/rarity_sort_unsorted.png"), Text.literal("> Unsorted").formatted(Formatting.GREEN));

	private final Identifier identifier;
	private final MutableText text;

	RaritySort(Identifier identifier, MutableText text) {
		this.identifier = identifier;
		this.text = text;
	}

}
