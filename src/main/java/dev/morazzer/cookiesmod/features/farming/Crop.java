package dev.morazzer.cookiesmod.features.farming;

import lombok.Getter;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum Crop {
	WHEAT(new Identifier("skyblock:items/wheat")),
	CARROT(new Identifier("skyblock:items/carrot_item")),
	POTATO(new Identifier("skyblock:items/potato_item")),
	NETHER_WART(new Identifier("skyblock:items/nether_stalk")),
	PUMPKIN(new Identifier("skyblock:items/pumpkin")),
	MELON(new Identifier("skyblock:items/melon")),
	COCOA_BEANS(new Identifier("skyblock:items/ink_sack_3")),
	SUGAR_CANE(new Identifier("skyblock:items/sugar_cane")),
	CACTUS(new Identifier("skyblock:items/cactus")),
	MUSHROOM(new Identifier("skyblock:items/huge_mushroom_2"));

	private final Identifier identifier;
	private final String name;

	Crop(Identifier identifier) {
		this.identifier = identifier;
		this.name = StringUtils.capitalize(name().toLowerCase().replace("_", " ").trim());
	}

	public static Object byName(String name) {
		for (Crop value : values()) {
			if (value.getName().equalsIgnoreCase(name)) {
				return value;
			}
		}
		return null;
	}
}
