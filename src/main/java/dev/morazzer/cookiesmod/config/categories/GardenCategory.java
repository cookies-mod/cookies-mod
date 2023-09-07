package dev.morazzer.cookiesmod.config.categories;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import dev.morazzer.cookiesmod.config.system.options.EnumDropdownOption;
import dev.morazzer.cookiesmod.features.garden.speed.SpeedPresets;
import dev.morazzer.cookiesmod.features.garden.speed.Speeds;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class GardenCategory extends Category {

	@Override
	public Text getName() {
		return Text.literal("Garden").formatted(Formatting.DARK_GREEN);
	}

	@Override
	public Text getDescription() {
		return Text.literal("All settings related with the garden features");
	}

	public static class Visitors extends Foldable {
		@Expose
		public BooleanOption shouldAddItems = new BooleanOption(Text.literal("Show required items"), Text.literal("Rather or not the items should be added to the description"), true);

		@Expose
		public BooleanOption useItemRarityColor = new BooleanOption(Text.literal("Use Item Rarity As Color"), Text.literal("Uses the item rarity for the color of the required items"), true)
				.withHiddenKeys("tier", "visitors");


		@Expose
		public EnumDropdownOption<CountPosition> countPosition = new EnumDropdownOption<>(Text.literal("Show required amount"), Text.literal("Where to show the required amount for the items"), CountPosition.RIGHT);

		@Override
		public Text getName() {
			return Text.literal("Visitor Helper");
		}

		public enum CountPosition {
			LEFT,
			RIGHT
		}
	}

	public static class Speed extends Foldable {

		@Expose
		public BooleanOption showSpeeds = new BooleanOption(Text.literal("Show speeds"), Text.literal("Shows the speeds in the rancher boots for specific crops"), true);

		@Expose
		public BooleanOption showNames = new BooleanOption(Text.literal("Show crop names"), Text.literal("Shows the crop names next to the icons"), true);

		@Expose
		public BooleanOption mergeEqualSpeeds = new BooleanOption(Text.literal("Merge Equal Speeds"), Text.literal("If two crops with the same speed should be merged into one line"), true);

		@Expose
		public EnumDropdownOption<SpeedPresets> speedPresets = new EnumDropdownOption<>(Text.literal("Use speed preset"),Text.literal("Change the preset that is used for speeds"),SpeedPresets.NORMAL);

		@Expose
		public Speeds speeds = new Speeds();

		@Override
		public Text getName() {
			return Text.literal("Speeds");
		}
	}

	public static class Hotkeys {

	}

	@Expose
	public Visitors visitors = new Visitors();

	@Expose
	public Speed speed = new Speed();

}
