package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import lombok.AllArgsConstructor;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * A requirement to have completed a target practice tier.
 */
@AllArgsConstructor
public class TargetPracticeRequirement extends Requirement {

    public String mode;

    {
        requirementType = RequirementTypes.TARGET_PRACTICE;
    }

    @Override
    public MutableText getRequirementString() {
        return super
            .getRequirementString()
            .append(Text.empty().append(" Target Practice " + mode).formatted(Formatting.GREEN));
    }

}
