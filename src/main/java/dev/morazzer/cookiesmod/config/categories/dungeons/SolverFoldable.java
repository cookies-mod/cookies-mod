package dev.morazzer.cookiesmod.config.categories.dungeons;

import dev.morazzer.cookiesmod.config.system.Foldable;
import net.minecraft.text.Text;

/**
 * Foldable that contains all settings related to solvers.
 */
public class SolverFoldable extends Foldable {

    public final TerminalFoldable terminalFoldable = new TerminalFoldable();

    @Override
    public Text getName() {
        return Text.literal("Solvers");
    }

}
