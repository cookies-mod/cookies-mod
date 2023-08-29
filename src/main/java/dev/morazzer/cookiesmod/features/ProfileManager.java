package dev.morazzer.cookiesmod.features;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.data.WrappedSkyblockProfile;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.HttpUtils;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ProfileManager {
    private static final LoadingCache<String, WrappedSkyblockProfile> sbProfileCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(CacheLoader.from(ProfileManager::requestSbProfiles));

    private static WrappedSkyblockProfile requestSbProfiles(String playerName) {
        AtomicReference<JsonObject> result = new AtomicReference<>(new JsonObject());

        HttpUtils.getResponse(URI.create("https://sky.shiiyu.moe/api/v2/profile/" + playerName), response -> {
            if (response == null || response.getStatusLine().getStatusCode() != 200) {
                return;
            }

            result.set(
                    GsonUtils.gson.fromJson(
                            ExceptionHandler.removeThrows(
                                    () -> new String(
                                            response.getEntity()
                                                    .getContent()
                                                    .readAllBytes()
                                    ),
                                    "{}"
                            ),
                            JsonObject.class
                    )
            );
        });

        WrappedSkyblockProfile skyblockProfile = new WrappedSkyblockProfile(result.get());

        return skyblockProfile;
    }

    public static WrappedSkyblockProfile getProfile(String playerName) {
        return ExceptionHandler.removeThrows(() -> sbProfileCache.get(playerName));
    }
}
