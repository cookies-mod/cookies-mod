package dev.morazzer.cookiesmod.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class DevUtils {

	private static final Set<Identifier> enabledTools = new CopyOnWriteArraySet<>();
	@Getter
	public static final Set<Identifier> availableTools = new HashSet<>();
	@Getter
	private static final Set<Identifier> disabledTools = new CopyOnWriteArraySet<>(availableTools);

	private static final Identifier EXTRA_LOGGING = createIdentifier("extra_logging");

	public static void log(String key, Object message, Object... replacements) {
		if (!enabledTools.contains(EXTRA_LOGGING)) return;
		log.info("[%s]: %s".formatted(key, message), replacements);
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

	public static Identifier createIdentifier(String name) {
		Identifier identifier = new Identifier("cookiesmod", "dev/" + name);
		availableTools.add(identifier);
		disable(identifier);
		return identifier;
	}

	public static boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
}
