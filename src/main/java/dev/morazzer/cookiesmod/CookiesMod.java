package dev.morazzer.cookiesmod;

import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.modules.ModuleManager;
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
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

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
        ConcurrentUtils.execute(RepositoryManager::load);

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage("dev.morazzer.cookiesmod")
                .setScanners(Scanners.TypesAnnotated));

        ModuleManager.loadModules(reflections);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> ClientCommand.loadCommands(reflections, dispatcher));


        if (ConfigManager.getConfig().devCategory.hideSpam.getValue()) {
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
