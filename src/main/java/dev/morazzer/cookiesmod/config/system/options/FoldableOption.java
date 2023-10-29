package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.config.system.editor.FoldableEditor;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Option to represent a foldable in the compiled version of the config.
 */
public class FoldableOption extends Option<Object, FoldableOption> {

    private final int id;

    public FoldableOption(Foldable foldable, int id) {
        super(foldable.getName(), Text.empty(), foldable);
        this.id = id;
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
    public @NotNull ConfigOptionEditor<Object, FoldableOption> getEditor() {
        return new FoldableEditor(this, id);
    }

    @Override
    public boolean canBeSerialized() {
        return false;
    }

}
