package dev.morazzer.cookiesmod.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;
import dev.morazzer.cookiesmod.features.repository.constants.CompostUpgradeCost;
import dev.morazzer.cookiesmod.features.repository.items.recipe.Ingredient;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.util.Identifier;

/**
 * Various constants and methods related to json/gson.
 */
public class JsonUtils {

    public static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Identifier.class,
            (JsonDeserializer<Identifier>) (json, typeOfT, context) -> new Identifier(json.getAsString()))
        .registerTypeAdapter(Ingredient.class,
            (JsonDeserializer<Ingredient>) (json, typeOfT, context) -> new Ingredient(json.getAsString()))
        .registerTypeAdapter(Identifier.class,
            (JsonSerializer<Identifier>) (src, typeOfSrc, context) -> context.serialize(src.toString()))
        .registerTypeAdapter(CompostUpgradeCost.CompostUpgrade.class,
            (JsonDeserializer<CompostUpgradeCost.CompostUpgrade>) CompostUpgradeCost.CompostUpgrade::deserialize)
        .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
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
                if (!jsonObject.has(field.getName())) {
                    continue;
                }
                fromJson(
                    ExceptionHandler.removeThrows(() -> field.get(instance)),
                    jsonObject.get(field.getName()).getAsJsonObject()
                );
            } else {
                if (!jsonObject.has(field.getName())) {
                    continue;
                }
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

    /**
     * Parses a {@linkplain com.google.gson.JsonElement} to a collection.
     *
     * @param jsonElement The json element.
     * @param creator     The creator of the collection.
     * @param mapper      The mapper to map the json element to the collection type.
     * @param <R>         The type of the collection.
     * @param <T>         The type in the collection.
     * @return The mapped collection.
     */
    public static <R extends Collection<T>, T> R fromJson(JsonElement jsonElement, Supplier<R> creator,
                                                          Function<JsonElement, T> mapper) {
        if (!jsonElement.isJsonArray()) {
            throw new IllegalArgumentException("Json element is not an array");
        }
        R collection = creator.get();
        for (JsonElement element : jsonElement.getAsJsonArray()) {
            collection.add(mapper.apply(element));
        }
        return collection;
    }

    /**
     * Parses a {@linkplain com.google.gson.JsonElement} to a map.
     *
     * @param jsonElement The json element.
     * @param creator     The creator of the map.
     * @param keyMapper   The mapper to map the key.
     * @param valueMapper The mapper to map the value.
     * @param <R>         The type of the map.
     * @param <K>         The type of the key.
     * @param <V>         The type of the value.
     * @return The mapped map.
     */
    public static <R extends Map<K, V>, K, V> R fromJson(JsonElement jsonElement, Supplier<R> creator,
                                                         Function<String, K> keyMapper,
                                                         Function<JsonElement, V> valueMapper) {
        if (!jsonElement.isJsonObject()) {
            throw new IllegalArgumentException("Json element is not an object");
        }
        R map = creator.get();
        for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
            map.put(keyMapper.apply(entry.getKey()), valueMapper.apply(entry.getValue()));
        }
        return map;
    }

    /**
     * Create a type token with an arbitrary type.
     *
     * @param <T> The type.
     * @return The type token.
     */
    public static <T> TypeToken<T> getTypeToken() {
        return new TypeToken<>() {
        };
    }

}
