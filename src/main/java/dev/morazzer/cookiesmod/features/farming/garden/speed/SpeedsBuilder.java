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
     * Set the speed for wheat.
     *
     * @param wheat The speed.
     * @return The builder.
     */
    public SpeedsBuilder setWheat(int wheat) {
        this.wheat = wheat;
        return this;
    }

    /**
     * Set the speed for carrot.
     *
     * @param carrot The speed.
     * @return The builder.
     */
    public SpeedsBuilder setCarrot(int carrot) {
        this.carrot = carrot;
        return this;
    }

    /**
     * Set the speed for potato.
     *
     * @param potato The speed.
     * @return The builder.
     */
    public SpeedsBuilder setPotato(int potato) {
        this.potato = potato;
        return this;
    }

    /**
     * Set the speed for nether wart.
     *
     * @param netherWart The speed.
     * @return The builder.
     */
    public SpeedsBuilder setNetherWart(int netherWart) {
        this.netherWart = netherWart;
        return this;
    }

    /**
     * Set the speed for pumpkin.
     *
     * @param pumpkin The speed.
     * @return The builder.
     */
    public SpeedsBuilder setPumpkin(int pumpkin) {
        this.pumpkin = pumpkin;
        return this;
    }

    /**
     * Set the speed for melon.
     *
     * @param melon The speed.
     * @return The builder.
     */
    public SpeedsBuilder setMelon(int melon) {
        this.melon = melon;
        return this;
    }

    /**
     * Set the speed for cocoa beans.
     *
     * @param cocoaBeans The speed.
     * @return The builder.
     */
    public SpeedsBuilder setCocoaBeans(int cocoaBeans) {
        this.cocoaBeans = cocoaBeans;
        return this;
    }

    /**
     * Set the speed for sugar cane.
     *
     * @param sugarCane The speed.
     * @return The builder.
     */
    public SpeedsBuilder setSugarCane(int sugarCane) {
        this.sugarCane = sugarCane;
        return this;
    }

    /**
     * Set the speed for cactus.
     *
     * @param cactus The speed.
     * @return The builder.
     */
    public SpeedsBuilder setCactus(int cactus) {
        this.cactus = cactus;
        return this;
    }

    /**
     * Set the speed for mushroom.
     *
     * @param mushroom The speed.
     * @return The builder.
     */
    public SpeedsBuilder setMushroom(int mushroom) {
        this.mushroom = mushroom;
        return this;
    }

    /**
     * Create the {@linkplain dev.morazzer.cookiesmod.features.farming.garden.speed.Speeds} instance with the values provided.
     *
     * @return The new instance.
     */
    public Speeds createSpeeds() {
        return new Speeds(wheat, carrot, potato, netherWart, pumpkin, melon, cocoaBeans, sugarCane, cactus, mushroom);
    }

}