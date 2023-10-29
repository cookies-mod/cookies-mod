package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.config.system.editor.EnumDropdownEditor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * A dropdown menu in the config which gets its values from an enum instance.
 *
 * @param <T> The enum to get the values from.
 */
@Slf4j
@Getter
public class EnumDropdownOption<T extends Enum<T>> extends Option<T, EnumDropdownOption<T>> {

    private TextSupplier<T> textSupplier;

    public EnumDropdownOption(Text name, Text description, T value) {
        super(name, description, value);
        this.textSupplier = enumValue -> Text.literal(StringUtils.capitalize(enumValue
                .name()
                .replace('_', ' ')
                .toLowerCase()));
    }

    public EnumDropdownOption<T> withSupplier(TextSupplier<T> textSupplier) {
        this.textSupplier = textSupplier;
        return this;
    }

    @Override
    public void load(@NotNull JsonElement jsonElement) {
        if (!jsonElement.isJsonPrimitive()) {
            log.warn("Error while loading config value, expected string got %s".formatted(jsonElement.isJsonObject() ? "json-object" : "json-array"));
            return;
        }
        if (!jsonElement.getAsJsonPrimitive().isString()) {
            log.warn("Error while loading config value, expected string got %s".formatted(jsonElement.getAsString()));
            return;
        }
        //Can't fail under normal circumstances
        //noinspection unchecked
        this.value = (T) Enum.valueOf(this.value.getClass(), jsonElement.getAsString());
    }

    @Override
    public @NotNull JsonElement save() {
        return new JsonPrimitive(this.value.toString());
    }

    @Override
    public @NotNull ConfigOptionEditor<T, EnumDropdownOption<T>> getEditor() {
        return new EnumDropdownEditor<>(this);
    }

    @FunctionalInterface
    public interface TextSupplier<T extends Enum<T>> {

        Text supplyText(T value);

    }

}
