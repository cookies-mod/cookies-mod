package dev.morazzer.cookiesmod.config.categories.about;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * The option that describes the {@linkplain dev.morazzer.cookiesmod.config.categories.about.credits.Credits} section.
 */
public class AboutOption extends Option<Object, AboutOption> {

    public AboutOption() {
        super(Text.empty(), Text.empty(), null);
    }

    @Override
    public void load(@NotNull JsonElement jsonElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull JsonElement save() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull ConfigOptionEditor<Object, AboutOption> getEditor() {
        return new AboutEditor(this);
    }

    @Override
    public boolean canBeSerialized() {
        return false;
    }

}
