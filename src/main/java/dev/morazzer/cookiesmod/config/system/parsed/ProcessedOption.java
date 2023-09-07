package dev.morazzer.cookiesmod.config.system.parsed;

import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ProcessedOption<T, O extends Option<T, O>> {

	private final Option<T, O> option;
	private final ConfigOptionEditor<T, O> editor;
	@Setter
	private int foldable = -1;

	public ProcessedOption(Option<T, O> option) {
		this.option = option;
		this.editor = this.option.getEditor();
	}
}
