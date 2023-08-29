package dev.morazzer.cookiesmod.data;

import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.utils.ConcurrentUtils;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import lombok.Getter;

@Getter
public class WrappedSkyblockProfile {

    private final JsonObject jsonObject;
    private boolean doneLoading;
    private boolean profileValid;

    public WrappedSkyblockProfile(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        this.profileValid = jsonObject.has("profiles") && !jsonObject.has("error");
    }

    public void tryAsyncLoad() {
        ConcurrentUtils.execute(this::tryLoad);
    }


    public void tryLoad() {
        try {
            this.load();
        } catch (Exception exception) {
            this.profileValid = false;
            this.doneLoading = false;
            ExceptionHandler.handleException(exception);
        }
    }

    private void load() {


        this.doneLoading = true;
    }

    public boolean hasValidProfiles() {
        return this.profileValid;
    }

    public boolean isFullyLoaded() {
        return this.doneLoading;
    }
}
