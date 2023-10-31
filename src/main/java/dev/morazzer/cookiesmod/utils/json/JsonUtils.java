package dev.morazzer.cookiesmod.utils.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;

import java.lang.reflect.Field;

/**
 * Various constants and methods related to json/gson.
 */
public class JsonUtils {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static final Gson CLEAN_GSON = new Gson();

    /**
     * Writes an object to a {@linkplain com.google.gson.JsonObject}.
     *
     * @param object The source object.
     * @return The json object.
     */
    public static JsonObject toJsonObject(Object object) {
        JsonObject jsonObject = new JsonObject();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.trySetAccessible();
            if (field.isAnnotationPresent(Save.class)) {
                jsonObject.add(field.getName(), ExceptionHandler.removeThrows(() -> toJsonObject(field.get(object))));
            } else {
                jsonObject.add(
                        field.getName(),
                        ExceptionHandler.removeThrows(() -> CLEAN_GSON.toJsonTree(field.get(object)))
                );
            }
        }
        return jsonObject;
    }

    /**
     * Parses an instance of an object from a {@linkplain com.google.gson.JsonObject} but keep defaults if not present.
     *
     * @param instance   The instance of the object with defaults.
     * @param jsonObject The json object to parse from.
     * @param <T>        The type of the object.
     * @return The object with modified fields.
     */
    public static <T> T fromJson(T instance, JsonObject jsonObject) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            field.trySetAccessible();
            if (field.isAnnotationPresent(Save.class)) {
                if (!jsonObject.has(field.getName())) continue;
                fromJson(
                        ExceptionHandler.removeThrows(() -> field.get(instance)),
                        jsonObject.get(field.getName()).getAsJsonObject()
                );
            } else {
                if (!jsonObject.has(field.getName())) continue;
                try {
                    field.set(instance, CLEAN_GSON.fromJson(jsonObject.get(field.getName()), field.getType()));
                } catch (IllegalAccessException e) {
                    ExceptionHandler.handleException(new RuntimeException(field
                            .getDeclaringClass()
                            .getName() + "#" + field.getName(), e));
                }
            }
        }
        return instance;
    }

}
