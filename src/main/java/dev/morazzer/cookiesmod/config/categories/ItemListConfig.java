package dev.morazzer.cookiesmod.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class ItemListConfig {

	@ConfigOption(name = "Enable Item List", description = "Enables the item list", hiddenKeys = {"item list","toggle items"})
	@Expose
	@ConfigEditorBoolean
	public boolean enableItemList = true;

	public static class Filters {
		@ConfigOption(name = "Enable Museum Filter", description = "Shows or hides the museum filter")
		@Expose
		@ConfigEditorBoolean
		public boolean enableMuseumFilter = true;

		@ConfigOption(name = "Enable Category Filter", description = "Shows or hides the category filter", hiddenKeys = {"type"})
		@Expose
		@ConfigEditorBoolean
		public boolean enableCategoryFilter = true;

		@ConfigOption(name = "Enable Rarity Filter", description = "Shows or hides the item rarity filter", hiddenKeys = {"tier"})
		@Expose
		@ConfigEditorBoolean
		public boolean enableRarityFilter = true;
	}

	public static class Sort {
		@ConfigOption(name = "Enable Alphabetical Sort", description = "Shows or hides the alphabetic sort button")
		@Expose
		@ConfigEditorBoolean
		public boolean enableAlphabeticalSort = true;

		@ConfigOption(name = "Enable Item Rarity Sort", description = "Shows or hides the item rarity sort button", hiddenKeys = {"type"})
		@Expose
		@ConfigEditorBoolean
		public boolean enableItemRaritySort = true;
	}


	@Expose
	@ConfigOption(name = "Filters", description = "")
	@Accordion
	public Filters filters = new Filters();

	@Expose
	@ConfigOption(name = "Sorts", description = "")
	@Accordion
	public Sort sort = new Sort();



}
