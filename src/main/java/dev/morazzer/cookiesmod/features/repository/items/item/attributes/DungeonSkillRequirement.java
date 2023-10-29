package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import dev.morazzer.cookiesmod.data.dungeons.DungeonType;
import lombok.AllArgsConstructor;
import net.minecraft.text.MutableText;
import org.apache.commons.lang3.StringUtils;

/**
 * A requirement for a specific dungeons kill level.
 */
@AllArgsConstructor
public class DungeonSkillRequirement extends Requirement {

    public DungeonType dungeonType;
    public int level;

    {
        requirementType = RequirementTypes.DUNGEON_SKILL;
    }

    @Override
    public MutableText getRequirementString() {
        return super
                .getRequirementString()
                .append(StringUtils.capitalize(dungeonType.name().replace("_", " ").toLowerCase()))
                .append(" Skill ")
                .append(level + ".");
    }

}
