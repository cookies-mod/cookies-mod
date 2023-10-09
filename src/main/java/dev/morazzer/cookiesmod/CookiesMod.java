package dev.morazzer.cookiesmod;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.features.hud.HudManager;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.generated.ModuleLoader;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.ConcurrentUtils;
import dev.morazzer.cookiesmod.utils.DevUtils;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.HttpUtils;
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

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class CookiesMod implements ModInitializer {

    @Getter
    private static CookiesMod instance;

    private static boolean firstStart = false;
    private final ModuleLoader moduleLoader = new ModuleLoader();

    private static final String prefix = "Cookies mod > ";

    {
        instance = this;
    }

    public static Identifier ROOT = new Identifier("cookiesmod", "root");

    @Override
    public void onInitialize() {
        ConfigManager.processConfig();
        ConcurrentUtils.execute(RepositoryManager::load);

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage("dev.morazzer.cookiesmod")
                .setScanners(Scanners.TypesAnnotated));


        loadModules();
        HudManager.init();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> ClientCommand.loadCommands(
                reflections,
                dispatcher
        ));

        if (ConfigManager.getConfig().devCategory.hideSpam.getValue()) {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            for (LoggerConfig value : context.getConfiguration().getLoggers().values()) {
                value.addFilter(new StringMatchFilter.Builder().setMatchString(
                                "Ignoring player info update for unknown player").setOnMismatch(Filter.Result.ACCEPT)
                        .setOnMatch(Filter.Result.DENY).build());
            }
        }
    }

    private void loadModules() {
        AtomicReference<JsonArray> jsonArrayAtomicReference = new AtomicReference<>();
        try {
            HttpUtils.getResponse(
                    ExceptionHandler.removeThrows(() -> new URI("https://cookies-mod-features.deno.dev")),
                    ExceptionHandler.wrap(closeableHttpResponse -> {
                        try {
                            String body = new String(closeableHttpResponse.getEntity().getContent().readAllBytes());
                            jsonArrayAtomicReference.set(GsonUtils.gsonClean.fromJson(body, JsonArray.class));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
            );
        } catch (Exception e) {
            jsonArrayAtomicReference.set(new JsonArray());
        }

        moduleLoader.shouldLoadCallback(id -> {
            if (jsonArrayAtomicReference.get() == null) {
                return true;
            }

            if (jsonArrayAtomicReference.get().contains(new JsonPrimitive(id))) {
                log.warn("Skipping module {}, it has been marked as disabled by the developers", id);
                return DevUtils.isDevEnvironment();
            }

            return true;
        });
        moduleLoader.loadModules();
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
