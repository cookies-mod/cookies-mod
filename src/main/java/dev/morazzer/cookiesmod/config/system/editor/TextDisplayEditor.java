package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.options.TextDisplayOption;
import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.NotNull;

/**
 * Editor to display text.
 */
public class TextDisplayEditor extends ConfigOptionEditor<Object, TextDisplayOption> {

    public TextDisplayEditor(TextDisplayOption option) {
        super(option);
    }

    @Override
    public void render(@NotNull DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
        super.render(drawContext, mouseX, mouseY, tickDelta, optionWidth);
    }

}
