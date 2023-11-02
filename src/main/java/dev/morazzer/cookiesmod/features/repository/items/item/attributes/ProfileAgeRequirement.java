package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

/**
 * A requirement to have a profile age above a set value.
 */
@AllArgsConstructor
public class ProfileAgeRequirement extends Requirement {

    public int minimumAge;
    public ChronoUnit minimumAgeUnit;

    {
        requirementType = RequirementTypes.PROFILE_AGE;
    }

    @Override
    public MutableText getRequirementString() {
        return Text
            .literal("*hidden* ")
            .append(super.getRequirementString())
            .append(Text
                .literal("Profile age of ")
                .append(String.valueOf(minimumAge))
                .append(StringUtils.capitalize(minimumAgeUnit.name().toLowerCase()))
                .formatted(Formatting.DARK_PURPLE))
            .append(" *hidden*")
            .formatted(Formatting.DARK_RED);
    }

}
