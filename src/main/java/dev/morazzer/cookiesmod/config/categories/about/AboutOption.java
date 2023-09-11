package dev.morazzer.cookiesmod.config.categories.about;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import net.minecraft.text.Text;

public class AboutOption extends Option<Object, AboutOption> {

	public AboutOption() {
		super(Text.empty(), Text.empty(), null);
	}

	@Override
	public void load(JsonElement jsonElement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean canBeSerialized() {
		return false;
	}

	@Override
	public JsonElement save() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ConfigOptionEditor<Object, AboutOption> getEditor() {
		return new AboutEditor(this);
	}
}
