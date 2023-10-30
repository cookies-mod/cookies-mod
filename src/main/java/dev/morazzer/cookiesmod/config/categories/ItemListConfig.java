package dev.morazzer.cookiesmod.config.categories;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import net.minecraft.text.Text;

/**
 * Category that describes all settings related to the item list.
 */
public class ItemListConfig extends Category {

    @Expose
    public final BooleanOption enableItemList = new BooleanOption(
            Text.literal("Enable Item List"),
            Text.literal("Enables the item list"),
            true
    ).withTags("item list", "toggle items", "toggle list");
    @Expose
    public final Filters filters = new Filters();
    @Expose
    public final Sort sort = new Sort();

    @Override
    public Text getName() {
        return Text.literal("Item List");
    }

    @Override
    public Text getDescription() {
        return Text.literal("Settings related to the item list");
    }

    public static class Filters extends Foldable {

        @Expose
        public final BooleanOption enableMuseumFilter = new BooleanOption(
                Text.literal("Enable Museum Filter"),
                Text.literal("Shows or hides the museum filter"),
                true
        );

        @Expose
        public final BooleanOption enableCategoryFilter = new BooleanOption(
                Text.literal("Enable Category Filter"),
                Text.literal("Shows or hides the category filter"),
                true
        ).withTag("type");

        @Expose
        public final BooleanOption enableRarityFilter = new BooleanOption(
                Text.literal("Enable Rarity Filter"),
                Text.literal("Shows or hides the item rarity filter"),
                true
        ).withTag("tier");

        @Override
        public Text getName() {
            return Text.literal("Filters");
        }

    }

    public static class Sort extends Foldable {

        @Expose
        public final BooleanOption enableAlphabeticalSort = new BooleanOption(
                Text.literal("Enable Alphabetical Sort"),
                Text.literal("Shows or hides the alphabetic sort button"),
                true
        );

        @Expose
        public final BooleanOption enableItemRaritySort = new BooleanOption(
                Text.literal("Enable Item Rarity Sort"),
                Text.literal("Shows or hides the item rarity sort button"),
                true
        ).withTag("type");

        @Override
        public Text getName() {
            return Text.literal("Sort");
        }

    }

}
