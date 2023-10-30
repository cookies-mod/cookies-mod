package dev.morazzer.cookiesmod.events.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.UUID;

/**
 * Various events related to profile swaps.
 */
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

    /**
     * Called when the mod detects a switch between two profiles.
     *
     * @param previous The previously loaded profile uuid.
     * @param current  The now loaded profile uuid.
     */
    void swapProfile(UUID previous, UUID current);

}
