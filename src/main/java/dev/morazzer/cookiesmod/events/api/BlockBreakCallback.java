package dev.morazzer.cookiesmod.events.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;

/**
 * Callback that will trigger when a block was broken.
 */
@FunctionalInterface
public interface BlockBreakCallback {

    Event<BlockBreakCallback> EVENT = EventFactory.createArrayBacked(
            BlockBreakCallback.class,
            blockBreakCallbacks -> (world, pos, state) -> {
                for (BlockBreakCallback blockBreakCallback : blockBreakCallbacks) {
                    blockBreakCallback.onBlockBreak(world, pos, state);
                }
            }
    );

    /**
     * Called when a block was broken.
     *
     * @param world    The world the block was in.
     * @param position The position the block was at.
     * @param state    The block state.
     */
    void onBlockBreak(@NotNull WorldAccess world, @NotNull BlockPos position, @NotNull BlockState state);

}
