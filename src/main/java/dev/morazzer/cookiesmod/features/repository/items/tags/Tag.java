package dev.morazzer.cookiesmod.features.repository.items.tags;

import net.minecraft.util.Identifier;

import java.util.List;

/**
 * A tag representing a collection of items/identifiers.
 *
 * @param identifiers All identifiers in the tag.
 * @param key         The tag identifier.
 */
public record Tag(
        List<Identifier> identifiers,
        Identifier key
) {}
