package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * A requirement to have melodies hair completed.
 */
public class MelodyHairRequirement extends Requirement {

    {
        requirementType = RequirementTypes.MELODY_HAIR;
    }

    @Override
    public MutableText getRequirementString() {
        return Text
            .literal("*hidden* ")
            .append(super.getRequirementString())
            .append(Text.literal("Melodies Harp ").formatted(Formatting.DARK_PURPLE))
            .append(" *hidden*")
            .formatted(Formatting.DARK_RED);
    }

}
