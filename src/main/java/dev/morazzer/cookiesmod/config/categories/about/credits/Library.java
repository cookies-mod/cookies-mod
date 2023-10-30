package dev.morazzer.cookiesmod.config.categories.about.credits;

import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A single library in the credit section.
 *
 * @param name        The name of the library.
 * @param description A list of text with a small description of the library.
 * @param url         A link to the website/GitHub repository of the library.
 */
public record Library(
        @NotNull String name,
        @NotNull List<Text> description,
        @NotNull String url
) {}
