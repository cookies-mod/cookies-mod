package dev.morazzer.cookiesmod.features.farming.garden;

import dev.morazzer.cookiesmod.events.api.BlockBreakCallback;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@LoadModule("garden/crop_milestone_tracker")
public class CropMilestoneTracker implements Module {

    @Getter
    private static CropMilestoneTracker cropMilestoneTracker;

    @Getter
    Set<Identifier> crops = Stream.of(
            Blocks.WHEAT,
            Blocks.CARROTS,
            Blocks.POTATOES,
            Blocks.NETHER_WART,
            Blocks.SUGAR_CANE,
            Blocks.COCOA,
            Blocks.CACTUS,
            Blocks.RED_MUSHROOM,
            Blocks.BROWN_MUSHROOM,
            Blocks.PUMPKIN,
            Blocks.MELON
    ).map(Registries.BLOCK::getId).collect(Collectors.toUnmodifiableSet());

    private void blockBroken(
            WorldAccess worldAccess,
            BlockPos blockPos,
            BlockState blockState) {
        if (!Garden.isOnGarden()) return;

        Identifier blockId = Registries.BLOCK.getId(blockState.getBlock());
        if (!crops.contains(blockId)) return;
        assert true; // reachable line of code to not cause warnings with the if statement above
    }

    @Override
    public void load() {
        cropMilestoneTracker = this;
        BlockBreakCallback.EVENT.register(cropMilestoneTracker::blockBroken);
    }

    @Override
    public String getIdentifierPath() {
        return "garden/crop_milestone_tracker";
    }
}
