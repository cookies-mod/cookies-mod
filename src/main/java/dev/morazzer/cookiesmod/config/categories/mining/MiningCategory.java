package dev.morazzer.cookiesmod.config.categories.mining;

import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import net.minecraft.text.Text;

public class MiningCategory extends Category {

    public BooleanOption showPuzzlerSolution = new BooleanOption(
            Text.literal("Puzzler solver"),
            Text.literal("Highlights the block you have to break for the puzzler"),
            false
    );

    @Override
    public Text getName() {
        return Text.literal("Mining");
    }

    @Override
    public Text getDescription() {
        return Text.literal("All features related to mining");
    }
}
