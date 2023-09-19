package dev.morazzer.cookiesmod.events.api;

import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.PlayerListEntry;

@FunctionalInterface
public interface PlayerListUpdateEvent {

	Event<PlayerListUpdateEvent> ADD_PLAYERS = EventFactory.createArrayBacked(
			PlayerListUpdateEvent.class,
			PlayerListUpdateEvent::spread
	);

	Event<PlayerListUpdateEvent> REMOVE_PLAYERS = EventFactory.createArrayBacked(
			PlayerListUpdateEvent.class,
			PlayerListUpdateEvent::spread
	);

	Event<PlayerListUpdateEvent> UPDATE_NAME = EventFactory.createArrayBacked(
			PlayerListUpdateEvent.class,
			PlayerListUpdateEvent::spread
	);

	static PlayerListUpdateEvent spread(PlayerListUpdateEvent[] playerListUpdateEvents) {
		return currentEntry -> ExceptionHandler.tryCatch(() -> {
			for (PlayerListUpdateEvent playerListUpdateEvent : playerListUpdateEvents) {
				playerListUpdateEvent.update(currentEntry);
			}
			return null;
		});
	}

	void update(PlayerListEntry currentEntry);

}
