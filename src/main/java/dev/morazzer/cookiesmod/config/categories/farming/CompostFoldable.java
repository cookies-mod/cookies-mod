package dev.morazzer.cookiesmod.config.categories.farming;

import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import dev.morazzer.cookiesmod.config.system.options.SliderOption;
import net.minecraft.text.Text;

public class CompostFoldable extends Foldable {

	public BooleanOption showWarnings = new BooleanOption(
			Text.literal("Show compost warnings"),
			Text.literal("If you should get notified for low fuel/organic matter"),
			false
	);

	public SliderOption<Integer> organicMatterAmount = SliderOption.integerOption(
					Text.literal("Organic Matter amount"),
					Text.literal("The amount of organic matter to warn at"),
					30000
			)
			.withMax(50000).withMin(0).withStep(1000);

	public SliderOption<Integer> fuelWarningAmount = SliderOption.integerOption(
			Text.literal("Fuel amount"),
			Text.literal("The amount of fuel to warn at"),
			5000
	).withMax(5000).withMin(3000).withStep(100);

	@Override
	public Text getName() {
		return Text.literal("Compost");
	}
}