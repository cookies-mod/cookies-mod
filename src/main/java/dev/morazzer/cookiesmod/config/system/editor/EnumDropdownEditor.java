package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.options.EnumDropdownOption;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.Arrays;

public class EnumDropdownEditor<T extends Enum<T>> extends ConfigOptionEditor<T, EnumDropdownOption<T>> {

	private boolean open = false;

	public EnumDropdownEditor(EnumDropdownOption<T> option) {
		super(option);
	}

	@Override
	public void init() {
		this.open = false;
	}

	public int getDropdownWidth(int optionWidth) {
		return Math.min(optionWidth / 3 - 10, 80);
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
		super.render(drawContext, mouseX, mouseY, tickDelta, optionWidth);

		if (!open) {
			int dropdownWidth = this.getDropdownWidth(optionWidth);
			int left = optionWidth / 6 - dropdownWidth / 2;
			int top = getHeight() - 21;

			T selected = this.option.getValue();
			Text displaySelected = this.option.getTextSupplier().supplyText(selected);

			RenderUtils.renderRectangle(drawContext, left, top, dropdownWidth, 14, false);
			//RenderUtils.renderTextScaled(drawContext, Text.literal("▼"), 2, dropdownWidth - 10, top, 0xffb0b0b0, false);
			RenderUtils.renderTextWithMaxWidth(drawContext, displaySelected, dropdownWidth - 16, left + 3, top + 3, ~0, false);
		}
	}

	@Override
	public void renderOverlay(DrawContext context, int mouseX, int mouseY, float delta, int optionWidth) {
		if (open) {
			int dropdownWidth = this.getDropdownWidth(optionWidth);
			int dropdownLeft = optionWidth / 6 - dropdownWidth / 2;
			int dropdownTop = this.getHeight() - 21;

			T selected = this.option.getValue();
			Text displaySelected = this.option.getTextSupplier().supplyText(selected);
			T[] values = selected.getDeclaringClass().getEnumConstants();

			int dropdownHeight = 12 * values.length;
			context.fill(dropdownLeft, dropdownTop, dropdownLeft + dropdownWidth, dropdownTop + dropdownHeight, 0xff202026); //Middle

			context.drawVerticalLine(dropdownLeft, dropdownTop, dropdownTop + dropdownHeight, 0xff2355ad);
			context.drawVerticalLine(dropdownLeft + dropdownWidth, dropdownTop, dropdownTop + dropdownHeight, 0xff2355ad);

			context.drawHorizontalLine(dropdownLeft, dropdownLeft + dropdownWidth, dropdownTop, 0xff2355ad);
			context.drawHorizontalLine(dropdownLeft, dropdownLeft + dropdownWidth, dropdownTop + dropdownHeight, 0xff2355ad);

			int dropdownY = 13;
			for (T value : values) {
				if (value == selected) {
					continue;
				}
				RenderUtils.renderTextWithMaxWidth(
						context,
						this.option.getTextSupplier().supplyText(value),
						dropdownWidth - 6,
						dropdownLeft + 3,
						dropdownTop + 3 + dropdownY,
						0xffb0b0b0,
						false
				);
				dropdownY += 12;
			}

			RenderUtils.renderTextWithMaxWidth(
					context,
					displaySelected,
					dropdownWidth - 16,
					dropdownLeft + 3,
					dropdownTop + 3,
					-1,
					false
			);

		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button, int optionWidth) {
		int dropdownWidth = this.getDropdownWidth(optionWidth);
		int dropdownLeft = (int) (optionWidth / 6f - dropdownWidth / 2f);
		int dropdownTop = getHeight() - 21;

		if (this.open) {
			T[] values = this.option.getValue().getDeclaringClass().getEnumConstants();
			int dropdownHeight = 12 * values.length;

			if ((mouseX >= dropdownLeft) && (mouseX < (dropdownLeft + dropdownWidth))
					&& (mouseY >= dropdownTop) && (mouseX < (dropdownTop + dropdownHeight))) {

				T selected = this.option.getValue();
				int tempIndex = (int) ((mouseY - dropdownTop) / 12);

				if (tempIndex == 0) {
					return true;
				}

				if (tempIndex <= selected.ordinal()) {
					tempIndex--;
				}

				int index = tempIndex;

				this.option.setValue(ExceptionHandler.removeThrows(() -> values[index], selected));
				this.open = false;
				return true;
			}
		}


		if (((mouseX >= dropdownLeft) && (mouseX < (dropdownLeft + dropdownWidth))
				&& (mouseY >= dropdownTop) && (mouseY < (dropdownTop + 14)))) {
			this.open = true;
			return true;
		}

		this.open = false;

		return super.mouseClicked(mouseX, mouseY, button, optionWidth);
	}

	@Override
	public boolean doesMatchSearch(String search) {
		return super.doesMatchSearch(search) || Arrays.stream(this.option.getValue().getDeclaringClass().getEnumConstants()).map(this.option.getTextSupplier()::supplyText).map(Text::getString).anyMatch(option -> option.contains(search));
	}
}
