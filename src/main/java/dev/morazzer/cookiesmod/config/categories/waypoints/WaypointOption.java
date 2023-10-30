package dev.morazzer.cookiesmod.config.categories.waypoints;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An option to add the waypoint editor into a category.
 */
public class WaypointOption extends Option<Object, WaypointOption> {

    public WaypointOption() {
        super(Text.empty(), Text.empty(), null);
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
    public @NotNull ConfigOptionEditor<Object, WaypointOption> getEditor() {
        return new WaypointEditor(this);
    }

    @Override
    public boolean canBeSerialized() {
        return false;
    }

}
