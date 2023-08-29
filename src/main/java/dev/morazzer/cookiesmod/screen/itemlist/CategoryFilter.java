package dev.morazzer.cookiesmod.screen.itemlist;

import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItem;
import lombok.Getter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.function.Predicate;

@Getter
public enum CategoryFilter {

	UNKNOWN(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Unfiltered").formatted(Formatting.WHITE)),
	ACCESSORY(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Accessories").formatted(Formatting.WHITE)),
	ARMOR(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Armors").formatted(Formatting.WHITE)),
	ARROW(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Arrows").formatted(Formatting.WHITE)),
	AXE(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Axes").formatted(Formatting.WHITE)),
	BAIT(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Baits").formatted(Formatting.WHITE)),
	BOW(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Bows").formatted(Formatting.WHITE)),
	COSMETIC(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Cosmetics").formatted(Formatting.WHITE)),
	DEPLOYABLE(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Deployables").formatted(Formatting.WHITE)),
	EQUIPMENT(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Equipment").formatted(Formatting.WHITE)),
	FAST_TRAVEL(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Fast travel").formatted(Formatting.WHITE)),
	FISHING_ROD(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Fishing Rods").formatted(Formatting.WHITE)),
	HOE(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Hoes").formatted(Formatting.WHITE)),
	MEMENTO(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Mementos").formatted(Formatting.WHITE)),
	MINING(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Mining tools").formatted(Formatting.WHITE)),
	MINION(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Minion").formatted(Formatting.WHITE)),
	PET(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Pet").formatted(Formatting.WHITE)),
	PET_ITEM(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Pet Items").formatted(Formatting.WHITE)),
	POWER_STONE(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Power Stones").formatted(Formatting.WHITE)),
	REFORGE_STONE(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Reforge Stones").formatted(Formatting.WHITE)),
	SHEARS(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Shears").formatted(Formatting.WHITE)),
	SPADE(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Spades").formatted(Formatting.WHITE)),
	SWORD(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Swords").formatted(Formatting.WHITE)),
	WAND(Identifier.of("cookiesmod", "gui/itemlist/rarityfilter/unset.png"), Text.literal("> Wands").formatted(Formatting.WHITE));


	private final Identifier identifier;
	private final MutableText text;

	CategoryFilter(Identifier identifier, MutableText text) {
		this.identifier = identifier;
		this.text = text;
	}

	public static Predicate<Identifier> getPredicate(CategoryFilter categoryFilter) {
		return switch (categoryFilter) {
			case UNKNOWN -> identifier -> true;
			case ACCESSORY -> i -> shouldBeFiltered(i, RepositoryItem.Category.ACCESSORY);
			case ARROW -> i -> shouldBeFiltered(i, RepositoryItem.Category.ARROW, RepositoryItem.Category.ARROW_POISON);
			case AXE -> i -> shouldBeFiltered(i, RepositoryItem.Category.AXE);
			case BAIT -> i -> shouldBeFiltered(i, RepositoryItem.Category.BAIT);
			case EQUIPMENT ->
					i -> shouldBeFiltered(i, RepositoryItem.Category.BELT, RepositoryItem.Category.BRACELET, RepositoryItem.Category.CLOAK, RepositoryItem.Category.GAUNTLET, RepositoryItem.Category.GLOVES, RepositoryItem.Category.NECKLACE);
			case ARMOR ->
					i -> shouldBeFiltered(i, RepositoryItem.Category.HELMET, RepositoryItem.Category.CHESTPLATE, RepositoryItem.Category.LEGGINGS, RepositoryItem.Category.BOOTS);
			case BOW -> i -> shouldBeFiltered(i, RepositoryItem.Category.BOW);
			case COSMETIC -> i -> shouldBeFiltered(i, RepositoryItem.Category.COSMETIC);
			case DEPLOYABLE -> i -> shouldBeFiltered(i, RepositoryItem.Category.DEPLOYABLE);
			case MINING -> i -> shouldBeFiltered(i, RepositoryItem.Category.DRILL, RepositoryItem.Category.PICKAXE);
			case FISHING_ROD ->
					i -> shouldBeFiltered(i, RepositoryItem.Category.FISHING_ROD, RepositoryItem.Category.FISHING_WEAPON);
			case HOE -> i -> shouldBeFiltered(i, RepositoryItem.Category.HOE);
			case SWORD -> i -> shouldBeFiltered(i, RepositoryItem.Category.LONGSWORD, RepositoryItem.Category.SWORD);
			case MEMENTO -> i -> shouldBeFiltered(i, RepositoryItem.Category.MEMENTO);
			case PET_ITEM -> i -> shouldBeFiltered(i, RepositoryItem.Category.PET_ITEM);
			case FAST_TRAVEL ->
					i -> shouldBeFiltered(i, RepositoryItem.Category.PORTAL, RepositoryItem.Category.TRAVEL_SCROLL);
			case REFORGE_STONE -> i -> shouldBeFiltered(i, RepositoryItem.Category.REFORGE_STONE);
			case SHEARS -> i -> shouldBeFiltered(i, RepositoryItem.Category.SHEARS);
			case SPADE -> i -> shouldBeFiltered(i, RepositoryItem.Category.SPADE);
			case WAND -> i -> shouldBeFiltered(i, RepositoryItem.Category.WAND);
			case POWER_STONE -> i -> shouldBeFiltered(i, RepositoryItem.Category.POWER_STONE);
			case MINION -> i -> RepositoryManager.getItem(i).getGenerator().isPresent();
			case PET -> i -> true; // TODO: ADD PETS
		};
	}

	private static boolean shouldBeFiltered(Identifier identifier, RepositoryItem.Category... categories) {
		RepositoryItem repositoryItem = RepositoryManager.getItem(identifier);

		return Arrays.asList(categories).contains(repositoryItem.getCategory());
	}
}
