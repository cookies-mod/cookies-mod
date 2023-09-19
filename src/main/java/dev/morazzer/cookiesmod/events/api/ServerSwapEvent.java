package dev.morazzer.cookiesmod.events.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

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

	void onServerSwap();

}
