package dev.morazzer.cookiesmod.utils.general;

import dev.morazzer.cookiesmod.events.api.ProfileSwapEvent;
import dev.morazzer.cookiesmod.events.api.ServerSwapEvent;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.CachedValue;
import dev.morazzer.cookiesmod.utils.DevUtils;
import lombok.Getter;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Functions related to skyblock.
 */
@LoadModule("skyblock_utils")
public class SkyblockUtils implements Module {

    private static final Identifier DISABLE_SKYBLOCK_CHECK = DevUtils.createIdentifier("disable_skyblock_check");
    private static final CachedValue<Boolean> isCurrentlyInSkyblock = new CachedValue<>(
            () -> ScoreboardUtils.getTitle().getString().matches("SK[YI]BLOCK.*"),
            5,
            TimeUnit.SECONDS
    );
    @Getter
    private static long lastServerSwap = -1;
    @Getter
    private static String lastServer = "";
    private static UUID lastProfileId;

    /**
     * @return Whether the user is in skyblock.
     */
    public static boolean isCurrentlyInSkyblock() {
        if (lastServerSwap + 5000 > System.currentTimeMillis()) {
            isCurrentlyInSkyblock.updateNow();
        }
        return isCurrentlyInSkyblock.getValue() || DevUtils.isEnabled(DISABLE_SKYBLOCK_CHECK);
    }

    /**
     * Gets the last profile id that was logged.
     *
     * @return The last profile id.
     */
    public static Optional<UUID> getLastProfileId() {
        return Optional.ofNullable(lastProfileId);
    }

    /**
     * Looks for any incoming profile id or server switch messages.
     *
     * @param text    The message.
     * @param overlay If the message is in the overlay.
     */
    private static void lookForProfileIdMessage(Text text, boolean overlay) {
        if (overlay) return;
        if (text.getString().matches("Profile ID: .*")) {
            DevUtils.log(
                    "profile.switch",
                    "Found new profile id was {} is {}",
                    lastProfileId,
                    text.getString().substring(12).trim()
            );
            UUID uuid = UUID.fromString(text.getString().substring(12).trim());

            if (lastProfileId == null || !lastProfileId.equals(uuid)) {
                ProfileSwapEvent.EVENT.invoker().swapProfile(lastProfileId, uuid);
                ProfileSwapEvent.EVENT_NO_UUID.invoker().run();
            }

            UUID previous = lastProfileId;
            lastProfileId = uuid;

            ProfileSwapEvent.AFTER_SET.invoker().swapProfile(previous, uuid);
            ProfileSwapEvent.AFTER_SET_NO_UUID.invoker().run();
        } else if (text.getString().matches("Sending to server (?:mini|mega)\\d*.{1,3}\\.{3}")) {
            lastServerSwap = System.currentTimeMillis();
            lastServer = text.getString().replaceAll("Sending to server ((?:mini|mega)\\d*.{1,3})\\.{3}", "$1");
            ServerSwapEvent.SERVER_SWAP.invoker().onServerSwap();
        }
    }

    @Override
    public void load() {
        ClientReceiveMessageEvents.GAME.register(SkyblockUtils::lookForProfileIdMessage);
    }

    @Override
    public String getIdentifierPath() {
        return "skyblock_utils";
    }

}
