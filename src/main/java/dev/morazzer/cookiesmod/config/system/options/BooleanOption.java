package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.BooleanEditor;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.text.Text;

/**
 * Used for simple toggle buttons in the config
 */
@Slf4j
public class BooleanOption extends Option<Boolean, BooleanOption> {

	public BooleanOption(Text name, Text description, Boolean value) {
		super(name, description, value);
	}

	@Override
	public void load(JsonElement jsonElement) {
		if (!jsonElement.isJsonPrimitive()) {
			log.warn("Error while loading config value, expected boolean got %s".formatted(jsonElement.isJsonObject() ? "json-object" : "json-array"));
			return;
		}
		if (!jsonElement.getAsJsonPrimitive().isBoolean()) {
			log.warn("Error while loading config value, expected boolean got %s".formatted(jsonElement.getAsString()));
			return;
		}
		this.value = jsonElement.getAsBoolean();
	}

	@Override
	public JsonElement save() {
		return new JsonPrimitive(this.value);
	}

	@Override
	public ConfigOptionEditor<Boolean, BooleanOption> getEditor() {
		return new BooleanEditor(this);
	}
}
