package dev.morazzer.cookiesmod.features.farming.garden.speed;

/**
 * Builder class for the {@linkplain dev.morazzer.cookiesmod.features.farming.garden.speed.Speeds}.
 */
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

    /**
     * @param wheat The speed for wheat.
     * @return The builder.
     */
    public SpeedsBuilder setWheat(int wheat) {
        this.wheat = wheat;
        return this;
    }

    /**
     * @param carrot The speed for carrot.
     * @return The builder.
     */
    public SpeedsBuilder setCarrot(int carrot) {
        this.carrot = carrot;
        return this;
    }

    /**
     * @param potato The speed for potato.
     * @return The builder.
     */
    public SpeedsBuilder setPotato(int potato) {
        this.potato = potato;
        return this;
    }

    /**
     * @param netherWart The speed for nether wart.
     * @return The builder.
     */
    public SpeedsBuilder setNetherWart(int netherWart) {
        this.netherWart = netherWart;
        return this;
    }

    /**
     * @param pumpkin The speed for pumpkin.
     * @return The builder.
     */
    public SpeedsBuilder setPumpkin(int pumpkin) {
        this.pumpkin = pumpkin;
        return this;
    }

    /**
     * @param melon The speed for melon.
     * @return The builder.
     */
    public SpeedsBuilder setMelon(int melon) {
        this.melon = melon;
        return this;
    }

    /**
     * @param cocoaBeans The speed for cocoa beans.
     * @return The builder.
     */
    public SpeedsBuilder setCocoaBeans(int cocoaBeans) {
        this.cocoaBeans = cocoaBeans;
        return this;
    }

    /**
     * @param sugarCane The speed for sugar cane.
     * @return The builder.
     */
    public SpeedsBuilder setSugarCane(int sugarCane) {
        this.sugarCane = sugarCane;
        return this;
    }

    /**
     * @param cactus The speed for cactus.
     * @return The builder.
     */
    public SpeedsBuilder setCactus(int cactus) {
        this.cactus = cactus;
        return this;
    }

    /**
     * @param mushroom The speed for mushroom.
     * @return The builder.
     */
    public SpeedsBuilder setMushroom(int mushroom) {
        this.mushroom = mushroom;
        return this;
    }

    /**
     * Creates the {@linkplain dev.morazzer.cookiesmod.features.farming.garden.speed.Speeds} instance with the values provided.
     *
     * @return The new instance.
     */
    public Speeds createSpeeds() {
        return new Speeds(wheat, carrot, potato, netherWart, pumpkin, melon, cocoaBeans, sugarCane, cactus, mushroom);
    }

}