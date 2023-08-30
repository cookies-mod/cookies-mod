package dev.morazzer.cookiesmod.mixin;

import dev.morazzer.cookiesmod.features.repository.items.RepositoryItem;

public interface ItemStackTooltip {

	 void cookies$setSkyblockItem(RepositoryItem repositoryItem);
	 RepositoryItem cookies$getSkyblockItem();

}
