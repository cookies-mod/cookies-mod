package dev.morazzer.cookiesmod.modules;

import dev.morazzer.cookiesmod.CookiesMod;
import net.minecraft.util.Identifier;

public interface Module {
	Identifier MODULE_ROOT = CookiesMod.ROOT.withPath("modules/");

	void load();

	void unload();

	default Identifier getIdentifier() {
		return MODULE_ROOT.withSuffixedPath(getIdentifierPath());
	}

	String getIdentifierPath();

	default void reload() {
		unload();
		load();
	}
}
