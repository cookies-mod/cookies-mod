package dev.morazzer.cookiesmod.modules;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class ModuleManager {

	private static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();

	public static void loadModules(Reflections reflections) {
		reflections.getTypesAnnotatedWith(LoadModule.class).forEach(aClass -> {
			log.debug("Found clazz annotated with @LoadModule");
			if (!Module.class.isAssignableFrom(aClass)) {
				log.warn("{} does not extend Module but is annotated with @LoadModule", aClass);
				return;
			}

			try {
				Constructor<? extends Module> constructor = (Constructor<? extends Module>) aClass.getConstructor();
				Module module = constructor.newInstance();
				modules.add(module);
			} catch (NoSuchMethodException e) {
				log.warn("No empty constructor found for class {}", aClass);
			} catch (InvocationTargetException e) {
				log.error("Error while invoking constructor {}", aClass, e);
			} catch (InstantiationException e) {
				log.error("Module {} is an abstract class", aClass);
			} catch (IllegalAccessException e) {
				log.error("Constructor not accessible {}", aClass);
			}
		});

		modules.forEach(Module::load);
	}
}
