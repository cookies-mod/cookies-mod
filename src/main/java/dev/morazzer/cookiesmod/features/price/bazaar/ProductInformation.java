package dev.morazzer.cookiesmod.features.price.bazaar;

import net.minecraft.util.Identifier;

public record ProductInformation(Identifier productId, ProductSummary[] sellSummary, ProductSummary[] buySummary,
                                 QuickStatus quickStatus) {
}
