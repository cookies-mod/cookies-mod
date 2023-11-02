package dev.morazzer.cookiesmod.features.price.bazaar;

import net.minecraft.util.Identifier;

/**
 * The product information for a specific item.
 *
 * @param productId   The identifier of the item.
 * @param sellSummary The summary of the sell data.
 * @param buySummary  The summary of the buy data.
 * @param quickStatus A quick view of all data.
 */
public record ProductInformation(
    Identifier productId,
    ProductSummary[] sellSummary,
    ProductSummary[] buySummary,
    QuickStatus quickStatus
) {
}
