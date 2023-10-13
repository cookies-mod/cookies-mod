package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.element.DropdownElement;
import dev.morazzer.cookiesmod.config.system.options.StringDropdownOption;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class StringDropdownEditor extends ConfigOptionEditor<String, StringDropdownOption> {
    private DropdownElement<String> dropdownElement;

    public StringDropdownEditor(StringDropdownOption option) {
        super(option);
    }

    @Override
    public void init() {
        this.dropdownElement = new DropdownElement<>(
                this.option.getPossibleValues().toArray(String[]::new),
                Text::literal
        );
        this.dropdownElement.setSelected(this.option.getValue());
    }

    public int getDropdownWidth(int optionWidth) {
        return Math.min(optionWidth / 3 - 10, 80);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
        super.render(drawContext, mouseX, mouseY, tickDelta, optionWidth);
        int dropdownWidth = this.getDropdownWidth(optionWidth);

        drawContext.getMatrices().push();
        drawContext.getMatrices().translate((float) optionWidth / 6 - (float) dropdownWidth / 2, getHeight() - 21, 0);
        dropdownElement.render(drawContext, dropdownWidth);
        drawContext.getMatrices().pop();
    }

    @Override
    public void renderOverlay(DrawContext drawContext, int mouseX, int mouseY, float delta, int optionWidth) {
        int dropdownWidth = this.getDropdownWidth(optionWidth);
        drawContext.getMatrices().push();
        drawContext.getMatrices().translate((float) optionWidth / 6 - (float) dropdownWidth / 2, getHeight() - 21, 0);
        dropdownElement.render(drawContext, dropdownWidth);
        drawContext.getMatrices().pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int optionWidth) {
        int dropdownWidth = this.getDropdownWidth(optionWidth);
        int dropdownLeft = (int) (optionWidth / 6f - dropdownWidth / 2f);
        int dropdownTop = getHeight() - 21;

        String value;
        if ((value = this.dropdownElement.mouseClicked(
                mouseX - dropdownLeft,
                mouseY - dropdownTop,
                dropdownWidth
        )) != null) {
            this.option.setValue(value);
        }

        return super.mouseClicked(mouseX, mouseY, button, optionWidth);
    }

    @Override
    public boolean doesMatchSearch(String search) {
        return super.doesMatchSearch(search) || this.option.getPossibleValues().stream()
                .anyMatch(key -> key.contains(search));
    }
}
