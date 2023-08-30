package dev.morazzer.cookiesmod.features.garden.speed;

public class SpeedsBuilder {
	private int wheat;
	private int carrot;
	private int potato;
	private int netherWart;
	private int pumpkin;
	private int melon;
	private int cocoaBeans;
	private int sugarCane;
	private int cactus;
	private int mushroom;

	public SpeedsBuilder setWheat(int wheat) {
		this.wheat = wheat;
		return this;
	}

	public SpeedsBuilder setCarrot(int carrot) {
		this.carrot = carrot;
		return this;
	}

	public SpeedsBuilder setPotato(int potato) {
		this.potato = potato;
		return this;
	}

	public SpeedsBuilder setNetherWart(int netherWart) {
		this.netherWart = netherWart;
		return this;
	}

	public SpeedsBuilder setPumpkin(int pumpkin) {
		this.pumpkin = pumpkin;
		return this;
	}

	public SpeedsBuilder setMelon(int melon) {
		this.melon = melon;
		return this;
	}

	public SpeedsBuilder setCocoaBeans(int cocoaBeans) {
		this.cocoaBeans = cocoaBeans;
		return this;
	}

	public SpeedsBuilder setSugarCane(int sugarCane) {
		this.sugarCane = sugarCane;
		return this;
	}

	public SpeedsBuilder setCactus(int cactus) {
		this.cactus = cactus;
		return this;
	}

	public SpeedsBuilder setMushroom(int mushroom) {
		this.mushroom = mushroom;
		return this;
	}

	public Speeds createSpeeds() {
		return new Speeds(wheat, carrot, potato, netherWart, pumpkin, melon, cocoaBeans, sugarCane, cactus, mushroom);
	}
}