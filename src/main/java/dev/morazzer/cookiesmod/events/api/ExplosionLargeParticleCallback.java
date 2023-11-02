package dev.morazzer.cookiesmod.events.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

/**
 * Callback that will be triggered when a large explosion particle is spawned.
 */
public interface ExplosionLargeParticleCallback {

    Event<ExplosionLargeParticleCallback> EVENT = EventFactory.createArrayBacked(
        ExplosionLargeParticleCallback.class,
        explosionLargeParticleCallbacks -> (world, x, y, z, d, spriteProvider) -> {
            for (ExplosionLargeParticleCallback explosionLargeParticleCallback : explosionLargeParticleCallbacks) {
                if (!explosionLargeParticleCallback.explosion(world, x, y, z, d, spriteProvider)) {
                    return false;
                }
            }
            return true;
        }
    );

    /**
     * Will be called if a large explosion particle spawns.
     *
     * @param world          The world the particle spawns in.
     * @param x              The x coordinate the particle spawns at.
     * @param y              The y coordinate the particle spawns at.
     * @param z              The z coordinate the particle spawns at.
     * @param scaleFactor    The scale factor.
     * @param spriteProvider The sprite provider.
     * @return False if the particle should be canceled, true otherwise.
     */
    boolean explosion(
        ClientWorld world,
        double x,
        double y,
        double z,
        double scaleFactor,
        SpriteProvider spriteProvider
    );

}
