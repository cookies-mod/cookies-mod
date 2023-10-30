package dev.morazzer.cookiesmod.modules;

import dev.morazzer.cookiesmod.CookiesMod;
import net.minecraft.util.Identifier;

/**
 * A generic module to allow easy enabling.
 */
public interface Module {

    Identifier MODULE_ROOT = CookiesMod.ROOT.withPath("modules/");

    /**
     * Loads a module.
     */
    void load();

    /**
     * Unloads a module.
     */
    default void unload() {}

    /**
     * @return Whether the module should be loaded.
     */
    default boolean shouldLoad() {
        return true;
    }

    /**
     * Gets the identifier of the module.
     *
     * @return The identifier.
     */
    default Identifier getIdentifier() {
        return MODULE_ROOT.withSuffixedPath(getIdentifierPath());
    }

    /**
     * Gets the identifier path.
     *
     * @return The identifier path.
     */
    String getIdentifierPath();

    /**
     * Reloads a module.
     */
    default void reload() {
        unload();
        load();
    }

}
