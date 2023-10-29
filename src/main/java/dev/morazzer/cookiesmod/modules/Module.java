package dev.morazzer.cookiesmod.modules;

import dev.morazzer.cookiesmod.CookiesMod;
import net.minecraft.util.Identifier;

/**
 * A generic module to allow easy enabling.
 */
public interface Module {

    Identifier MODULE_ROOT = CookiesMod.ROOT.withPath("modules/");

    /**
     * Load a module.
     */
    void load();

    /**
     * Unload a module-
     */
    default void unload() {}

    /**
     * If the module should be loaded.
     *
     * @return If it should load.
     */
    default boolean shouldLoad() {
        return true;
    }

    /**
     * Get the identifier of the module.
     *
     * @return The identifier.
     */
    default Identifier getIdentifier() {
        return MODULE_ROOT.withSuffixedPath(getIdentifierPath());
    }

    /**
     * Get the identifier path.
     *
     * @return The identifier path.
     */
    String getIdentifierPath();

    /**
     * Reload a module.
     */
    default void reload() {
        unload();
        load();
    }

}
