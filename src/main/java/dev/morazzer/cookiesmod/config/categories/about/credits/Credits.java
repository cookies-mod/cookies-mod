package dev.morazzer.cookiesmod.config.categories.about.credits;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Record to describe all aspects of the credits.
 *
 * @param maintainers  A list of all people who maintain the project.
 * @param libraries    A list of all libraries used in the project.
 * @param discordStaff A list of all discord staff members.
 * @param contributors A list of all contributors (both development and art).
 * @param ideas        A list of all people who helped with providing ideas.
 * @param descriptions Descriptions on how to be listed for each respective category.
 */
public record Credits(
    @NotNull List<Person> maintainers,
    @NotNull List<Library> libraries,
    @NotNull List<Person> discordStaff,
    @NotNull Contributors contributors,
    @NotNull List<Person> ideas,
    @NotNull Descriptions descriptions
) {
}
