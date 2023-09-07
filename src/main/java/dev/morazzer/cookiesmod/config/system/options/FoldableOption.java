package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.config.system.editor.FoldableEditor;
import net.minecraft.text.Text;

public class FoldableOption extends Option<Object, FoldableOption> {
	private final int id;

	public FoldableOption(Foldable foldable, int id) {
		super(foldable.getName(), Text.empty(), foldable);
		this.id = id;
	}

	@Override
	public void load(JsonElement jsonElement) {

	}

	@Override
	public JsonElement save() {
		return null;
	}

	@Override
	public ConfigOptionEditor<Object, FoldableOption> getEditor() {
		return new FoldableEditor(this, id);
	}
}
