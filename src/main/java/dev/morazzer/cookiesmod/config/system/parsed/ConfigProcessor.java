package dev.morazzer.cookiesmod.config.system.parsed;

import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.Config;
import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * Processor to parse/compile the config, so it can be displayed.
 */
@Slf4j
public class ConfigProcessor {

    /**
     * Starts the processing of a config.
     *
     * @param config       The config to process.
     * @param configReader The config reader to read the values.
     */
    public static void processConfig(Config<?> config, ConfigReader configReader) {
        configReader.beginConfig(config);
        for (Field field : config.getClass().getFields()) {
            if (field.getType().getSuperclass() != Category.class) {
                continue;
            }
            if (!Modifier.isPublic(field.getModifiers())) {
                log.warn("Category in {} on non public field {}", config.getClass(), field.getName());
                continue;
            }

            Category category = (Category) ExceptionHandler.removeThrows(() -> field.get(config));

            configReader.beginCategory(category);
            processCategory(category, configReader);
            configReader.endCategory();
        }
        configReader.endConfig();
    }

    /**
     * Processes a foldable and all of its fields.
     *
     * @param foldable     The foldable to process.
     * @param configReader The config reader to read the values.
     */
    private static void processFoldable(Foldable foldable, ConfigReader configReader) {
        processAny(foldable, configReader);
    }

    /**
     * Processes any object and all of its fields.
     *
     * @param object       The object to process.
     * @param configReader The config reader to read the values.
     */
    public static void processAny(Object object, ConfigReader configReader) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (!Optional
                .ofNullable(field.getType().getSuperclass())
                .map(clazz -> clazz.isAssignableFrom(Option.class) || clazz.isAssignableFrom(Foldable.class))
                .orElse(false)) {
                continue;
            }
            if (Optional
                .of(field.getType().getSuperclass())
                .map(clazz -> clazz.isAssignableFrom(Foldable.class))
                .orElse(false)) {
                Foldable foldable = (Foldable) ExceptionHandler.removeThrows(() -> field.get(object));
                configReader.beginFoldable(foldable);
                processFoldable(foldable, configReader);
                configReader.endFoldable();
                continue;
            }

            if (Optional
                .of(field.getType().getSuperclass())
                .map(clazz -> clazz.isAssignableFrom(Option.class))
                .orElse(false)) {
                configReader.processOption((Option<?, ?>) ExceptionHandler.removeThrows(() -> field.get(object)));
                continue;
            }

            log.warn("Couldn't process field {} in {}", field, object.getClass().getName());
        }
    }

    /**
     * Processes a foldable and all of its fields.
     *
     * @param category     The category to process.
     * @param configReader The config reader to read the values.
     */
    private static void processCategory(Category category, ConfigReader configReader) {
        processAny(category, configReader);
    }

}
