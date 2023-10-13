package dev.morazzer.cookiesmod.config.system.element;

import dev.morazzer.cookiesmod.utils.maths.LinearInterpolatedInteger;
import dev.morazzer.cookiesmod.utils.maths.MathUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class SwitchElement {

    boolean booleanValue;

    private static final Identifier BAR = Identifier.of("cookiesmod", "gui/config/toggle_bar.png");
    private static final Identifier TOGGLE_OFF = Identifier.of("cookiesmod", "gui/config/toggle_off.png");
    private static final Identifier TOGGLE_STEP_ONE = Identifier.of("cookiesmod", "gui/config/toggle_one.png");
    private static final Identifier TOGGLE_STEP_TWO = Identifier.of("cookiesmod", "gui/config/toggle_two.png");
    private static final Identifier TOGGLE_STEP_THREE = Identifier.of("cookiesmod", "gui/config/toggle_three.png");
    private static final Identifier TOGGLE_ON = Identifier.of("cookiesmod", "gui/config/toggle_on.png");

    private final LinearInterpolatedInteger value = new LinearInterpolatedInteger(200, 0);

    public SwitchElement(boolean value) {
        this.booleanValue = value;
    }

    public void init() {
        value.setTargetValue(booleanValue ? 100 : 0);
    }

    public void render(DrawContext drawContext) {
        value.tick();

        drawContext.drawTexture(BAR, 0, 0, 0, 0, 48, 14, 48, 14);

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

        drawContext.drawTexture(buttonIdentifier, (int) (animationPercentage * 36), 0, 0, 0, 12, 14, 12, 14);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if ((mouseX >= 0) && (mouseX < (48))
                && (mouseY >= 0) && (mouseY < (14))) {
            if (button == 0) {
                this.booleanValue = !booleanValue;
                int newTarget = this.booleanValue ? 100 : 0;
                this.value.setTargetValue(newTarget);
                return true;
            }
        }
        return false;
    }

    public void setTargetValue(int targetValue) {
        value.setTargetValue(targetValue);
    }
}
