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

@Slf4j
@Getter
public class SliderOption<T extends Number> extends Option<T, SliderOption<T>> {
	private T min;
	private T max;
	private T step;
	private final NumberTransformer<T> numberTransformer;

	protected SliderOption(Text name, Text description, T value, NumberTransformer<T> numberTransformer) {
		super(name, description, value);
		this.numberTransformer = numberTransformer;
	}

	public SliderOption<T> withMin(T min) {
		this.min = min;
		return this;
	}

	public SliderOption<T> withMax(T max) {
		this.max = max;
		return this;
	}

	public SliderOption<T> withStep(T step) {
		this.step = step;
		return this;
	}

	@Override
	public void load(JsonElement jsonElement) {
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
	public JsonElement save() {
		return new JsonPrimitive(this.value);
	}

	@Override
	public ConfigOptionEditor<T, SliderOption<T>> getEditor() {
		return new SliderEditor<>(this);
	}

	public <N extends Number> void setValue(N number) {
		super.setValue(this.numberTransformer.parseNumber(number));
	}

	@Contract("_, _, _ -> new")
	@SuppressWarnings("unused")
	public static @NotNull SliderOption<Integer> integerOption(Text name, Text description, Integer value) {
		return new SliderOption<>(name, description, value, Number::intValue);
	}

	@Contract("_, _, _ -> new")
	@SuppressWarnings("unused")
	public static SliderOption<Float> floatOption(Text name, Text description, Float value) {
		return new SliderOption<>(name, description, value, Number::floatValue);
	}

	@Contract("_, _, _ -> new")
	@SuppressWarnings("unused")
	public static SliderOption<Byte> byteOption(Text name, Text description, Byte value) {
		return new SliderOption<>(name, description, value, Number::byteValue);
	}

	@Contract("_, _, _ -> new")
	@SuppressWarnings("unused")
	public static SliderOption<Long> longOption(Text name, Text description, Long value) {
		return new SliderOption<>(name, description, value, Number::longValue);
	}

	@Contract("_, _, _ -> new")
	@SuppressWarnings("unused")
	public static SliderOption<Double> doubleOption(Text name, Text description, Double value) {
		return new SliderOption<>(name, description, value, Number::doubleValue);
	}

	@Contract("_, _, _ -> new")
	@SuppressWarnings("unused")
	public static SliderOption<Short> shortOption(Text name, Text description, Short value) {
		return new SliderOption<>(name, description, value, Number::shortValue);
	}

	@FunctionalInterface
	public interface NumberTransformer<T extends Number> {
		T parseNumber(Number number);
	}
}
