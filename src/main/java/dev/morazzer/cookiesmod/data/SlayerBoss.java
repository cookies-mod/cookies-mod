package dev.morazzer.cookiesmod.data;

import org.jetbrains.annotations.NotNull;

public enum SlayerBoss {
	ZOMBIE, WOLF, ENDERMAN, SPIDER, BLAZE;

	public static SlayerBoss valueOfIgnore(@NotNull String value) {
		return valueOf(value.toUpperCase());
	}

}
