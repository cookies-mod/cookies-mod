package dev.morazzer.cookiesmod.config.categories.about.credits;

import java.util.List;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * A list of descriptions that if met will allow a person to be listed in the credit section.
 *
 * @param maintainer       A list of text describing what a maintainer has to do.
 * @param discordStaff     A list of text describing how the discord staff get into the credits.
 * @param contributionCode A list of text describing when a person will be listed if they contribute.
 * @param contributionsArt A list of text describing when a person will be listed if they contribute.
 * @param ideas            A list of text describing when a person will be listed if they contribute ideas.
 * @param libraries        A list that explains the topic of the library section.
 */
public record Descriptions(
    @NotNull List<Text> maintainer,
    @NotNull List<Text> discordStaff,
    @NotNull List<Text> contributionCode,
    @NotNull List<Text> contributionsArt,
    @NotNull List<Text> ideas,
    @NotNull List<Text> libraries
) {
}
