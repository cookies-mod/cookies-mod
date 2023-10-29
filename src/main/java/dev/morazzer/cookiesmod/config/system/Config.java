package dev.morazzer.cookiesmod.config.system;

import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * A config that can be displayed in-game.
 *
 * @param <T> The type of the config.
 */
public abstract class Config<T extends Config<T>> {

    /**
     * Save to config to a json object.
     *
     * @return The config as json object.
     */
    public JsonObject save() {
        JsonObject jsonObject = new JsonObject();

        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (Optional
                    .ofNullable(declaredField.getType().getSuperclass())
                    .map(Category.class::equals)
                    .orElse(false)) {
                Category category = (Category) ExceptionHandler.removeThrows(() -> declaredField.get(this));
                jsonObject.add(declaredField.getName(), category.save());
            }
        }

        return jsonObject;
    }

    /**
     * Load all categories and values from a json object.
     *
     * @param jsonObject The json object.
     */
    public void load(JsonObject jsonObject) {
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (Optional
                    .ofNullable(declaredField.getType().getSuperclass())
                    .map(Category.class::equals)
                    .orElse(false)) {
                Category category = (Category) ExceptionHandler.removeThrows(() -> declaredField.get(this));
                if (!jsonObject.has(declaredField.getName())) {
                    continue;
                }
                category.load(jsonObject.get(declaredField.getName()));
            }
        }
    }

    /**
     * Get the title of the config that will always be displayed.
     *
     * @return The title.
     */
    public abstract Text getTitle();

}
