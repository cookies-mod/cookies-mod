package dev.morazzer.cookiesmod.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class GsonUtils {

    public static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static Gson gsonClean = new Gson();

    public static JsonObject emptyObject = gson.fromJson("{}", JsonObject.class);

}
