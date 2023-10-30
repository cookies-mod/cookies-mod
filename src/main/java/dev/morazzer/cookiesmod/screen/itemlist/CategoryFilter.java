package dev.morazzer.cookiesmod.screen.itemlist;

import dev.morazzer.cookiesmod.features.repository.items.RepositoryItemManager;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.Category;
import lombok.Getter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * All types of category filters with the respective icon path.
 */
@Getter
public enum CategoryFilter {

    UNKNOWN(
            Identifier.of("cookiesmod", "gui/itemlist/category/unfiltered.png"),
            Text.literal("> Unfiltered").formatted(Formatting.WHITE)
    ),
    ACCESSORY(
            Identifier.of("cookiesmod", "gui/itemlist/category/accessories.png"),
            Text.literal("> Accessories").formatted(Formatting.WHITE)
    ),
    ARMOR(
            Identifier.of("cookiesmod", "gui/itemlist/category/armor.png"),
            Text.literal("> Armors").formatted(Formatting.WHITE)
    ),
    ARROW(
            Identifier.of("cookiesmod", "gui/itemlist/category/arrows.png"),
            Text.literal("> Arrows").formatted(Formatting.WHITE)
    ),
    AXE(
            Identifier.of("cookiesmod", "gui/itemlist/category/axe.png"),
            Text.literal("> Axes").formatted(Formatting.WHITE)
    ),
    BAIT(
            Identifier.of("cookiesmod", "gui/itemlist/category/baits.png"),
            Text.literal("> Baits").formatted(Formatting.WHITE)
    ),
    BOW(
            Identifier.of("cookiesmod", "gui/itemlist/category/bows.png"),
            Text.literal("> Bows").formatted(Formatting.WHITE)
    ),
    COSMETIC(
            Identifier.of("cookiesmod", "gui/itemlist/category/cosmetics.png"),
            Text.literal("> Cosmetics").formatted(Formatting.WHITE)
    ),
    DEPLOYABLE(
            Identifier.of("cookiesmod", "gui/itemlist/category/deployables.png"),
            Text.literal("> Deployables").formatted(Formatting.WHITE)
    ),
    EQUIPMENT(
            Identifier.of("cookiesmod", "gui/itemlist/category/equipment.png"),
            Text.literal("> Equipment").formatted(Formatting.WHITE)
    ),
    FAST_TRAVEL(
            Identifier.of("cookiesmod", "gui/itemlist/category/travel_scroll.png"),
            Text.literal("> Fast travel").formatted(Formatting.WHITE)
    ),
    FISHING_ROD(
            Identifier.of("cookiesmod", "gui/itemlist/category/rod.png"),
            Text.literal("> Fishing Rods").formatted(Formatting.WHITE)
    ),
    HOE(
            Identifier.of("cookiesmod", "gui/itemlist/category/hoe.png"),
            Text.literal("> Hoes").formatted(Formatting.WHITE)
    ),
    MEMENTO(
            Identifier.of("cookiesmod", "gui/itemlist/category/momento.png"),
            Text.literal("> Mementos").formatted(Formatting.WHITE)
    ),
    MINING(
            Identifier.of("cookiesmod", "gui/itemlist/category/pickaxe.png"),
            Text.literal("> Mining tools").formatted(Formatting.WHITE)
    ),
    MINION(
            Identifier.of("cookiesmod", "gui/itemlist/category/minions.png"),
            Text.literal("> Minion").formatted(Formatting.WHITE)
    ),
    PET(
            Identifier.of("cookiesmod", "gui/itemlist/category/pets.png"),
            Text.literal("> Pet").formatted(Formatting.WHITE)
    ),
    PET_ITEM(
            Identifier.of("cookiesmod", "gui/itemlist/category/pet_items.png"),
            Text.literal("> Pet Items").formatted(Formatting.WHITE)
    ),
    POWER_STONE(
            Identifier.of("cookiesmod", "gui/itemlist/category/power_stone.png"),
            Text.literal("> Power Stones").formatted(Formatting.WHITE)
    ),
    REFORGE_STONE(
            Identifier.of("cookiesmod", "gui/itemlist/category/reforge_stone.png"),
            Text.literal("> Reforge Stones").formatted(Formatting.WHITE)
    ),
    SHEARS(
            Identifier.of("cookiesmod", "gui/itemlist/category/shears.png"),
            Text.literal("> Shears").formatted(Formatting.WHITE)
    ),
    SPADE(
            Identifier.of("cookiesmod", "gui/itemlist/category/shovel.png"),
            Text.literal("> Spades").formatted(Formatting.WHITE)
    ),
    SWORD(
            Identifier.of("cookiesmod", "gui/itemlist/category/sword.png"),
            Text.literal("> Swords").formatted(Formatting.WHITE)
    ),
    WAND(
            Identifier.of("cookiesmod", "gui/itemlist/category/wand.png"),
            Text.literal("> Wands").formatted(Formatting.WHITE)
    );

