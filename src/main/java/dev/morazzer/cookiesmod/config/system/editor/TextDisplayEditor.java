package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.options.TextDisplayOption;
import net.minecraft.client.gui.DrawContext;

public class TextDisplayEditor extends ConfigOptionEditor<Object, TextDisplayOption> {
	public TextDisplayEditor(TextDisplayOption option) {
		super(option);
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
		super.render(drawContext, mouseX, mouseY, tickDelta, optionWidth);
	}
}
