package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.options.StringDropdownOption;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StringDropdownEditor extends ConfigOptionEditor<String, StringDropdownOption> {
	private boolean open = false;

	public StringDropdownEditor(StringDropdownOption option) {
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


			RenderUtils.renderRectangle(drawContext, left, top, dropdownWidth, 14, false);
			//RenderUtils.renderTextScaled(drawContext, Text.literal("▼"), 2, dropdownWidth - 10, top, 0xffb0b0b0, false);
			RenderUtils.renderTextWithMaxWidth(drawContext, Text.of(this.option.getValue()), dropdownWidth - 16, left + 3, top + 3, ~0, false);
		}
	}

	@Override
	public void renderOverlay(DrawContext context, int mouseX, int mouseY, float delta, int optionWidth) {
		if (open) {
			int dropdownWidth = this.getDropdownWidth(optionWidth);
			int dropdownLeft = optionWidth / 6 - dropdownWidth / 2;
			int dropdownTop = this.getHeight() - 21;

			String selected = this.option.getValue();
			Set<String> values = this.option.getPossibleValues();

			int dropdownHeight = 12 * values.size();
			context.fill(dropdownLeft, dropdownTop, dropdownLeft + dropdownWidth, dropdownTop + dropdownHeight, 0xff202026); //Middle

			context.drawVerticalLine(dropdownLeft, dropdownTop, dropdownTop + dropdownHeight, 0xff2355ad);
			context.drawVerticalLine(dropdownLeft + dropdownWidth, dropdownTop, dropdownTop + dropdownHeight, 0xff2355ad);

			context.drawHorizontalLine(dropdownLeft, dropdownLeft + dropdownWidth, dropdownTop, 0xff2355ad);
			context.drawHorizontalLine(dropdownLeft, dropdownLeft + dropdownWidth, dropdownTop + dropdownHeight, 0xff2355ad);

			int dropdownY = 13;
			for (String value : values) {
				if (value.equals(selected)) {
					continue;
				}
				RenderUtils.renderTextWithMaxWidth(
						context,
						Text.of(value),
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
					Text.of(selected),
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
			Set<String> values = this.option.getPossibleValues();
			int dropdownHeight = 12 * values.size();

			if ((mouseX >= dropdownLeft) && (mouseX < (dropdownLeft + dropdownWidth))
					&& (mouseY >= dropdownTop) && (mouseX < (dropdownTop + dropdownHeight))) {

				String selected = this.option.getValue();
				List<String> selection = new ArrayList<>(values);
				selection.remove(selected);
				int index = (int) ((mouseY - dropdownTop) / 12) - 1;

				if (index == -1) {
					return true;
				}

				this.option.setValue(ExceptionHandler.removeThrows(() -> selection.get(index), selected));
				this.open = false;
				return true;
			}
		}


		this.open = ((mouseX >= dropdownLeft) && (mouseX < (dropdownLeft + dropdownWidth))
				&& (mouseY >= dropdownTop) && (mouseY < (dropdownTop + 14)));

		return super.mouseClicked(mouseX, mouseY, button, optionWidth);
	}

	@Override
	public boolean doesMatchSearch(String search) {
		return super.doesMatchSearch(search) || this.option.getPossibleValues().stream().anyMatch(key -> key.contains(search));
	}
}
