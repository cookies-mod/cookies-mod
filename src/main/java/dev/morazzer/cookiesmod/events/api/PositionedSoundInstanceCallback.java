package dev.morazzer.cookiesmod.events.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

@FunctionalInterface
public interface PositionedSoundInstanceCallback {

	Event<PositionedSoundInstanceCallback> CALLBACK = EventFactory.createArrayBacked(
			PositionedSoundInstanceCallback.class,
			positionedSoundInstanceCallbacks -> (id, category, volume, pitch, random, repeat, repeatDelay, attenuationType, x, y, z, relative) -> {
				for (PositionedSoundInstanceCallback positionedSoundInstanceCallback : positionedSoundInstanceCallbacks) {
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

	boolean play(
			Identifier id, SoundCategory category, float volume, float pitch, Random random, boolean repeat,
			int repeatDelay, SoundInstance.AttenuationType attenuationType, double x, double y, double z,
			boolean relative);
}
