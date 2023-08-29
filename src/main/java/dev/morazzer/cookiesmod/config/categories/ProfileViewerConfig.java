package dev.morazzer.cookiesmod.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class ProfileViewerConfig {

    @ConfigOption(name = "Keep last search", description = "Keeps the last viewed profile open when you open pv again", hiddenKeys = "")
    @Expose
    @ConfigEditorBoolean
    public boolean keepLastProfileOpen = false;

}
