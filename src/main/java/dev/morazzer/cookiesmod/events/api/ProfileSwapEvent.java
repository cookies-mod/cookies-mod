package dev.morazzer.cookiesmod.events.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.UUID;

@FunctionalInterface
public interface ProfileSwapEvent {

	Event<ProfileSwapEvent> EVENT = EventFactory.createArrayBacked(
			ProfileSwapEvent.class,
			profileSwapEvents -> (previous, current) -> {
				for (ProfileSwapEvent profileSwapEvent : profileSwapEvents) {
					profileSwapEvent.swapProfile(previous, current);
				}
			}
	);

	Event<Runnable> EVENT_NO_UUID = EventFactory.createArrayBacked(
			Runnable.class,
			runnables -> () -> {
				for (Runnable runnable : runnables) {
					runnable.run();
				}
			}
	);

	void swapProfile(UUID previous, UUID current);

}
