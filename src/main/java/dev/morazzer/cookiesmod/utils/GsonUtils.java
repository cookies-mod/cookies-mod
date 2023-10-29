package dev.morazzer.cookiesmod.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Static gson instances.
 */
public class GsonUtils {

    public static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static final Gson gsonClean = new Gson();

    public static final JsonObject emptyObject = new JsonObject();

}
