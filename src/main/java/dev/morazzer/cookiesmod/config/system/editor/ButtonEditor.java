package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.element.ButtonElement;
import dev.morazzer.cookiesmod.config.system.options.ButtonOption;
import net.minecraft.client.gui.DrawContext;

public class ButtonEditor extends ConfigOptionEditor<Runnable, ButtonOption> {

	private final ButtonElement buttonElement;

	public ButtonEditor(ButtonOption option) {
		super(option);
		this.buttonElement = new ButtonElement(option.getValue(), option.getButtonText());
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
		super.render(drawContext, mouseX, mouseY, tickDelta, optionWidth);

		drawContext.getMatrices().push();
		drawContext.getMatrices().translate((float) optionWidth / 6 - 24, this.getHeight() - 21, 1);
		this.buttonElement.render(drawContext);
		drawContext.getMatrices().pop();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button, int optionWidth) {
		int buttonLeft = optionWidth / 6 - 24;
		int buttonTop = this.getHeight() - 21;
		return this.buttonElement.mouseClicked(mouseX - buttonLeft, mouseY - buttonTop);
	}
}
