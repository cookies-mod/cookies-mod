package dev.morazzer.cookiesmod.config.system;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.Optional;


public abstract class Category {

	public abstract Text getName();

	public abstract Text getDescription();


	@SuppressWarnings("DuplicatedCode")
	public final void load(JsonElement jsonObject) {
		for (Field declaredField : this.getClass().getDeclaredFields()) {
			if (Optional.ofNullable(declaredField.getType().getSuperclass()).map(Option.class::equals).orElse(false)) {
				Option<?, ?> o = (Option<?, ?>) ExceptionHandler.removeThrows(() -> declaredField.get(this));
				if (!o.canBeSerialized()) {
					continue;
				}
				if (!jsonObject.getAsJsonObject().has(declaredField.getName())) {
					continue;
				}
				o.load(jsonObject.getAsJsonObject().get(declaredField.getName()));
			} else if (Optional.ofNullable(declaredField.getType().getSuperclass()).map(Foldable.class::equals).orElse(false)) {
				Foldable foldable = (Foldable) ExceptionHandler.removeThrows(() -> declaredField.get(this));
				if (!jsonObject.getAsJsonObject().has(declaredField.getName())) {
					continue;
				}
				foldable.load(jsonObject.getAsJsonObject().get(declaredField.getName()));
			}
		}
	}

	@SuppressWarnings("DuplicatedCode")
	public final JsonElement save() {
		JsonObject jsonObject = new JsonObject();
		for (Field declaredField : this.getClass().getDeclaredFields()) {
			if (Optional.ofNullable(declaredField.getType().getSuperclass()).map(Option.class::equals).orElse(false)) {
				Option<?, ?> o = (Option<?, ?>) ExceptionHandler.removeThrows(() -> declaredField.get(this));
				if (!o.canBeSerialized()) {
					continue;
				}

				jsonObject.add(declaredField.getName(), o.save());
			} else if (Optional.ofNullable(declaredField.getType().getSuperclass()).map(Foldable.class::equals).orElse(false)) {
				Foldable foldable = (Foldable) ExceptionHandler.removeThrows(() -> declaredField.get(this));
				jsonObject.add(declaredField.getName(), foldable.save());
			}
		}
		return jsonObject;
	}
}
