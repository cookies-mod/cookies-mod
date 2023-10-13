package dev.morazzer.cookiesmod.screen.widgets;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class EnumCycleWidget<T extends Enum<T>, V> extends PressableWidget {
    private final IdentifierResolver<T> identifierResolver;
    private final TextSupplier<T> textSupplier;
    private final OnClick<T> onClick;
    private final List<Enum<T>> values;
    private final ValueSupplier<T, V> valueSupplier;
    @Getter
    @Setter
    private int enumIndex = 0;
    private final AtomicReference<Identifier> identifier = new AtomicReference<>();

    private final AtomicReference<List<Text>> tooltips = new AtomicReference<>();

    public EnumCycleWidget(
            int x,
            int y,
            int width,
            int height,
            Enum<T>[] values,
            IdentifierResolver<T> identifierResolver,
            TextSupplier<T> textSupplier,
            OnClick<T> onClick,
            ValueSupplier<T, V> valueSupplier) {
        super(x, y, width, height, Text.empty());
        this.identifierResolver = identifierResolver;
        this.textSupplier = textSupplier;
        this.onClick = onClick;
        this.values = Arrays.stream(values).sorted(Comparator.comparingInt(Enum::ordinal)).toList();
        this.valueSupplier = valueSupplier;
        this.constructTooltip();
        this.updateIdentifier();
    }

    AtomicInteger lastButton = new AtomicInteger();

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        lastButton.set(button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onPress() {
        switch (lastButton.get()) {
            case 0 -> enumIndex++;
            case 1 -> enumIndex--;
        }
        if (enumIndex > values.size() - 1) {
            enumIndex = 0;
        } else if (enumIndex < 0) {
            enumIndex = values.size() - 1;
        }
        this.onClick.click((T) values.get(enumIndex));
        this.constructTooltip();
        this.updateIdentifier();
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
    }

    @SuppressWarnings("unchecked")
    public void updateIdentifier() {
        this.identifier.set(this.identifierResolver.resolve((T) values.get(enumIndex)));
    }

    @SuppressWarnings("unchecked")
    public void constructTooltip() {
        this.tooltips.set(
                this.values.stream()
                        .map(tEnum -> (T) tEnum)
                        .map(t -> {
                            if (t.ordinal() == enumIndex) {
                                return textSupplier.resolve(t).copy();
                            } else {
                                return textSupplier.resolve(t).copy().formatted(Formatting.GRAY);
                            }
                        }).map(Text.class::cast)
                        .toList()
        );
    }

    @SuppressWarnings("unchecked")
    public V getValue() {
        return this.valueSupplier.getValue((T) values.get(enumIndex));
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderButton(context, mouseX, mouseY, delta);
        context.drawTexture(
                this.identifier.get(),
                this.getX() + 1,
                this.getY() + 1,
                0,
                0,
                this.getWidth() - 2,
                this.getHeight() - 2,
                this.getWidth() - 2,
                this.getHeight() - 2
        );
        if (super.isHovered()) {
            context.drawTooltip(
                    MinecraftClient.getInstance().textRenderer,
                    this.tooltips.get(),
                    mouseX,
                    mouseY
            );
        }
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }


    @FunctionalInterface
    public interface OnClick<T extends Enum<T>> {
        void click(T t);

        static <T extends Enum<T>> OnClick<T> identity(Runnable function) {
            return ignored -> function.run();
        }
    }

    @FunctionalInterface
    public interface IdentifierResolver<T extends Enum<T>> {
        Identifier resolve(T t);
    }

    @FunctionalInterface
    public interface TextSupplier<T extends Enum<T>> {
        MutableText resolve(T t);
    }

    @FunctionalInterface
    public interface ValueSupplier<T extends Enum<T>, V> {
        V getValue(T t);
    }
}
