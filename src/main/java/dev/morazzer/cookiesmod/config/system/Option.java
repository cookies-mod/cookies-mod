package dev.morazzer.cookiesmod.config.system;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public abstract class Option<T, O extends Option<T, O>> {

	private final Text name;
	private final Text description;
	protected T value;
	private List<String> hiddenKeys = new ArrayList<>();
	protected List<ValueChangeCallback<T>> callbacks = new ArrayList<>();

	public Option(Text name, Text description, T value) {
		this.name = name;
		this.description = description;
		this.value = value;
	}

	public void setValue(T value) {
		T oldValue = this.value;
		this.value = value;
		this.updateCallbacks(oldValue);
	}

	protected void updateCallbacks(T oldValue) {
		this.callbacks.forEach(callbacks -> callbacks.valueChanged(oldValue, this.value));
	}

	public O withHiddenKey(String key) {
		return this.withHiddenKeys(key);
	}

	public O withHiddenKeys(String... keys) {
		this.hiddenKeys.addAll(Arrays.asList(keys));
		//noinspection unchecked
		return (O) this;
	}

	public abstract void load(JsonElement jsonElement);

	public abstract JsonElement save();

	public abstract ConfigOptionEditor<T, O> getEditor();

	public boolean canBeSerialized() {
		return true;
	}

	public O withCallback(ValueChangeCallback<T> valueChangeCallback) {
		this.callbacks.add(valueChangeCallback);
		//noinspection unchecked
		return (O) this;
	}

	public O asOption() {
		//noinspection unchecked
		return (O) this;
	}

	@FunctionalInterface
	public interface ValueChangeCallback<T> {
		void valueChanged(T oldValue, T newValue);
	}
}
