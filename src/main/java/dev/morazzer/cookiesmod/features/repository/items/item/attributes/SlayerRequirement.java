package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import dev.morazzer.cookiesmod.data.enums.SlayerBoss;
import lombok.AllArgsConstructor;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

/**
 * A requirement to have a specific slayer level.
 */
@AllArgsConstructor
public class SlayerRequirement extends Requirement {

    public SlayerBoss slayerBossType;
    public int level;

    {
        requirementType = RequirementTypes.SLAYER;
    }

    @Override
    public MutableText getRequirementString() {
        return Text
            .literal("☠")
            .formatted(Formatting.DARK_RED)
            .append(" Requires ")
            .formatted(Formatting.RED)
            .append(Text
                .empty()
                .append(StringUtils.capitalize(slayerBossType.name().toLowerCase()))
                .append(" Slayer ")
                .append(level + ".")
                .formatted(Formatting.DARK_PURPLE));
    }

}
