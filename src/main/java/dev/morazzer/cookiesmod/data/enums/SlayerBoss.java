package dev.morazzer.cookiesmod.data.enums;

import org.jetbrains.annotations.NotNull;

public enum SlayerBoss {
	ZOMBIE, WOLF, ENDERMAN, SPIDER, BLAZE;

	public static SlayerBoss valueOfIgnore(@NotNull String value) {
		return valueOf(value.toUpperCase());
	}

}
