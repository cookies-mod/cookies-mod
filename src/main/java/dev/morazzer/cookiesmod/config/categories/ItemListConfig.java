package dev.morazzer.cookiesmod.config.categories;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import net.minecraft.text.Text;

public class ItemListConfig extends Category {

	@Expose
	public BooleanOption enableItemList = new BooleanOption(Text.literal("Enable Item List"), Text.literal("Enables the item list"), true)
			.withHiddenKeys("item list", "toggle items", "toggle list");

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
		public BooleanOption enableMuseumFilter = new BooleanOption(Text.literal("Enable Museum Filter"), Text.literal("Shows or hides the museum filter"), true);

		@Expose
		public BooleanOption enableCategoryFilter = new BooleanOption(Text.literal("Enable Category Filter"), Text.literal("Shows or hides the category filter"), true)
				.withHiddenKey("type");

		@Expose
		public BooleanOption enableRarityFilter = new BooleanOption(Text.literal("Enable Rarity Filter"), Text.literal("Shows or hides the item rarity filter"), true)
				.withHiddenKey("tier");

		@Override
		public Text getName() {
			return Text.literal("Filters");
		}
	}

	public static class Sort extends Foldable {
		@Expose
		public BooleanOption enableAlphabeticalSort = new BooleanOption(Text.literal("Enable Alphabetical Sort"), Text.literal("Shows or hides the alphabetic sort button"), true);

		@Expose
		public BooleanOption enableItemRaritySort = new BooleanOption(Text.literal("Enable Item Rarity Sort"), Text.literal("Shows or hides the item rarity sort button"), true)
				.withHiddenKey("type");

		@Override
		public Text getName() {
			return Text.literal("Sort");
		}
	}


	@Expose
	public Filters filters = new Filters();

	@Expose
	public Sort sort = new Sort();


}
