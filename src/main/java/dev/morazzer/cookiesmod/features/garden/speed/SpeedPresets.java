package dev.morazzer.cookiesmod.features.garden.speed;

import lombok.Getter;

@Getter
public enum SpeedPresets {

	NORMAL(
			new SpeedsBuilder()
					.setWheat(355)
					.setCarrot(355)
					.setPotato(355)
					.setNetherWart(93)
					.setPumpkin(258)
					.setMelon(258)
					.setCocoaBeans(155)
					.setSugarCane(327)
					.setCactus(400)
					.setMushroom(200)
					.createSpeeds()
	),
	MEGA(
			new SpeedsBuilder()
					.setWheat(93)
					.setCarrot(93)
					.setPotato(93)
					.setNetherWart(93)
					.setPumpkin(155)
					.setMelon(155)
					.setCocoaBeans(155)
					.setSugarCane(328)
					.setCactus(500)
					.setMushroom(233)
					.createSpeeds()
	);

	private final Speeds speeds;

	SpeedPresets(Speeds speeds) {
		this.speeds = speeds;
	}

}
