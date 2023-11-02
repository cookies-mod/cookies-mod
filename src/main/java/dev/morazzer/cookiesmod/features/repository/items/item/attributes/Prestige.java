package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import java.util.List;
import net.minecraft.util.Identifier;

/**
 * Representing the prestige levels for crimson armours.
 *
 * @param itemId The item id of the new item.
 * @param costs  The cost to upgrade it.
 */
public record Prestige(
    Identifier itemId,
    List<SalvageUpgrade<?>> costs
) {
}
