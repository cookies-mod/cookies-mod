package dev.morazzer.cookiesmod.config.categories.waypoints;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;

public class WaypointOption extends Option<Object, WaypointOption> {
    public WaypointOption() {
        super(Text.empty(), Text.empty(), null);
    }

    @Override
    public boolean canBeSerialized() {
        return false;
    }

    @Override
    @Contract("_->fail")
    public void load(JsonElement jsonElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Contract("->fail")
    public JsonElement save() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConfigOptionEditor<Object, WaypointOption> getEditor() {
        return new WaypointEditor(this);
    }
}
