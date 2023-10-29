package dev.morazzer.cookiesmod.config.categories.about.credits;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Record to list all contributors who helped the project by either creating art or code for it.
 *
 * @param code A list of all developers.
 * @param art  A list of all artists.
 */
public record Contributors(
        @NotNull List<Person> code,
        @NotNull List<Person> art
) {}
