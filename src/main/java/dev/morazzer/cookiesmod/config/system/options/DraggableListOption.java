package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.config.system.editor.DraggableListEditor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Draggable list in the config to allow rearranging of various items.
 */
@Slf4j
@Getter
public class DraggableListOption extends Option<List<String>, DraggableListOption> {

    private ValueSupplier valueSupplier;

    public DraggableListOption(Text name, Text description, List<String> value) {
        super(name, description, value);
    }

    /**
     * Adds a value supplier to the option to correctly map the strings to their respective display variant.
     *
     * @param valueSupplier The supplier for the {@linkplain net.minecraft.text.Text}.
     * @return The option.
     */
    public DraggableListOption withValueSupplier(ValueSupplier valueSupplier) {
        this.valueSupplier = valueSupplier;
        return this;
    }

    @Override
    public void load(@NotNull JsonElement jsonElement) {
        if (!jsonElement.isJsonArray()) {
            log.warn("Error while loading config value, expected array got %s".formatted(jsonElement.isJsonObject() ? "json-object" : "json-primitive"));
            return;
        }
        this.value = new ArrayList<>();
        for (JsonElement element : jsonElement.getAsJsonArray()) {
            if (!element.isJsonPrimitive()) {
                log.warn("Skip bad value, expected string got %s".formatted(jsonElement.isJsonObject() ? "json-object" : "json-array"));
                continue;
            }
            if (!element.getAsJsonPrimitive().isString()) {
                log.warn("Skip bad value, expected string got %s".formatted(jsonElement.getAsString()));
                continue;
            }
            this.value.add(element.getAsString());
        }
    }

    @Override
    public @NotNull JsonElement save() {
        JsonArray jsonArray = new JsonArray();
        this.value.forEach(jsonArray::add);
        return jsonArray;
    }

    @Override
    public @NotNull ConfigOptionEditor<List<String>, DraggableListOption> getEditor() {
        return new DraggableListEditor(this);
    }

    @FunctionalInterface
    public interface ValueSupplier {

        Text getText(String key);

    }

}
