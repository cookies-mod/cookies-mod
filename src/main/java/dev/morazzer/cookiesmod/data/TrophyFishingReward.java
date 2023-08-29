package dev.morazzer.cookiesmod.data;

public enum TrophyFishingReward {
	BRONZE("Novice"),
	SILVER("Adept"),
	GOLD("Expert"),
	DIAMOND("Master");

	private final String name;

	TrophyFishingReward(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
