package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.element.SwitchElement;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.NotNull;

/**
 * Editor to display a single boolean value switch.
 */
public class BooleanEditor extends ConfigOptionEditor<Boolean, BooleanOption> {

    private SwitchElement switchElement;

    public BooleanEditor(BooleanOption option) {
        super(option);
    }

    @Override
    public void init() {
        this.switchElement = new SwitchElement(option.getValue());
        this.switchElement.setTargetValue(option.getValue() ? 100 : 0);
    }

    @Override
    public void render(@NotNull DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
        super.render(drawContext, mouseX, mouseY, tickDelta, optionWidth);

        int x = optionWidth / 6 - 24;
        int y = getHeight() - 21;

        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(x, y, 1);
        switchElement.render(drawContext);
        drawContext.getMatrices().pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int optionWidth) {
        int x = optionWidth / 6 - 24;
        int y = getHeight() - 21;


        if (this.switchElement.mouseClicked(mouseX - x, mouseY - y, button)) {
            this.option.setValue(!this.option.getValue());
        }
        return false;
    }

}
