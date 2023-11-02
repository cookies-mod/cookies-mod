package dev.morazzer.cookiesmod.events.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

/**
 * Callback to stop a positioned sound from playing.
 */
@FunctionalInterface
public interface PositionedSoundInstanceCallback {

    Event<PositionedSoundInstanceCallback> CALLBACK = EventFactory.createArrayBacked(
        PositionedSoundInstanceCallback.class,
        positionedSoundInstanceCallbacks ->
            (id, category, volume, pitch, random, repeat, repeatDelay, attenuationType, x, y, z, relative) -> {
                for (var positionedSoundInstanceCallback : positionedSoundInstanceCallbacks) {
                    if (positionedSoundInstanceCallback.play(
                        id,
                        category,
                        volume,
                        pitch,
                        random,
                        repeat,
                        repeatDelay,
                        attenuationType,
                        x,
                        y,
                        z,
                        relative
                    )) {
                        return true;
                    }
                }
                return false;
            }
    );

    /**
     * Called when a sound should be played.
     *
     * @param identifier      The identifier of the sound.
     * @param category        The category of the sound.
     * @param volume          The volume of the sound.
     * @param pitch           The pitch of the sound.
     * @param random          The random number.
     * @param repeat          If the sound should be repeated.
     * @param repeatDelay     The delay to wait till repeating.
     * @param attenuationType If the sound is linear.
     * @param x               The x position of the sound source.
     * @param y               The y position of the sound source.
     * @param z               The z position of the sound source.
     * @param relative        If the coordinates are relative or not.
     * @return True if the sound should not be played, false otherwise.
     */
    boolean play(
        Identifier identifier,
        SoundCategory category,
        float volume,
        float pitch,
        Random random,
        boolean repeat,
        int repeatDelay,
        SoundInstance.AttenuationType attenuationType,
        double x,
        double y,
        double z,
        boolean relative
    );

}
