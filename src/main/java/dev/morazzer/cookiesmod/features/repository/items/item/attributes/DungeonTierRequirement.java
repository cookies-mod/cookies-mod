package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import dev.morazzer.cookiesmod.data.dungeons.DungeonType;
import dev.morazzer.cookiesmod.utils.RomanNumerals;
import lombok.AllArgsConstructor;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

/**
 * A requirement for a specific dungeon floor completion.
 */
@AllArgsConstructor
public class DungeonTierRequirement extends Requirement {

    public DungeonType dungeonType;
    public int tier;

    {
        requirementType = RequirementTypes.DUNGEON_TIER;
    }

    @Override
    public MutableText getRequirementString() {
        return super
            .getRequirementString()
            .append(Text
                .empty()
                .append(StringUtils.capitalize(dungeonType.name().replace("_", " ").toLowerCase()))
                .append(" Floor ")
                .append(RomanNumerals.toRoman(tier))
                .append(" Completion."));
    }

}
