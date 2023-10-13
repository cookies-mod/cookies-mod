package dev.morazzer.cookiesmod.config.system.element;

import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.function.Function;

public class DropdownElement<T> {

    private final T[] elements;
    private final Function<T, Text> textSupplier;
    @Getter
    private boolean open = false;
    private T selected;
    public int selectedIndex;

    public void setSelected(T selected) {
        this.selected = selected;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == selected) {
                this.selectedIndex = i;
            }
        }
    }

    public DropdownElement(T[] elements, Function<T, Text> textSupplier) {
        this.elements = elements;
        this.textSupplier = textSupplier;
    }

    public void render(DrawContext drawContext, int dropdownWidth) {
        if (!open) {
            Text displaySelected = this.textSupplier.apply(selected);

            RenderUtils.renderRectangle(drawContext, 0, 0, dropdownWidth, 14, false);
            //RenderUtils.renderTextScaled(drawContext, Text.literal("▼"), 2, dropdownWidth - 10, top, 0xffb0b0b0, false);
            RenderUtils.renderTextWithMaxWidth(
                    drawContext,
                    displaySelected,
                    dropdownWidth - 16,
                    3,
                    3,
                    ~0,
                    false
            );
        }
    }

    public void renderOverlay(DrawContext context, int dropdownWidth) {
        if (open) {
            Text displaySelected = this.textSupplier.apply(selected);
            T[] values = this.elements;

            int dropdownHeight = 12 * values.length;
            context.fill(
                    0,
                    0,
                    dropdownWidth,
                    dropdownHeight,
                    0xff202026
            ); //Middle

            context.drawVerticalLine(0, 0, dropdownHeight, 0xff2355ad);
            context.drawVerticalLine(
                    dropdownWidth,
                    0,
                    dropdownHeight,
                    0xff2355ad
            );

            context.drawHorizontalLine(0, dropdownWidth, 0, 0xff2355ad);
            context.drawHorizontalLine(
                    0,
                    dropdownWidth,
                    dropdownHeight,
                    0xff2355ad
            );

            int dropdownY = 13;
            for (T value : values) {
                if (value == selected) {
                    continue;
                }
                RenderUtils.renderTextWithMaxWidth(
                        context,
                        this.textSupplier.apply(value),
                        dropdownWidth - 6,
                        3,
                        3 + dropdownY,
                        0xffb0b0b0,
                        false
                );
                dropdownY += 12;
            }

            RenderUtils.renderTextWithMaxWidth(
                    context,
                    displaySelected,
                    dropdownWidth - 16,
                    3,
                    3,
                    -1,
                    false
            );

        }
    }

    public T mouseClicked(double mouseX, double mouseY, int dropdownWidth) {
        if (this.open) {
            T[] values = this.elements;
            int dropdownHeight = 12 * values.length;

            if ((mouseX >= 0) && (mouseX < (dropdownWidth))
                    && (mouseY >= 0) && (mouseY < (dropdownHeight))) {

                T selected = this.selected;
                int tempIndex = (int) ((mouseY) / 12);

                if (tempIndex == 0) {
                    return selected;
                }

                if (tempIndex <= this.selectedIndex) {
                    tempIndex--;
                }

                int index = tempIndex;

                this.selected = ExceptionHandler.removeThrows(() -> values[index], selected);
                this.selectedIndex = index;
                this.open = false;
                return this.selected;
            }
            this.open = false;
            return null;
        }


        if (((mouseX >= 0) && (mouseX < (dropdownWidth))
                && (mouseY >= 0) && (mouseY < (14)))) {
            this.open = true;
            return null;
        }
        return null;
    }

}
