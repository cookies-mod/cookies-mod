package dev.morazzer.cookiesmod.config.system.parsed;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.Config;
import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

@Slf4j
public class ConfigProcessor {


	public static void processConfig(Config<?> config, ConfigReader configReader) {
		configReader.beginConfig(config);
		for (Field field : config.getClass().getFields()) {
			System.out.println();
			if (field.getType().getSuperclass() != Category.class) {
				continue;
			}
			if (!field.isAnnotationPresent(Expose.class)) {
				log.warn("Category without @Expose in {} on field {}", config.getClass(), field.getName());
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

	private static void processFoldable(Foldable foldable, ConfigReader reader) {
		processAny(foldable, reader);
	}

	public static void processAny(Object object, ConfigReader reader) {
		for (Field field : object.getClass().getDeclaredFields()) {
			if (!Optional.ofNullable(field.getType().getSuperclass()).map(clazz -> clazz.isAssignableFrom(Option.class) || clazz.isAssignableFrom(Foldable.class)).orElse(false)) {
				continue;
			}
			if (!field.isAnnotationPresent(Expose.class)
					&& Modifier.isTransient(field.getModifiers())) {
				log.warn("Field {} int category {} is non transient and doesn't have @Expose", field.getName(), object.getClass().getName());
			}

			if (Optional.ofNullable(field.getType().getSuperclass()).map(clazz -> clazz.isAssignableFrom(Foldable.class)).orElse(false)) {
				Foldable foldable = (Foldable) ExceptionHandler.removeThrows(() -> field.get(object));
				reader.beginFoldable(foldable);
				processFoldable(foldable, reader);
				reader.endFoldable();
				continue;
			}

			if (Optional.ofNullable(field.getType().getSuperclass()).map(clazz -> clazz.isAssignableFrom(Option.class)).orElse(false)) {
				reader.processOption((Option<?, ?>) ExceptionHandler.removeThrows(() -> field.get(object)));
				continue;
			}

			log.warn("Couldn't process field {} in {}", field, object.getClass().getName());
		}
	}

	private static void processCategory(Category category, ConfigReader reader) {
		processAny(category, reader);
	}

}
