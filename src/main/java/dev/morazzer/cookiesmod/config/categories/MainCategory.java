package dev.morazzer.cookiesmod.config.categories;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import net.minecraft.text.Text;

public class MainCategory extends Category {

	@Expose
	public BooleanOption booleanOption = new BooleanOption(Text.literal("Auto Updates"), Text.literal("Automatically checks for updates on startup"), true);

	@Override
	public Text getName() {
		return Text.literal("Main Category");
	}

	@Override
	public Text getDescription() {
		return Text.literal("Test Description");
	}

}
