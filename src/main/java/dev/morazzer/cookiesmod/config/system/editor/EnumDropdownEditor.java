package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.element.DropdownElement;
import dev.morazzer.cookiesmod.config.system.options.EnumDropdownOption;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.Arrays;

public class EnumDropdownEditor<T extends Enum<T>> extends ConfigOptionEditor<T, EnumDropdownOption<T>> {

    private DropdownElement<T> dropdownElement;

    public EnumDropdownEditor(EnumDropdownOption<T> option) {
        super(option);
    }

    @Override
    public void init() {
        this.dropdownElement = new DropdownElement<>(
                this.option.getValue().getDeclaringClass().getEnumConstants(),
                this.option.getTextSupplier()::supplyText
        );
        this.dropdownElement.setSelected(this.option.getValue());
    }

    public int getDropdownWidth(int optionWidth) {
        return Math.min(optionWidth / 3 - 10, 80);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
        super.render(drawContext, mouseX, mouseY, tickDelta, optionWidth);

		drawContext.getMatrices().push();
	    int dropdownWidth = this.getDropdownWidth(optionWidth);
		drawContext.getMatrices().translate((float) optionWidth / 6 - (float) dropdownWidth / 2, getHeight() - 21, 0);
		this.dropdownElement.render(drawContext, dropdownWidth);
		drawContext.getMatrices().pop();
    }

    @Override
    public void renderOverlay(DrawContext drawContext, int mouseX, int mouseY, float delta, int optionWidth) {
	    drawContext.getMatrices().push();
	    int dropdownWidth = this.getDropdownWidth(optionWidth);
	    drawContext.getMatrices().translate((float) optionWidth / 6 - (float) dropdownWidth / 2, getHeight() - 21, 0);
	    this.dropdownElement.renderOverlay(drawContext, dropdownWidth);
	    drawContext.getMatrices().pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int optionWidth) {
        int dropdownWidth = this.getDropdownWidth(optionWidth);
        int dropdownLeft = (int) (optionWidth / 6f - dropdownWidth / 2f);
        int dropdownTop = getHeight() - 21;
		T value;

		if ((value = this.dropdownElement.mouseClicked(mouseX - dropdownLeft, mouseY - dropdownTop, dropdownWidth)) != null) {
			this.option.setValue(value);
			return true;
		}

        return super.mouseClicked(mouseX, mouseY, button, optionWidth);
    }

    @Override
    public boolean doesMatchSearch(String search) {
        return super.doesMatchSearch(search) || Arrays.stream(this.option.getValue().getDeclaringClass()
                        .getEnumConstants()).map(this.option.getTextSupplier()::supplyText).map(Text::getString)
                .anyMatch(option -> option.contains(search));
    }
}
