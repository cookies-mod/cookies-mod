package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ButtonEditor;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import lombok.Getter;
import net.minecraft.text.Text;

@Getter
public class ButtonOption extends Option<Runnable, ButtonOption> {
	private final Text buttonText;

	public ButtonOption(Text name, Text description, Runnable value, Text buttonText) {
		super(name, description, value);
		this.buttonText = buttonText;
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
	public ConfigOptionEditor<Runnable, ButtonOption> getEditor() {
		return new ButtonEditor(this);
	}
}
