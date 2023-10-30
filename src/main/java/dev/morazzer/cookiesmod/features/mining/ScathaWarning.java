package dev.morazzer.cookiesmod.features.mining;

import dev.morazzer.cookiesmod.commands.dev.subcommands.TestEntrypoint;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import dev.morazzer.cookiesmod.utils.general.CookiesUtils;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Warnings if a scatha or worms spawns.
 */
@LoadModule("mining/scatha_alert")
public class ScathaWarning implements Module {

    private static final String WORM_MESSAGE = "You hear the sound of something approaching...";

    /**
     * Plays the scatha warning.
     */
    @TestEntrypoint("scatha_alert")
    public static void playSound() {
        CookiesUtils.getPlayer().ifPresent(clientPlayerEntity -> {
            clientPlayerEntity.playSound(SoundEvent.of(SoundEvents.ENTITY_DOLPHIN_DEATH.getId()), 0.2f, 0.2f);
            clientPlayerEntity.playSound(SoundEvent.of(SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE.getId()), 2, 2f);
            clientPlayerEntity.playSound(SoundEvent.of(SoundEvents.ENTITY_TURTLE_EGG_BREAK.getId()), 1f, 1);
            MinecraftClient.getInstance().inGameHud.setTitle(Text.literal("Worm Approaching!")
                    .formatted(Formatting.RED));
            MinecraftClient.getInstance().inGameHud.setTitleTicks(2, 50, 10);
        });
    }

    @Override
    public void load() {
        ClientReceiveMessageEvents.GAME.register(ExceptionHandler.wrap((message, overlay) -> {
            if (!ConfigManager.getConfig().miningCategory.scathaAlert.getValue()) return;
            if (LocationUtils.getCurrentIsland() != LocationUtils.Islands.CRYSTAL_HOLLOWS) return;
            if (message.getString().equals(WORM_MESSAGE)) {
                playSound();
            }
        }));
    }

    @Override
    public String getIdentifierPath() {
        return "mining/scatha_alert";
    }

}
