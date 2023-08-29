package dev.morazzer.cookiesmod.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class DevCategory {

    @ConfigOption(name = "Show repo options", description = "Shows repo options in the /dev command", hiddenKeys = {"repo dev options item list"})
    @Expose
    @ConfigEditorBoolean
    public boolean displayRepoOption = true;

    @ConfigOption(name = "Hide spam from console log", description = "Hides all packet spam from log")
    @Expose
    @ConfigEditorBoolean
    public boolean hideSpam = true;

}
