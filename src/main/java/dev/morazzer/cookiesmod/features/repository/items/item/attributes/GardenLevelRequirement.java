package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import lombok.AllArgsConstructor;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * A requirement for a specific garden level.
 */
@AllArgsConstructor
public class GardenLevelRequirement extends Requirement {

    public int level;

    {
        requirementType = RequirementTypes.GARDEN_LEVEL;
    }

    @Override
    public MutableText getRequirementString() {
        return super
                .getRequirementString()
                .append(Text.empty().append("Garden Level").append(" " + level).formatted(Formatting.GREEN))
                .append(".");
    }

}
