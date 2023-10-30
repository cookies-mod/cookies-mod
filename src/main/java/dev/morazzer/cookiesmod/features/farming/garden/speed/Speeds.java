package dev.morazzer.cookiesmod.features.farming.garden.speed;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.options.SliderOption;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Speeds for farming.
 */
public class Speeds extends Foldable {

    @Expose
    public final SliderOption<Integer> wheat = SliderOption.integerOption(
            Text.literal("Wheat"),
            Text.literal("Overrides the preset value for wheat (-1 to keep preset)"),
            -1
    ).withMax(500).withMin(-1).withStep(1);
    @Expose
    public final SliderOption<Integer> carrot = SliderOption.integerOption(
            Text.literal("Carrot"),
            Text.literal("Overrides the preset value for carrots (-1 to keep preset)"),
            -1
    ).withMax(500).withMin(-1).withStep(1);
    @Expose
    public final SliderOption<Integer> potato = SliderOption.integerOption(
            Text.literal("Potato"),
            Text.literal("Overrides the preset value for potatoes (-1 to keep preset)"),
            -1
    ).withMax(500).withMin(-1).withStep(1);
    @Expose
    public final SliderOption<Integer> netherWart = SliderOption.integerOption(
            Text.literal("Nether Wart"),
            Text.literal("Overrides the preset value for nether warts (-1 to keep preset)"),
            -1
    ).withMax(500).withMin(-1).withStep(1);
    @Expose
    public final SliderOption<Integer> pumpkin = SliderOption.integerOption(
            Text.literal("Pumpkin"),
            Text.literal("Overrides the preset value for pumpkins (-1 to keep preset)"),
            -1
    ).withMax(500).withMin(-1).withStep(1);
    @Expose
    public final SliderOption<Integer> melon = SliderOption.integerOption(
            Text.literal("Melon"),
            Text.literal("Overrides the preset value for melons (-1 to keep preset)"),
            -1
    ).withMax(500).withMin(-1).withStep(1);
    @Expose
    public final SliderOption<Integer> cocoaBeans = SliderOption.integerOption(
            Text.literal("Cocoa Beans"),
            Text.literal("Overrides the preset value for cocoa beans (-1 to keep preset)"),
            -1
    ).withMax(500).withMin(-1).withStep(1);
    @Expose
    public final SliderOption<Integer> sugarCane = SliderOption.integerOption(
            Text.literal("Sugar Cane"),
            Text.literal("Overrides the preset value for sugar cane (-1 to keep preset)"),
            -1
    ).withMax(500).withMin(-1).withStep(1);
    @Expose
    public final SliderOption<Integer> cactus = SliderOption.integerOption(
            Text.literal("Cactus"),
            Text.literal("Overrides the preset value for cactus (-1 to keep preset)"),
            -1
    ).withMax(500).withMin(-1).withStep(1);
    @Expose
    public final SliderOption<Integer> mushroom = SliderOption.integerOption(
            Text.literal("Mushroom"),
            Text.literal("Overrides the preset value for mushrooms (-1 to keep preset)"),
            -1
    ).withMax(500).withMin(-1).withStep(1);

    public Speeds(
            int wheat,
            int carrot,
            int potato,
            int netherWart,
            int pumpkin,
            int melon,
            int cocoaBeans,
            int sugarCane,
            int cactus,
            int mushroom
    ) {
        this.wheat.setValue(wheat);
        this.carrot.setValue(carrot);
        this.potato.setValue(potato);
        this.netherWart.setValue(netherWart);
        this.pumpkin.setValue(pumpkin);
        this.melon.setValue(melon);
        this.cocoaBeans.setValue(cocoaBeans);
        this.sugarCane.setValue(sugarCane);
        this.cactus.setValue(cactus);
        this.mushroom.setValue(mushroom);
    }

    public Speeds() {
    }

    public static Speeds merge(Speeds preset, Speeds config) {
        if (preset.equals(config)) return preset;

        return new SpeedsBuilder()
                .setWheat(getValue(preset.wheat, config.wheat))
                .setCarrot(getValue(preset.carrot, config.carrot))
                .setPotato(getValue(preset.potato, config.potato))
                .setNetherWart(getValue(preset.netherWart, config.netherWart))
                .setPumpkin(getValue(preset.pumpkin, config.pumpkin))
                .setMelon(getValue(preset.melon, config.melon))
                .setCocoaBeans(getValue(preset.cocoaBeans, config.cocoaBeans))
                .setSugarCane(getValue(preset.sugarCane, config.sugarCane))
                .setCactus(getValue(preset.cactus, config.cactus))
                .setMushroom(getValue(preset.mushroom, config.mushroom))
                .createSpeeds();
    }

    private static int getValue(SliderOption<? extends Number> preset, SliderOption<? extends Number> config) {
        return config.getValue().intValue() != -1 ? config.getValue().intValue() : preset.getValue().intValue();
    }

    public int getValue(Identifier identifier) {
        return switch (identifier.getPath()) {
            case "items/wheat" -> this.wheat.getValue();
            case "items/carrot_item" -> this.carrot.getValue();
            case "items/potato_item" -> this.potato.getValue();
            case "items/nether_stalk" -> this.netherWart.getValue();
            case "items/pumpkin" -> this.pumpkin.getValue();
            case "items/melon" -> this.melon.getValue();
            case "items/ink_sack_3" -> this.cocoaBeans.getValue();
            case "items/sugar_cane" -> this.sugarCane.getValue();
            case "items/cactus" -> this.cactus.getValue();
            case "items/huge_mushroom_2" -> this.mushroom.getValue();
            default -> throw new IllegalStateException("Unexpected value: " + identifier.getPath());
        };
    }

    public Text getName() {
        return Text.literal("Custom Speeds");
    }

}
