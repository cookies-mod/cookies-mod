package dev.morazzer.cookiesmod.mixin;

import dev.morazzer.cookiesmod.features.repository.items.item.SkyblockItem;

/**
 * Accessor to set and get a {@linkplain SkyblockItem} instance for an item to override lore rendering.
 */
public interface ItemStackTooltip {

    void cookies$setSkyblockItem(SkyblockItem repositoryItem);

    SkyblockItem cookies$getSkyblockItem();

}
