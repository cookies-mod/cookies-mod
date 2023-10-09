package dev.morazzer.cookiesmod.features.farming.garden.composter;

import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.events.api.PositionedSoundInstanceCallback;
import dev.morazzer.cookiesmod.features.farming.garden.Garden;
import dev.morazzer.cookiesmod.features.farming.garden.Plot;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;

@LoadModule("farming/garden/composter_mute")
public class MuteComposterNoise implements Module {

    @Override
    public void load() {
        PositionedSoundInstanceCallback.CALLBACK.register((id, category, volume, pitch, random, repeat, repeatDelay, attenuationType, x, y, z, relative) -> {
            if (!Garden.isOnGarden()) return false;
            if (!Plot.getCurrentPlot().isBarn()) return false;
            if (!ConfigManager.getConfig().gardenCategory.compostFoldable.muteComposter.getValue()) return false;

            // All the sounds + their respective pitch/volume the composter produces
            return (id.toString().equals("minecraft:entity.wolf.growl") && volume == 0.3f && pitch == 0.5873016f)
                    || (id.toString().equals("minecraft:block.water.ambient") && volume == 0.5f && pitch == 0.5873016f)
                    || (id.toString().equals("minecraft:block.piston.extend") && volume == 1f && pitch == 1.4920635f)
                    || (id.toString().equals("minecraft:entity.chicken.egg") && volume == 1f && pitch == 0.7936508f);
        });
    }

    @Override
    public String getIdentifierPath() {
        return "farming/garden/composter_mute";
    }
}
