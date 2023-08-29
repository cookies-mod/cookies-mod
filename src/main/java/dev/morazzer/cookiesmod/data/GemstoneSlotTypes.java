package dev.morazzer.cookiesmod.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GemstoneSlotTypes {
	COMBAT("\u2694"),
	DEFENSIVE("\u2624"),
	AMETHYST("\u2748"),
	JADE("\u2618"),
	JASPER("\u2741"),
	OFFENSIVE("\u2620"),
	SAPPHIRE("\u270E"),
	AMBER("\u2E15"),
	TOPAZ("\u2727"),
	RUBY("\u2764"),
	UNIVERSAL("\u2742"),
	MINING("\u2726"),
	OPAL("\u2742");

	private final String icon;
}
