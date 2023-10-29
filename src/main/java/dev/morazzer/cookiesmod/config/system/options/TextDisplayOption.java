package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.config.system.editor.TextDisplayEditor;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Easy way to display text in the config.
 */
public class TextDisplayOption extends Option<Object, TextDisplayOption> {

    public TextDisplayOption(Text name, Text description) {
        super(name, description, null);
    }

    @Override
    @Contract("_->fail")
    public void load(@NotNull JsonElement jsonElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Contract("->fail")
    public @NotNull JsonElement save() {
        throw new UnsupportedOperationException();
    }

    @Override
    @NotNull
    public ConfigOptionEditor<Object, TextDisplayOption> getEditor() {
        return new TextDisplayEditor(this);
    }

    @Override
    public boolean canBeSerialized() {
        return false;
    }

}
