package dev.morazzer.cookiesmod.events.api;

import java.util.UUID;
import javax.annotation.Nullable;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

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

    Event<ProfileSwapEvent> AFTER_SET = EventFactory.createArrayBacked(
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

    Event<Runnable> AFTER_SET_NO_UUID = EventFactory.createArrayBacked(
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
    void swapProfile(@Nullable UUID previous, UUID current);

}
