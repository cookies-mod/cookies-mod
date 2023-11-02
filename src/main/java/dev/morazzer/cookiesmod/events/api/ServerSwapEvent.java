package dev.morazzer.cookiesmod.events.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Event that is called when the mod detects a server swap.
 */
@FunctionalInterface
public interface ServerSwapEvent {

    Event<ServerSwapEvent> SERVER_SWAP = EventFactory.createArrayBacked(
        ServerSwapEvent.class,
        serverSwapEvents -> () -> {
            for (ServerSwapEvent serverSwapEvent : serverSwapEvents) {
                serverSwapEvent.onServerSwap();
            }
        }
    );

    /**
     * Called when the mod detects a server swap.
     */
    void onServerSwap();

}
