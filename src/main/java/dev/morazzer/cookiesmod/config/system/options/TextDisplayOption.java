package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.config.system.editor.TextDisplayEditor;
import net.minecraft.text.Text;

public class TextDisplayOption extends Option<Object, TextDisplayOption> {
	public TextDisplayOption(Text name, Text description) {
		super(name, description, null);
	}

	@Override
	public boolean canBeSerialized() {
		return false;
	}

	@Override
	public void load(JsonElement jsonElement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public JsonElement save() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ConfigOptionEditor<Object, TextDisplayOption> getEditor() {
		return new TextDisplayEditor(this);
	}
}
