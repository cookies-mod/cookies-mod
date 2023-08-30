package dev.morazzer.cookiesmod.features.garden.speed;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorSlider;
import io.github.moulberry.moulconfig.annotations.ConfigOption;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Speeds {
	public Speeds(int wheat, int carrot, int potato, int nether_wart, int pumpkin, int melon, int cocoa_beans, int sugar_cane, int cactus, int mushroom) {
		this.wheat = wheat;
		this.carrot = carrot;
		this.potato = potato;
		this.nether_wart = nether_wart;
		this.pumpkin = pumpkin;
		this.melon = melon;
		this.cocoa_beans = cocoa_beans;
		this.sugar_cane = sugar_cane;
		this.cactus = cactus;
		this.mushroom = mushroom;
	}

	public Speeds() {

	}

	public static Speeds merge(Speeds preset, Speeds config) {
		if (preset.equals(config)) return preset;

		return new SpeedsBuilder()
				.setWheat(getValue(preset.wheat, config.wheat))
				.setCarrot(getValue(preset.carrot, config.carrot))
				.setPotato(getValue(preset.potato, config.potato))
				.setNetherWart(getValue(preset.nether_wart, config.nether_wart))
				.setPumpkin(getValue(preset.pumpkin, config.pumpkin))
				.setMelon(getValue(preset.melon, config.melon))
				.setCocoaBeans(getValue(preset.cocoa_beans, config.cocoa_beans))
				.setSugarCane(getValue(preset.sugar_cane, config.sugar_cane))
				.setCactus(getValue(preset.cactus, config.cactus))
				.setMushroom(getValue(preset.mushroom, config.mushroom))
				.createSpeeds();
	}

	public int getValue(Identifier identifier) {
		return switch (identifier.getPath()) {
			case "item/wheat" -> this.wheat;
			case "item/carrot_item" -> this.carrot;
			case "item/potato_item" -> this.potato;
			case "item/nether_stalk" -> this.nether_wart;
			case "item/pumpkin" -> this.pumpkin;
			case "item/melon" -> this.melon;
			case "item/ink_sack_3" -> this.cocoa_beans;
			case "item/sugar_cane" -> this.sugar_cane;
			case "item/cactus" -> this.cactus;
			case "item/huge_mushroom_2" -> this.mushroom;
			default -> throw new IllegalStateException("Unexpected value: " + identifier.getPath());
		};
	}

	private static int getValue(int preset, int config) {
		return config != -1 ? config : preset;
	}


	@Expose
	@ConfigOption(name = "Wheat", description = "Overrides the preset value for wheat (-1 to keep preset)")
	@ConfigEditorSlider(maxValue = 500, minValue = -1, minStep = 1)
	public int wheat = -1;
	@Expose
	@ConfigOption(name = "Carrot", description = "Overrides the preset value for carrots (-1 to keep preset)")
	@ConfigEditorSlider(maxValue = 500, minValue = -1, minStep = 1)
	public int carrot = -1;
	@Expose
	@ConfigOption(name = "Potato", description = "Overrides the preset value for potatoes (-1 to keep preset)")
	@ConfigEditorSlider(maxValue = 500, minValue = -1, minStep = 1)
	public int potato = -1;
	@Expose
	@ConfigOption(name = "Nether Wart", description = "Overrides the preset value for nether warts (-1 to keep preset)")
	@ConfigEditorSlider(maxValue = 500, minValue = -1, minStep = 1)
	public int nether_wart = -1;
	@Expose
	@ConfigOption(name = "Pumpkin", description = "Overrides the preset value for pumpkins (-1 to keep preset)")
	@ConfigEditorSlider(maxValue = 500, minValue = -1, minStep = 1)
	public int pumpkin = -1;
	@Expose
	@ConfigOption(name = "Melon", description = "Overrides the preset value for melons (-1 to keep preset)")
	@ConfigEditorSlider(maxValue = 500, minValue = -1, minStep = 1)
	public int melon = -1;
	@Expose
	@ConfigOption(name = "Cocoa Beans", description = "Overrides the preset value for cocoa beans (-1 to keep preset)")
	@ConfigEditorSlider(maxValue = 500, minValue = -1, minStep = 1)
	public int cocoa_beans = -1;
	@Expose
	@ConfigOption(name = "Sugar Cane", description = "Overrides the preset value for sugar cane (-1 to keep preset)")
	@ConfigEditorSlider(maxValue = 500, minValue = -1, minStep = 1)
	public int sugar_cane = -1;
	@Expose
	@ConfigOption(name = "Cactus", description = "Overrides the preset value for cactus (-1 to keep preset)")
	@ConfigEditorSlider(maxValue = 500, minValue = -1, minStep = 1)
	public int cactus = -1;
	@Expose
	@ConfigOption(name = "Mushroom", description = "Overrides the preset value for mushrooms (-1 to keep preset)")
	@ConfigEditorSlider(maxValue = 500, minValue = -1, minStep = 1)
	public int mushroom = -1;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		Speeds speeds = (Speeds) o;

		return new EqualsBuilder().append(wheat, speeds.wheat).append(carrot, speeds.carrot).append(potato, speeds.potato).append(nether_wart, speeds.nether_wart).append(pumpkin, speeds.pumpkin).append(melon, speeds.melon).append(cocoa_beans, speeds.cocoa_beans).append(sugar_cane, speeds.sugar_cane).append(cactus, speeds.cactus).append(mushroom, speeds.mushroom).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(wheat).append(carrot).append(potato).append(nether_wart).append(pumpkin).append(melon).append(cocoa_beans).append(sugar_cane).append(cactus).append(mushroom).toHashCode();
	}
}
