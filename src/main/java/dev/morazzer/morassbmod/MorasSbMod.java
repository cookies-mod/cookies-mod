package dev.morazzer.morassbmod;

import dev.morazzer.morassbmod.commands.dev.DevCommand;
import dev.morazzer.morassbmod.commands.OpenConfigCommand;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.NetworkState;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class MorasSbMod implements ModInitializer {

    public static String prefix = "[MSBM]: ";
    public static int color = 0xE99DBE;

    public static MutableText createPrefix() {
        return Text.literal(MorasSbMod.prefix).setStyle(Style.EMPTY.withColor(MorasSbMod.color));
    }

    public static final Logger LOGGER = LoggerFactory.getLogger("morassbmod");

    @Override
    public void onInitialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            new OpenConfigCommand().register(dispatcher);
            new DevCommand().register(dispatcher);
        });
        LOGGER.info("Skymora loading...");
    }
}
