package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import lombok.AllArgsConstructor;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

/**
 * A requirement that needs a specific collection level.
 */
@AllArgsConstructor
public class CollectionRequirement extends Requirement {

    public int tier;
    public String collection;

    {
        requirementType = RequirementTypes.COLLECTION;
    }

    @Override
    public MutableText getRequirementString() {
        return super
                .getRequirementString()
                .append(Text
                        .empty()
                        .append(StringUtils.capitalize(collection.replace("_", " ").toLowerCase()))
                        .append(" Collection ")
                        .append(tier + "."));
    }

}
