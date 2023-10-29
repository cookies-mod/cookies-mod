package dev.morazzer.cookiesmod.events.mixins;

import dev.morazzer.cookiesmod.events.api.InventoryUpdateEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Yes, this accesses direct packet processing, but it doesn't modify or send anything to the server.
 */
@Mixin(ClientPlayNetworkHandler.class)
public class InventorySlotUpdateEventMixin {

    /**
     * Called when the client receives new items for an inventory.
     *
     * @param packet The packet the client receives.
     * @param ci     The callback information.
     */
    @Inject(method = "onInventory", at = @At("RETURN"))
    public void onSlotUpdate(InventoryS2CPacket packet, CallbackInfo ci) {
        InventoryUpdateEvent.EVENT.invoker().update(packet.getSyncId(), packet.getContents());
    }

}
