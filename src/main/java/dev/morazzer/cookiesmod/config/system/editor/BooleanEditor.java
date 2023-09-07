package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import dev.morazzer.cookiesmod.utils.maths.LinearInterpolatedInteger;
import dev.morazzer.cookiesmod.utils.maths.MathUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class BooleanEditor extends ConfigOptionEditor<Boolean, BooleanOption> {

	private static final Identifier BAR = Identifier.of("cookiesmod", "gui/config/toggle_bar.png");
	private static final Identifier TOGGLE_OFF = Identifier.of("cookiesmod", "gui/config/toggle_off.png");
	private static final Identifier TOGGLE_STEP_ONE = Identifier.of("cookiesmod", "gui/config/toggle_one.png");
	private static final Identifier TOGGLE_STEP_TWO = Identifier.of("cookiesmod", "gui/config/toggle_two.png");
	private static final Identifier TOGGLE_STEP_THREE = Identifier.of("cookiesmod", "gui/config/toggle_three.png");
	private static final Identifier TOGGLE_ON = Identifier.of("cookiesmod", "gui/config/toggle_on.png");

	private final LinearInterpolatedInteger value = new LinearInterpolatedInteger(200, 0);

	public BooleanEditor(BooleanOption option) {
		super(option);
	}

	@Override
	public void init() {
		value.setTargetValue(option.getValue() ? 100 : 0);
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
		super.render(drawContext, mouseX, mouseY, tickDelta, optionWidth);
		value.tick();

		int x = optionWidth / 6 - 24;
		int y = getHeight() - 21;

		drawContext.drawTexture(BAR, x, y, 0, 0, 48, 14, 48, 14);

		float animationPercentage = MathUtils.sigmoidZeroOne(value.getValue() / 100F);
		Identifier buttonIdentifier;
		if (animationPercentage < 0.2) {
			buttonIdentifier = TOGGLE_OFF;
		} else if (animationPercentage < 0.4) {
			buttonIdentifier = TOGGLE_STEP_ONE;
		} else if (animationPercentage < 0.6) {
			buttonIdentifier = TOGGLE_STEP_TWO;
		} else if (animationPercentage < 0.8) {
			buttonIdentifier = TOGGLE_STEP_THREE;
		} else {
			buttonIdentifier = TOGGLE_ON;
		}

		drawContext.drawTexture(buttonIdentifier, (int) (x + (animationPercentage * 36)), y, 0, 0, 12, 14, 12, 14);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button, int optionWidth) {
		int x = optionWidth / 6 - 24;
		int y = getHeight() - 21;

		if ((mouseX >= x) && (mouseX < (x + 48))
				&& (mouseY >= y) && (mouseY < (y + 14))) {
			if (button == 0) {
				this.option.setValue(!this.option.getValue());
				int newTarget = this.option.getValue() ? 100 : 0;
				this.value.setTargetValue(newTarget);
			}
		}
		return false;
	}
}
