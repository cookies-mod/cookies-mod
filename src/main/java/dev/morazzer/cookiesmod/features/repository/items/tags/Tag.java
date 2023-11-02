package dev.morazzer.cookiesmod.features.repository.items.tags;

import java.util.List;
import net.minecraft.util.Identifier;

/**
 * A tag representing a collection of items/identifiers.
 *
 * @param identifiers All identifiers in the tag.
 * @param key         The tag identifier.
 */
public record Tag(
    List<Identifier> identifiers,
    Identifier key
) {
}
