package dev.morazzer.cookiesmod.events.mixins;

import dev.morazzer.cookiesmod.events.api.BlockBreakCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockBreakCallbackMixin {

    /**
     * Called on block break/replace that has been caused by the player.
     *
     * @param world The world the block was in.
     * @param pos   The position of the block.
     * @param state The state of the block.
     * @param ci    The callback information.
     */
    @Inject(method = "onBroken", at = @At("RETURN"))
    private void replace(WorldAccess world, BlockPos pos, BlockState state, CallbackInfo ci) {
        BlockBreakCallback.EVENT.invoker().onBlockBreak(world, pos, state);
    }

}
