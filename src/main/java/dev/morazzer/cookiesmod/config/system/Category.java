package dev.morazzer.cookiesmod.config.system;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import java.lang.reflect.Field;
import java.util.Optional;
import net.minecraft.text.Text;

/**
 * A category in the config.
 */
public abstract class Category {

    /**
     * The display name that will be displayed in the config.
     *
     * @return The name.
     */
    public abstract Text getName();

    /**
     * A small description of what is in this category.
     *
     * @return A description.
     */
    public abstract Text getDescription();

    /**
     * Loads the values of the options in the category from a {@linkplain com.google.gson.JsonObject}.
     *
     * @param jsonObject The json object.
     */
    public final void load(JsonElement jsonObject) {
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (Optional.ofNullable(declaredField.getType().getSuperclass()).map(Option.class::equals).orElse(false)) {
                Option<?, ?> o = (Option<?, ?>) ExceptionHandler.removeThrows(() -> declaredField.get(this));
                if (!o.canBeSerialized()) {
                    continue;
                }
                if (!jsonObject.getAsJsonObject().has(declaredField.getName())) {
                    continue;
                }
                o.load(jsonObject.getAsJsonObject().get(declaredField.getName()));
            } else if (Optional
                .ofNullable(declaredField.getType().getSuperclass())
                .map(Foldable.class::equals)
                .orElse(false)) {
                Foldable foldable = (Foldable) ExceptionHandler.removeThrows(() -> declaredField.get(this));
                if (!jsonObject.getAsJsonObject().has(declaredField.getName())) {
                    continue;
                }
                foldable.load(jsonObject.getAsJsonObject().get(declaredField.getName()));
            }
        }
    }

    /**
     * Saves the values of the current category to a {@linkplain com.google.gson.JsonObject}.
     *
     * @return The json object.
     */
    public final JsonElement save() {
        JsonObject jsonObject = new JsonObject();
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (Optional.ofNullable(declaredField.getType().getSuperclass()).map(Option.class::equals).orElse(false)) {
                Option<?, ?> o = (Option<?, ?>) ExceptionHandler.removeThrows(() -> declaredField.get(this));
                if (!o.canBeSerialized()) {
                    continue;
                }

                jsonObject.add(declaredField.getName(), o.save());
            } else if (Optional
                .ofNullable(declaredField.getType().getSuperclass())
                .map(Foldable.class::equals)
                .orElse(false)) {
                Foldable foldable = (Foldable) ExceptionHandler.removeThrows(() -> declaredField.get(this));
                jsonObject.add(declaredField.getName(), foldable.save());
            }
        }
        return jsonObject;
    }

}
