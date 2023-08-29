package dev.morazzer.cookiesmod.data;

import lombok.Getter;

@Getter
public enum TrophyFishingReward {
	BRONZE("Novice"),
	SILVER("Adept"),
	GOLD("Expert"),
	DIAMOND("Master");

	private final String name;

	TrophyFishingReward(String name) {
		this.name = name;
	}

}
