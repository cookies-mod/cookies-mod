package dev.morazzer.cookiesmod.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.LoggerFactory;

/**
 * Various methods related to development settings.
 */
@Slf4j
public class DevUtils {

    @Getter
    public static final Set<Identifier> availableTools = new HashSet<>();
    private static final Set<Identifier> enabledTools = new CopyOnWriteArraySet<>();
    @Getter
    private static final Set<Identifier> disabledTools = new CopyOnWriteArraySet<>(availableTools);

    private static final Identifier EXTRA_LOGGING = createIdentifier("extra_logging");

    /**
     * Logs a specific value to the console.
     *
     * @param key          The key of the logger.
     * @param message      The message to log.
     * @param replacements The replacements.
     */
    public static void log(String key, Object message, Object... replacements) {
        if (!enabledTools.contains(EXTRA_LOGGING)) {
            return;
        }
        LoggerFactory.getLogger(key).info("%s".formatted(message), replacements);
    }

    /**
     * Enables a specific dev tool.
     *
     * @param identifier The tool to enable.
     * @return If the tool was enabled.
     */
    public static boolean enable(Identifier identifier) {
        if (!availableTools.contains(identifier)) {
            return false;
        }

        enabledTools.add(identifier);
        disabledTools.remove(identifier);
        return true;
    }

    /**
     * Disables a specific dev tool.
     *
     * @param identifier The tool to disable.
     * @return If the tool was disabled.
     */
    public static boolean disable(Identifier identifier) {
        if (!availableTools.contains(identifier)) {
            return false;
        }

        enabledTools.remove(identifier);
        disabledTools.add(identifier);
        return true;
    }

    /**
     * Whether a dev tool is enabled.
     *
     * @param identifier The tool to check.
     * @return Whether it is enabled.
     */
    public static boolean isEnabled(Identifier identifier) {
        return enabledTools.contains(identifier);
    }

    /**
     * Gets a list of all enabled tools.
     *
     * @return A list of tools.
     */
    public static Set<Identifier> getEnabledTools() {
        return Collections.unmodifiableSet(enabledTools);
    }

    /**
     * Creates a dev tool.
     *
     * @param name The name of the tool.
     * @return The identifier.
     */
    public static Identifier createIdentifier(String name) {
        Identifier identifier = new Identifier("cookiesmod", "dev/" + name);
        availableTools.add(identifier);
        disable(identifier);
        return identifier;
    }

    /**
     * Whether the mod is running in a development environment.
     *
     * @return Whether it is a development environment.
     */
    public static boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

}
