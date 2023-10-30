package dev.morazzer.cookiesmod.events.mixins;

import dev.morazzer.cookiesmod.events.api.PlayerListUpdateEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Yes, this accesses direct packet processing, but it doesn't modify or send anything to the server.
 */
@Mixin(ClientPlayNetworkHandler.class)
public class PlayerListUpdateMixin {

    /**
     * Called when the client receives a player list update packet.
     *
     * @param action        The action that happens.
     * @param receivedEntry The entry the game received.
     * @param currentEntry  The old entry the game had.
     * @param ci            The callback information.
     */
    @Inject(method = "handlePlayerListAction", at = @At("RETURN"))
    public void addEntry(
            PlayerListS2CPacket.Action action, PlayerListS2CPacket.Entry receivedEntry, PlayerListEntry currentEntry,
            CallbackInfo ci
    ) {
        if (action == PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME) {
            PlayerListUpdateEvent.UPDATE_NAME.invoker().update(currentEntry);
            return;
        }

        if (action != PlayerListS2CPacket.Action.UPDATE_LISTED) {
            return;
        }

        if (receivedEntry.listed()) {
            PlayerListUpdateEvent.ADD_PLAYERS.invoker().update(currentEntry);
            return;
        }
        PlayerListUpdateEvent.REMOVE_PLAYERS.invoker().update(currentEntry);
    }

}
