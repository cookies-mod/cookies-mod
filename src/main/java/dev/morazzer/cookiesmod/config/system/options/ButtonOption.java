package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ButtonEditor;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import lombok.Getter;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A simple button in the config.
 */
@Getter
public class ButtonOption extends Option<Runnable, ButtonOption> {

    private final Text buttonText;

    public ButtonOption(Text name, Text description, Runnable value, Text buttonText) {
        super(name, description, value);
        this.buttonText = buttonText;
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
    public ConfigOptionEditor<Runnable, ButtonOption> getEditor() {
        return new ButtonEditor(this);
    }

    @Override
    public boolean canBeSerialized() {
        return false;
    }

}
