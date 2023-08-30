package dev.morazzer.cookiesmod.config.categories;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.features.garden.speed.SpeedPresets;
import dev.morazzer.cookiesmod.features.garden.speed.Speeds;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDropdown;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class GardenCategory {

	public static class Visitors {
		@ConfigOption(name = "Show required items", description = "Rather or not the items should be added to the description")
		@Expose
		@ConfigEditorBoolean
		public boolean shouldAddItems = true;

		@ConfigOption(name = "Use Item Rarity As Color", description = "Uses the item rarity for the color of the required items", hiddenKeys = {"tier", "visitors"})
		@Expose
		@ConfigEditorBoolean
		public boolean useItemRarityColor = true;


		@ConfigOption(name = "Show required amount", description = "Where to show the required amount for the items")
		@Expose
		@ConfigEditorDropdown
		public CountPosition countPosition = CountPosition.RIGHT;

		public enum CountPosition {
			LEFT,
			RIGHT
		}
	}

	public static class Speed {
		@ConfigOption(name = "Show speeds", description = "Shows the speeds in the rancher boots for specific crops")
		@Expose
		@ConfigEditorBoolean
		public boolean showSpeeds = true;

		@ConfigOption(name = "Show crop names", description = "Shows the crop names next to the icons")
		@Expose
		@ConfigEditorBoolean
		public boolean showNames = true;

		@ConfigOption(name = "Merge Equal Speeds", description = "If two crops with the same speed should be merged into one line")
		@Expose
		@ConfigEditorBoolean
		public boolean mergeEqualSpeeds = true;

		@ConfigOption(name = "Use speed preset", description = "Change the preset that is used for speeds")
		@Expose
		@ConfigEditorDropdown
		public SpeedPresets speedPresets = SpeedPresets.NORMAL;


		@Expose
		@ConfigOption(name = "Speed overrides", description = "")
		@Accordion
		public Speeds speeds = new Speeds();
	}

	@Expose
	@ConfigOption(name = "Visitor Helper", description = "")
	@Accordion
	public Visitors visitors = new Visitors();

	@Expose
	@ConfigOption(name = "Walk Speeds", description = "")
	@Accordion
	public Speed speed = new Speed();

}
