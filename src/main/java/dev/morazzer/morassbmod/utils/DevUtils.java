package dev.morazzer.morassbmod.utils;

import dev.morazzer.morassbmod.MorasSbMod;
import dev.morazzer.morassbmod.gui.screen.config.ConfigScreen;
import dev.morazzer.morassbmod.gui.screen.element.ClickableTextWidget;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class DevUtils {

    private static final Set<Identifier> enabledTools = new CopyOnWriteArraySet<>();
    private static final Identifier EXTRA_LOGGING = new Identifier("morassbmod", "dev/extra_logging");
    private static final Set<Identifier> availableTools = Set.of(
            EXTRA_LOGGING,
            ConfigScreen.DEBUG_CONFIG_SCREEN,
            ClickableTextWidget.SHOW_CLICKABLE_TEXT_BOUNDING_BOX
    );

    private static final Set<Identifier> disabledTools = new CopyOnWriteArraySet<>(availableTools);

    public static void log(String key, Object message, Object... replacements) {
        if (!enabledTools.contains(EXTRA_LOGGING)) return;
        MorasSbMod.LOGGER.info("[%s]: %s".formatted(key, message), replacements);
    }

    public static boolean enable(Identifier identifier) {
        if (!availableTools.contains(identifier)) {
            return false;
        }

        enabledTools.add(identifier);
        disabledTools.remove(identifier);
        return true;
    }

    public static boolean disable(Identifier identifier) {
        if (!availableTools.contains(identifier)) {
            return false;
        }

        enabledTools.remove(identifier);
        disabledTools.add(identifier);
        return true;
    }

    public static boolean isEnabled(Identifier identifier) {
        return enabledTools.contains(identifier);
    }

    public static Set<Identifier> getEnabledTools() {
        return Collections.unmodifiableSet(enabledTools);
    }

    public static Set<Identifier> getAvailableTools() {
        //return
        return availableTools;
    }

    public static Set<Identifier> getDisabledTools() {
        return disabledTools;
    }
}
