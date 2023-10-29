package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.config.system.editor.SliderEditor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A slider option to select a number, between a fixed minimum and a fixed maximum, in the config.
 *
 * @param <T> The type of the number.
 */
@Slf4j
@Getter
public class SliderOption<T extends Number> extends Option<T, SliderOption<T>> {

    private final NumberTransformer<T> numberTransformer;
    private T min;
    private T max;
    private T step;

    protected SliderOption(Text name, Text description, T value, NumberTransformer<T> numberTransformer) {
        super(name, description, value);
        this.numberTransformer = numberTransformer;
    }

    /**
     * Create a new integer slider.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param value       The initial value.
     * @return The option.
     */
    @Contract("_, _, _ -> new")
    @SuppressWarnings("unused")
    public static @NotNull SliderOption<Integer> integerOption(Text name, Text description, Integer value) {
        return new SliderOption<>(name, description, value, Number::intValue);
    }

    /**
     * Create a new float slider.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param value       The initial value.
     * @return The option.
     */
    @Contract("_, _, _ -> new")
    @SuppressWarnings("unused")
    public static SliderOption<Float> floatOption(Text name, Text description, Float value) {
        return new SliderOption<>(name, description, value, Number::floatValue);
    }

    /**
     * Create a new byte slider.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param value       The initial value.
     * @return The option.
     */
    @Contract("_, _, _ -> new")
    @SuppressWarnings("unused")
    public static SliderOption<Byte> byteOption(Text name, Text description, Byte value) {
        return new SliderOption<>(name, description, value, Number::byteValue);
    }

    /**
     * Create a new long slider.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param value       The initial value.
     * @return The option.
     */
    @Contract("_, _, _ -> new")
    @SuppressWarnings("unused")
    public static SliderOption<Long> longOption(Text name, Text description, Long value) {
        return new SliderOption<>(name, description, value, Number::longValue);
    }

    /**
     * Create a new double slider.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param value       The initial value.
     * @return The option.
     */
    @Contract("_, _, _ -> new")
    @SuppressWarnings("unused")
    public static SliderOption<Double> doubleOption(Text name, Text description, Double value) {
        return new SliderOption<>(name, description, value, Number::doubleValue);
    }

    /**
     * Create a new short slider.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param value       The initial value.
     * @return The option.
     */
    @Contract("_, _, _ -> new")
    @SuppressWarnings("unused")
    public static SliderOption<Short> shortOption(Text name, Text description, Short value) {
        return new SliderOption<>(name, description, value, Number::shortValue);
    }

    /**
     * Set a minimum for the value, it can't get lower than this.
     *
     * @param min The minimum value.
     * @return The option.
     */
    public SliderOption<T> withMin(T min) {
        this.min = min;
        return this;
    }

    /**
     * Set a maximum for the value, it can't get higher than this.
     *
     * @param max The maximum value.
     * @return The option.
     */
    public SliderOption<T> withMax(T max) {
        this.max = max;
        return this;
    }

    /**
     * Set the step size for the slider, it will set the value to the next closest step.
     *
     * @param step The step size.
     * @return The option.
     */
    public SliderOption<T> withStep(T step) {
        this.step = step;
        return this;
    }

    /**
     * Set the value of the option.
     *
     * @param number The number to set it to.
     * @param <N>    The type of the number.
     */
    public <N extends Number> void setValue(N number) {
        super.setValue(this.numberTransformer.parseNumber(number));
    }

    @Override
    public void load(@NotNull JsonElement jsonElement) {
        if (!jsonElement.isJsonPrimitive()) {
            log.warn("Error while loading config value, expected number got %s".formatted(jsonElement.isJsonObject() ? "json-object" : "json-array"));
            return;
        }
        if (!jsonElement.getAsJsonPrimitive().isNumber()) {
            log.warn("Error while loading config value, expected number got %s".formatted(jsonElement.getAsString()));
            return;
        }
        //noinspection unchecked
        this.value = (T) jsonElement.getAsNumber();
    }

    @Override
    public @NotNull JsonElement save() {
        return new JsonPrimitive(this.value);
    }

    @Override
    public @NotNull ConfigOptionEditor<T, SliderOption<T>> getEditor() {
        if (this.max == null || this.min == null || this.step == null || this.value == null) {
            throw new UnsupportedOperationException("Cannot create editor for slider option with name \"%s\"".formatted(
                    this.getName().getString()));
        }
        return new SliderEditor<>(this);
    }

    @Override
    protected void updateCallbacks(T oldValue) {
        super.callbacks.forEach(callbacks -> callbacks.valueChanged(
                this.numberTransformer.parseNumber(oldValue),
                this.numberTransformer.parseNumber(this.value)
        ));
    }

    @FunctionalInterface
    public interface NumberTransformer<T extends Number> {

        T parseNumber(Number number);

    }

}
