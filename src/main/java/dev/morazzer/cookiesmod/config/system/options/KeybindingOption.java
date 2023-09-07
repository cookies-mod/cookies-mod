package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.config.system.editor.KeybindingEditor;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

@Slf4j
public class KeybindingOption extends Option<InputUtil.Key, KeybindingOption> {
	InputUtil.Key defaultKey;

	public KeybindingOption(Text name, Text description, InputUtil.Key value) {
		super(name, description, value);
		this.defaultKey = value;
	}

	public KeybindingOption withDefault(InputUtil.Key key) {
		this.defaultKey = key;
		return this;
	}

	@Override
	public void load(JsonElement jsonElement) {
		//noinspection DuplicatedCode
		if (!jsonElement.isJsonPrimitive()) {
			log.warn("Error while loading config value, expected string got %s".formatted(jsonElement.isJsonObject() ? "json-object" : "json-array"));
			return;
		}
		if (!jsonElement.getAsJsonPrimitive().isString()) {
			log.warn("Error while loading config value, expected string got %s".formatted(jsonElement.getAsString()));
			return;
		}
		this.value = InputUtil.fromTranslationKey(jsonElement.getAsString());
	}

	@Override
	public JsonElement save() {
		return new JsonPrimitive(value.getTranslationKey());
	}

	@Override
	public ConfigOptionEditor<InputUtil.Key, KeybindingOption> getEditor() {
		return new KeybindingEditor(this);
	}
}
