package dev.morazzer.cookiesmod.events.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

@FunctionalInterface
public interface BlockBreakCallback {
	Event<BlockBreakCallback> EVENT = EventFactory.createArrayBacked(BlockBreakCallback.class, blockBreakCallbacks -> (world, pos, state) ->  {
		for (BlockBreakCallback blockBreakCallback : blockBreakCallbacks){
			blockBreakCallback.onBlockBreak(world, pos, state);
		}
	});

	void onBlockBreak(WorldAccess world, BlockPos pos, BlockState state);
}
