package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import dev.morazzer.cookiesmod.data.enums.TrophyFishingReward;
import lombok.AllArgsConstructor;
import net.minecraft.text.MutableText;

/**
 * A requirement to have a specific trophy fisher reward.
 */
@AllArgsConstructor
public class TrophyFishingRequirement extends Requirement {

    public TrophyFishingReward reward;

    {
        requirementType = RequirementTypes.TROPHY_FISHING;
    }

    @Override
    public MutableText getRequirementString() {
        return super.getRequirementString().append(reward.getName()).append(" Trophy Fisher.");
    }

}
