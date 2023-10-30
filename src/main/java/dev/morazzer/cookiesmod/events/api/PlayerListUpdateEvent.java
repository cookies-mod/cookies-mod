package dev.morazzer.cookiesmod.events.api;

import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.PlayerListEntry;

/**
 * Various events related to the player list.
 */
@FunctionalInterface
public interface PlayerListUpdateEvent {

    Event<PlayerListUpdateEvent> ADD_PLAYERS = EventFactory.createArrayBacked(
            PlayerListUpdateEvent.class,
            PlayerListUpdateEvent::distribute
    );

    Event<PlayerListUpdateEvent> REMOVE_PLAYERS = EventFactory.createArrayBacked(
            PlayerListUpdateEvent.class,
            PlayerListUpdateEvent::distribute
    );

    Event<PlayerListUpdateEvent> UPDATE_NAME = EventFactory.createArrayBacked(
            PlayerListUpdateEvent.class,
            PlayerListUpdateEvent::distribute
    );

    /**
     * Distributes an event to all the subscribed listeners.
     *
     * @param playerListUpdateEvents The list of events.
     * @return The invoker.
     */
    static PlayerListUpdateEvent distribute(PlayerListUpdateEvent[] playerListUpdateEvents) {
        return currentEntry -> ExceptionHandler.tryCatch(() -> {
            for (PlayerListUpdateEvent playerListUpdateEvent : playerListUpdateEvents) {
                playerListUpdateEvent.update(currentEntry);
            }
            return null;
        });
    }

    /**
     * Called when the player list was updated.
     *
     * @param currentEntry The entry that was updated/created/removed.
     */
    void update(PlayerListEntry currentEntry);

}
