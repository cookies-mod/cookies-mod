package dev.morazzer.cookiesmod.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class MainCategory {

    @ConfigOption(name = "Auto Updates", description = "Automatically checks for updates on startup", hiddenKeys = {"version"})
    @Expose
    @ConfigEditorBoolean
    public boolean autoUpdates = true;

}
