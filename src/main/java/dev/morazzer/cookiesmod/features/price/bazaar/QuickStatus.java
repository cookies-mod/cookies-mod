package dev.morazzer.cookiesmod.features.price.bazaar;

/**
 * The quick overview for a specific item.
 *
 * @param sellPrice      The currently highest sell price.
 * @param sellVolume     The amount that is up for buying.
 * @param sellMovingWeek The number of items that are moved weekly.
 * @param sellOrders     The number of orders that currently are available.
 * @param buyPrice       The currently lowest buy price.
 * @param buyVolume      The amount that is up for sale.
 * @param buyMovingWeek  The number of items that are moved weekly.
 * @param buyOrders      The number of orders that currently are available.
 */
public record QuickStatus(
    float sellPrice,
    long sellVolume,
    long sellMovingWeek,
    long sellOrders,
    float buyPrice,
    long buyVolume,
    long buyMovingWeek,
    long buyOrders
) {
}