    private final Identifier identifier;
    private final MutableText text;

    CategoryFilter(Identifier identifier, MutableText text) {
        this.identifier = identifier;
        this.text = text;
    }

    public static Predicate<Identifier> getPredicate(CategoryFilter categoryFilter) {
        return switch (categoryFilter) {
            case UNKNOWN -> identifier -> true;
            case ACCESSORY -> i -> shouldBeFiltered(i, Category.ACCESSORY);
            case ARROW -> i -> shouldBeFiltered(i, Category.ARROW, Category.ARROW_POISON);
            case AXE -> i -> shouldBeFiltered(i, Category.AXE);
            case BAIT -> i -> shouldBeFiltered(i, Category.BAIT);
            case EQUIPMENT -> i -> shouldBeFiltered(i,
                    Category.BELT,
                    Category.BRACELET,
                    Category.CLOAK,
                    Category.GAUNTLET,
                    Category.GLOVES,
                    Category.NECKLACE
            );
            case ARMOR ->
                    i -> shouldBeFiltered(i, Category.HELMET, Category.CHESTPLATE, Category.LEGGINGS, Category.BOOTS);
            case BOW -> i -> shouldBeFiltered(i, Category.BOW);
            case COSMETIC -> i -> shouldBeFiltered(i, Category.COSMETIC);
            case DEPLOYABLE -> i -> shouldBeFiltered(i, Category.DEPLOYABLE);
            case MINING -> i -> shouldBeFiltered(i, Category.DRILL, Category.PICKAXE);
            case FISHING_ROD -> i -> shouldBeFiltered(i, Category.FISHING_ROD, Category.FISHING_WEAPON);
            case HOE -> i -> shouldBeFiltered(i, Category.HOE);
            case SWORD -> i -> shouldBeFiltered(i, Category.LONGSWORD, Category.SWORD);
            case MEMENTO -> i -> shouldBeFiltered(i, Category.MEMENTO);
            case PET_ITEM -> i -> shouldBeFiltered(i, Category.PET_ITEM);
            case FAST_TRAVEL -> i -> shouldBeFiltered(i, Category.PORTAL, Category.TRAVEL_SCROLL);
            case REFORGE_STONE -> i -> shouldBeFiltered(i, Category.REFORGE_STONE);
            case SHEARS -> i -> shouldBeFiltered(i, Category.SHEARS);
            case SPADE -> i -> shouldBeFiltered(i, Category.SPADE);
            case WAND -> i -> shouldBeFiltered(i, Category.WAND);
            case POWER_STONE -> i -> shouldBeFiltered(i, Category.POWER_STONE);
            case MINION -> i -> RepositoryItemManager.getItem(i).getGenerator().isPresent();
            case PET -> i -> true; // TODO: ADD PETS
        };
    }

    private static boolean shouldBeFiltered(Identifier identifier, Category... categories) {
        return Arrays.asList(categories).contains(RepositoryItemManager.getItem(identifier).getCategory());
    }
}
