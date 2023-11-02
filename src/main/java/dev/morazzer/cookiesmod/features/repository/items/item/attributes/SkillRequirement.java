package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import dev.morazzer.cookiesmod.data.enums.Skill;
import lombok.AllArgsConstructor;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

/**
 * A requirement to have a specific skill level or above.
 */
@AllArgsConstructor
public class SkillRequirement extends Requirement {

    public Skill skill;
    public int level;

    {
        requirementType = RequirementTypes.SKILL;
    }

    @Override
    public MutableText getRequirementString() {
        return super
            .getRequirementString()
            .append(Text
                .empty()
                .append(StringUtils.capitalize(skill.name().toLowerCase()))
                .append(" Skill ")
                .append(level + "."))
            .formatted(Formatting.GREEN);
    }

}
