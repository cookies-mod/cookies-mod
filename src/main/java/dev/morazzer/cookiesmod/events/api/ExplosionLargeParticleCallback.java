package dev.morazzer.cookiesmod.events.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public interface ExplosionLargeParticleCallback {

    Event<ExplosionLargeParticleCallback> EVENT = EventFactory.createArrayBacked(
            ExplosionLargeParticleCallback.class,
            explosionLargeParticleCallbacks -> (world, x, y, z, d, spriteProvider) -> {
                for (ExplosionLargeParticleCallback explosionLargeParticleCallback : explosionLargeParticleCallbacks) {
                    if (!explosionLargeParticleCallback.explosion(world, x,y,z,d, spriteProvider)) {
                        return false;
                    }
                }
                return true;
            }
    );


    boolean explosion(ClientWorld world, double x, double y, double z, double d, SpriteProvider spriteProvider);

}
