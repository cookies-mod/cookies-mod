package dev.morazzer.cookiesmod.config.categories.dungeons;

import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import net.minecraft.text.Text;

/**
 * Category that describes all settings related to dungeons.
 */
public class DungeonCategory extends Category {

    public final BooleanOption implosionHider = new BooleanOption(
        Text.literal("Implosion Hider"),
        Text.literal("Hides the explosion particles of the Wither Impact ability"),
        true
    );

    public final SolverFoldable solverFoldable = new SolverFoldable();

    @Override
    public Text getName() {
        return Text.literal("Dungeons");
    }

    @Override
    public Text getDescription() {
        return Text.literal("All settings related to dungeons");
    }

}
