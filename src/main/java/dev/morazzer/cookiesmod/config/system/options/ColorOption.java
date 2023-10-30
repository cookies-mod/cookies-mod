package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ColorEditor;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * Option to select a color in the config.
 */
@Slf4j
@Getter
public class ColorOption extends Option<Color, ColorOption> {

    private boolean allowAlpha;

    public ColorOption(Text name, Text description, Color value) {
        super(name, description, value);
    }

    /**
     * Allows the color to have an alpha value. By default, this will be off.
     *
     * @return The option.
     */
    public ColorOption withAlpha() {
        this.allowAlpha = true;
        return this;
    }

    @Override
    public void load(@NotNull JsonElement jsonElement) {
        if (!jsonElement.isJsonPrimitive()) {
            log.warn("Error while loading config value, expected number got %s".formatted(jsonElement.isJsonObject() ? "json-object" : "json-array"));
            return;
        }
        if (!jsonElement.getAsJsonPrimitive().isNumber()) {
            log.warn("Error while loading config value, expected number got %s".formatted(jsonElement.getAsString()));
            return;
        }
        int argb = jsonElement.getAsInt();
        this.value = new Color(argb, allowAlpha);
    }

    @Override
    public @NotNull JsonElement save() {
        return new JsonPrimitive(this.value.getRGB() | this.value.getAlpha() << 24);
    }

    @Override
    public @NotNull ConfigOptionEditor<Color, ColorOption> getEditor() {
        return new ColorEditor(this);
    }

}
