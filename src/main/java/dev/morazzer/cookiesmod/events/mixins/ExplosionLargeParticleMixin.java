package dev.morazzer.cookiesmod.events.mixins;

import dev.morazzer.cookiesmod.events.api.ExplosionLargeParticleCallback;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExplosionLargeParticle.class)
public abstract class ExplosionLargeParticleMixin extends SpriteBillboardParticle {


    protected ExplosionLargeParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(
            ClientWorld world, double x, double y, double z, double d, SpriteProvider spriteProvider, CallbackInfo ci) {
        if (!ExplosionLargeParticleCallback.EVENT.invoker().explosion(world, x, y, z, d, spriteProvider)) {
            scale = 0;
        }
    }

}
