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
import dev.morazzer.cookiesmod.utils.HttpUtils;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for the mod.
 */
public class CookiesMod implements ModInitializer {

    public static final Identifier ROOT = new Identifier("cookiesmod", "root");
    private static final Logger LOGGER = LoggerFactory.getLogger("cookies-mod");
    private static final String prefix = "Cookies Mod > ";
    @Getter
    private static CookiesMod instance;
    private static boolean firstStart = false;
    private final ModuleLoader moduleLoader = new ModuleLoader();

    {
        instance = this;
    }

    /**
     * Sets the first start.
     */
    public static void setFirstStart() {
        CookiesMod.firstStart = true;
    }

    /**
     * Creates the prefix as colored text.
     *
     * @return The prefix.
     */
    public static MutableText createPrefix() {
        return Text.literal(prefix).styled(style -> style.withColor(ColorUtils.mainColor));
    }

    /**
     * Creates the prefix with a gradient from the default mod color to the end color.
     *
     * @param endColor The end color.
     * @return The text.
     */
    public static MutableText createPrefix(int endColor) {
        return ColorUtils.literalWithGradient(prefix, ColorUtils.mainColor, endColor);
    }

    /**
     * Creates a text with the default mod color.
     *
     * @return The text.
     */
    public static MutableText createColor() {
        return Text.empty().styled(style -> style.withColor(ColorUtils.mainColor));
    }

    @Override
    public void onInitialize() {
        ConfigManager.processConfig();
        ConcurrentUtils.execute(RepositoryManager::load);

        loadModules();
        HudManager.init();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> ClientCommand.loadCommands(
            dispatcher
        ));

        if (ConfigManager.getConfig().devCategory.hideSpam.getValue()) {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            for (LoggerConfig value : context.getConfiguration().getLoggers().values()) {
                value.addFilter(new StringMatchFilter.Builder().setMatchString(
                        "Ignoring player info update for unknown player").setOnMismatch(Filter.Result.NEUTRAL)
                    .setOnMatch(Filter.Result.DENY).build());
                value.addFilter(new StringMatchFilter.Builder().setMatchString(
                        "Signature is missing from Property textures").setOnMismatch(Filter.Result.NEUTRAL)
                    .setOnMatch(Filter.Result.DENY).build());
                value.addFilter(new StringMatchFilter.Builder().setMatchString("Received packet for unknown team ")
                    .setOnMismatch(Filter.Result.NEUTRAL).setOnMatch(Filter.Result.DENY).build());
            }
        }
    }

    /**
     * Loads all modules and check if they are force disabled.
     */
    private void loadModules() {
        AtomicReference<JsonArray> jsonArrayAtomicReference = new AtomicReference<>();
        try {
            HttpUtils.getResponse(
                ExceptionHandler.removeThrows(() -> new URI("https://cookies-mod-features.deno.dev")),
                ExceptionHandler.wrap(closeableHttpResponse -> {
                    try {
                        String body = new String(closeableHttpResponse.getEntity().getContent().readAllBytes());
                        jsonArrayAtomicReference.set(JsonUtils.CLEAN_GSON.fromJson(body, JsonArray.class));
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
                LOGGER.warn("Skipping module {}, it has been marked as disabled by the developers", id);
                return DevUtils.isDevEnvironment();
            }

            return true;
        });
        moduleLoader.loadModules();
    }

}
