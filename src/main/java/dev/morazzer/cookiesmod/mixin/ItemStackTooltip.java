package dev.morazzer.cookiesmod.mixin;

import dev.morazzer.cookiesmod.features.repository.items.item.SkyblockItem;

public interface ItemStackTooltip {

	 void cookies$setSkyblockItem(SkyblockItem repositoryItem);
	SkyblockItem cookies$getSkyblockItem();

}
