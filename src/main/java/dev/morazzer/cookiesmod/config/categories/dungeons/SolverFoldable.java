package dev.morazzer.cookiesmod.config.categories.dungeons;

import dev.morazzer.cookiesmod.config.system.Foldable;
import net.minecraft.text.Text;

public class SolverFoldable extends Foldable {

    public TerminalFoldable terminalFoldable = new TerminalFoldable();

    @Override
    public Text getName() {
        return Text.literal("Solvers");
    }
}
