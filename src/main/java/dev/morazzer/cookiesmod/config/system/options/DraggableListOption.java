package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.config.system.editor.DraggableListEditor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class DraggableListOption extends Option<List<String>, DraggableListOption> {

	private ValueSupplier valueSupplier;
	private List<String> keys;

	public DraggableListOption(Text name, Text description, List<String> value) {
		super(name, description, value);
	}

	public DraggableListOption withValueSupplier(ValueSupplier valueSupplier) {
		this.valueSupplier = valueSupplier;
		return this;
	}

	public DraggableListOption withKeys(List<String> amount) {
		this.keys = amount;
		return this;
	}

	@Override
	public void load(JsonElement jsonElement) {
		if (!jsonElement.isJsonArray()) {
			log.warn("Error while loading config value, expected array got %s".formatted(jsonElement.isJsonObject() ? "json-object" : "json-primitive"));
			return;
		}
		this.value = new ArrayList<>();
		for (JsonElement element : jsonElement.getAsJsonArray()) {
			if (!element.isJsonPrimitive()) {
				log.warn("Skip bad value, expected string got %s".formatted(jsonElement.isJsonObject() ? "json-object" : "json-array"));
				continue;
			}
			if (!element.getAsJsonPrimitive().isString()) {
				log.warn("Skip bad value, expected string got %s".formatted(jsonElement.getAsString()));
				continue;
			}
			this.value.add(element.getAsString());
		}
	}

	@Override
	public JsonElement save() {
		JsonArray jsonArray = new JsonArray();
		this.value.forEach(jsonArray::add);
		return jsonArray;
	}

	@Override
	public ConfigOptionEditor<List<String>, DraggableListOption> getEditor() {
		return new DraggableListEditor(this);
	}

	@FunctionalInterface
	public interface ValueSupplier {
		Text getText(String key);
	}
}
