package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import dev.morazzer.cookiesmod.data.enums.Factions;
import lombok.AllArgsConstructor;
import net.minecraft.text.MutableText;
import org.apache.commons.lang3.StringUtils;

/**
 * A requirement that needs a specific reputation amount in a specific faction.
 */
@AllArgsConstructor
public class CrimsonIsleReputationRequirement extends Requirement {

    public Factions faction;
    public int reputation;

    {
        requirementType = RequirementTypes.CRIMSON_ISLE_REPUTATION;
    }

    @Override
    public MutableText getRequirementString() {
        return super
            .getRequirementString()
            .append(String.valueOf(reputation))
            .append(" ")
            .append(StringUtils.capitalize(faction.name().toLowerCase()))
            .append(" Reputation.");
    }

}
