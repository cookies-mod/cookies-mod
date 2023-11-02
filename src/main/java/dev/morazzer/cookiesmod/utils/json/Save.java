package dev.morazzer.cookiesmod.utils.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tells the {@linkplain dev.morazzer.cookiesmod.utils.json.JsonUtils#toJsonObject(Object)} and
 * {@linkplain dev.morazzer.cookiesmod.utils.json.JsonUtils#fromJson(Object, com.google.gson.JsonObject)} methods to not
 * use {@linkplain com.google.gson.Gson} for the annotated field.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Save {
}
