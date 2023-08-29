package dev.morazzer.cookiesmod.data;

import lombok.Getter;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum Stats {
	PERCENT_FORMATTING("+##.###%;-##.###%"),


	ABILITY_DAMAGE_PERCENT(Formatting.RED, null, PERCENT_FORMATTING.formatting),
	ATTACK_SPEED(Formatting.RED, null, PERCENT_FORMATTING.formatting),
	BREAKING_POWER(Formatting.GRAY), // SPECIAL
	COMBAT_WISDOM(Formatting.GREEN),
	CRITICAL_CHANCE(Formatting.RED, "Crit Chance", PERCENT_FORMATTING.formatting),
	CRITICAL_DAMAGE(Formatting.RED, "Crit Damage", PERCENT_FORMATTING.formatting),
	DAMAGE(Formatting.RED),
	DEFENSE(Formatting.GREEN),
	FARMING_FORTUNE(Formatting.GREEN),
	FARMING_WISDOM(Formatting.GREEN),
	FEROCITY(Formatting.GREEN),
	FISHING_SPEED(Formatting.GREEN),
	FISHING_WISDOM(Formatting.GREEN),
	FORAGING_WISDOM(Formatting.GREEN),
	HEALTH(Formatting.GREEN),
	HEALTH_REGENERATION(Formatting.GREEN, "Health Regen"),
	INTELLIGENCE(Formatting.GREEN),
	MAGIC_FIND(Formatting.GREEN),
	MENDING(Formatting.GREEN),
	MINING_FORTUNE(Formatting.GREEN),
	MINING_SPEED(Formatting.GREEN),
	PET_LUCK(Formatting.GREEN),
	RIFT_DAMAGE(Formatting.GREEN),
	RIFT_HEALTH(Formatting.GREEN, "Hearts"),
	RIFT_INTELLIGENCE(Formatting.GREEN, "Intelligence"),
	RIFT_MANA_REGEN(Formatting.GREEN, "Mana Regen", PERCENT_FORMATTING.formatting),
	RIFT_TIME(Formatting.GREEN, null, "##.###s"),
	RIFT_WALK_SPEED(Formatting.GREEN, "Speed"),
	SEA_CREATURE_CHANCE(Formatting.RED, null, PERCENT_FORMATTING.formatting),
	STRENGTH(Formatting.RED),
	TRUE_DEFENSE(Formatting.GREEN),
	VITALITY(Formatting.GREEN),
	WALK_SPEED(Formatting.GREEN, "Speed"),
	WEAPON_ABILITY_DAMAGE(Formatting.DARK_RED, true);


	private final Formatting color;
	private final String displayName;
	private final String formatting;
	private final boolean hidden;

	Stats(String formatting) {
		this.formatting = formatting;
		this.color = null;
		this.displayName = "";
		this.hidden = true;
	}

	Stats(Formatting color) {
		this(color, false);
	}

	Stats(Formatting color, boolean hidden) {
		this(color, null, hidden);
	}

	Stats(Formatting color, String displayName) {
		this(color, displayName, false);
	}

	Stats(Formatting color, String displayName, boolean hidden) {
		this(color, displayName, null, hidden);
	}

	Stats(Formatting color, String displayName, String formatting) {
		this(color, displayName, formatting, false);
	}

	Stats(Formatting color, String displayName, String formatting, boolean hidden) {
		this.color = color;
		this.displayName = StringUtils.defaultString(displayName, StringUtils.capitalize(name().toLowerCase().replace("_", " ")));
		this.formatting = StringUtils.defaultString(formatting, "+##.###;-##.###");
		this.hidden = hidden;
	}


	public static Stats valueOfIgnore(String value) {
		return valueOf(value.toUpperCase());
	}

}
