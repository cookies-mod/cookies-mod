package dev.morazzer.cookiesmod.features.price.bazaar;

/**
 * The product summary for a specific item.
 *
 * @param amount       The number of items in the offer.
 * @param pricePerUnit The price per unit.
 * @param orders       The number of orders.
 */
public record ProductSummary(
    int amount,
    float pricePerUnit,
    int orders
) {
}
