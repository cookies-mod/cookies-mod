package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.options.ButtonOption;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ButtonEditor extends ConfigOptionEditor<Runnable, ButtonOption> {
	private static final Identifier BUTTON = Identifier.of("cookiesmod", "gui/config/button.png");

	public ButtonEditor(ButtonOption option) {
		super(option);
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
		super.render(drawContext, mouseX, mouseY, tickDelta, optionWidth);

		drawContext.drawTexture(BUTTON, optionWidth / 6 - 24, this.getHeight() - 21, 0, 0, 48, 16, 48, 16);
		drawContext.drawCenteredTextWithShadow(this.getTextRenderer(), this.option.asOption().getButtonText(), (int) (optionWidth / 6f), getHeight() - 16, ~0);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button, int optionWidth) {
		int buttonLeft = optionWidth / 6 - 24;
		int buttonTop = this.getHeight() - 21;
		if ((mouseX > buttonLeft) && (mouseX < (buttonLeft + 48))
				&& (mouseY > buttonTop) && (mouseY < (buttonTop + 16))) {
			this.option.asOption().getValue().run();
		}
		return false;
	}
}
