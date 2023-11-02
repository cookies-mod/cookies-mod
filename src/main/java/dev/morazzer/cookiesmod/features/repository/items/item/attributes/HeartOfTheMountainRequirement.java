package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import lombok.AllArgsConstructor;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * A requirement for a specific heart of the mountain tier.
 */
@AllArgsConstructor
public class HeartOfTheMountainRequirement extends Requirement {

    public int tier;

    {
        requirementType = RequirementTypes.HEART_OF_THE_MOUNTAIN;
    }

    @Override
    public MutableText getRequirementString() {
        return super
            .getRequirementString()
            .append(Text
                .empty()
                .append(" Heart Of The Mountain Tier ")
                .append(tier + ".")
                .formatted(Formatting.DARK_PURPLE));
    }

}
