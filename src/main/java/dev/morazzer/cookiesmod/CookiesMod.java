package dev.morazzer.cookiesmod;

import dev.morazzer.cookiesmod.commands.OpenConfigCommand;
import dev.morazzer.cookiesmod.commands.ProfileViewerCommand;
import dev.morazzer.cookiesmod.commands.dev.DevCommand;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.screen.itemlist.ItemListScreen;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.ConcurrentUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.StringMatchFilter;

@Slf4j
public class CookiesMod implements ModInitializer {

    @Getter
    private static CookiesMod instance;

    private static boolean firstStart = false;

    private static final String prefix = "Cookies mod > ";

    {
        instance = this;
    }

    public static Identifier ROOT = new Identifier("cookiesmod", "root");

    @Override
    public void onInitialize() {

        log.info("Skymora loading...");
        ConfigManager.processConfig();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            new OpenConfigCommand().register(dispatcher);
            new DevCommand().register(dispatcher);
            new ProfileViewerCommand().register(dispatcher);
        });

        ConcurrentUtils.execute(RepositoryManager::load);
        new ItemListScreen().load();


        if (ConfigManager.getConfig().devCategory.hideSpam) {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            for (LoggerConfig value : context.getConfiguration().getLoggers().values()) {
                value.addFilter(new StringMatchFilter.Builder().setMatchString("Ignoring player info update for unknown player").setOnMismatch(Filter.Result.ACCEPT).setOnMatch(Filter.Result.DENY).build());
            }
        }
    }

    public static void setFirstStart() {
        CookiesMod.firstStart = true;
    }

    public static MutableText createPrefix() {
        return Text.literal(prefix).styled(style -> style.withColor(ColorUtils.mainColor));
    }

    public static MutableText createColor() {
        return Text.empty().styled(style -> style.withColor(ColorUtils.mainColor));
    }

    public static MutableText createPrefix(int endColor) {
        return ColorUtils.literalWithGradient(prefix, ColorUtils.mainColor, endColor);
    }
}
