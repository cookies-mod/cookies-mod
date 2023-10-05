package dev.morazzer.cookiesmod.config.categories.mining;

import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import dev.morazzer.cookiesmod.features.mining.FetchurHud;
import net.minecraft.text.Text;

public class MiningCategory extends Category {

    public BooleanOption showPuzzlerSolution = new BooleanOption(
            Text.literal("Puzzler solver"),
            Text.literal("Highlights the block you have to break for the puzzler"),
            false
    );

    public BooleanOption currentFetchurItem = new BooleanOption(
            Text.literal("Fetchur solver"),
            Text.literal("Show next fetchur item"),
            false
    ).withHudElement(new FetchurHud());

    @Override
    public Text getName() {
        return Text.literal("Mining");
    }

    @Override
    public Text getDescription() {
        return Text.literal("All features related to mining");
    }
}