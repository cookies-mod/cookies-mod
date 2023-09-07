package dev.morazzer.cookiesmod.config.system;

import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.Optional;

public abstract class Config<T extends Config<T>> {
	public JsonObject save() {
		JsonObject jsonObject = new JsonObject();

		for (Field declaredField : this.getClass().getDeclaredFields()) {
			if (Optional.ofNullable(declaredField.getType().getSuperclass()).map(Category.class::equals).orElse(false)) {
				Category category = (Category) ExceptionHandler.removeThrows(() -> declaredField.get(this));
				jsonObject.add(declaredField.getName(), category.save());
			}
		}

		return jsonObject;
	}

	public T load(JsonObject jsonObject) {
		for (Field declaredField : this.getClass().getDeclaredFields()) {
			if (Optional.ofNullable(declaredField.getType().getSuperclass()).map(Category.class::equals).orElse(false)) {
				Category category = (Category) ExceptionHandler.removeThrows(() -> declaredField.get(this));
				if (!jsonObject.has(declaredField.getName())) {
					continue;
				}
				category.load(jsonObject.get(declaredField.getName()));
			}
		}

		//noinspection unchecked
		return (T) this;
	}

	public abstract Text getTitle();
}
