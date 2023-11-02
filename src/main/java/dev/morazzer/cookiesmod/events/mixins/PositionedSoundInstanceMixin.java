package dev.morazzer.cookiesmod.events.mixins;

import dev.morazzer.cookiesmod.events.api.PositionedSoundInstanceCallback;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PositionedSoundInstance.class)
public class PositionedSoundInstanceMixin extends AbstractSoundInstance {

    protected PositionedSoundInstanceMixin(SoundEvent sound, SoundCategory category, Random random) {
        super(sound, category, random);
    }

    /**
     * Called when the game spawns a new sound instance. Called when a sound should be played.
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
     * @param ci              The callback information.
     */
    @Inject(
        method = "<init>(Lnet/minecraft/util/Identifier;Lnet/minecraft/sound/SoundCategory;FFLnet/minecraft/util/math/random/Random;ZILnet/minecraft/client/sound/SoundInstance$AttenuationType;DDDZ)V",
        at = @At("RETURN")
    )
    public void create(
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
        boolean relative,
        CallbackInfo ci
    ) {
        if (PositionedSoundInstanceCallback.CALLBACK.invoker()
            .play(
                identifier,
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
            super.volume = 0;
        }
    }

}
