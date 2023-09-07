package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.config.system.editor.StringDropdownEditor;
import joptsimple.internal.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.text.Text;

import java.util.Set;

@Getter
@Slf4j
public class StringDropdownOption extends Option<String, StringDropdownOption> {
	private final Set<String> possibleValues;

	public StringDropdownOption(Text name, Text description, String value, String... possibleValues) {
		super(name, description, value);
		this.possibleValues = Set.of(possibleValues);
	}

	@Override
	public void load(JsonElement jsonElement) {
		if (!jsonElement.isJsonPrimitive()) {
			log.warn("Error while loading config value, expected any of [%s] got %s".formatted(Strings.join(possibleValues, ", "), jsonElement.isJsonObject() ? "json-object" : "json-array"));
			return;
		}
		if (!jsonElement.getAsJsonPrimitive().isString()) {
			log.warn("Error while loading config value, expected any of [%s] got %s".formatted(Strings.join(possibleValues, ", "), jsonElement.getAsString()));
			return;
		}
		if (!possibleValues.contains(jsonElement.getAsString())) {
			log.warn("Error while loading config value, expected any of [%s] found %s".formatted(Strings.join(possibleValues, ", "), jsonElement.getAsString()));
			return;
		}
		this.value = jsonElement.getAsString();
	}

	@Override
	public JsonElement save() {
		return new JsonPrimitive(this.value);
	}

	@Override
	public ConfigOptionEditor<String, StringDropdownOption> getEditor() {
		return new StringDropdownEditor(this);
	}
}
