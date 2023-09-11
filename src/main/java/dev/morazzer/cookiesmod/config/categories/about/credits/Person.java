package dev.morazzer.cookiesmod.config.categories.about.credits;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.features.PlayerManager;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Getter
public class Person {

	private final ItemStack itemStack;
	private final UUID uuid;
	private final Text name;
	private final List<Text> description;
	private final int number;

	private Person(UUID uuid, String name, List<Text> description, MutableText prefix, MutableText suffix, int number) {
		this.uuid = uuid;
		this.name = prefix.append(name).append(suffix);
		this.description = description;
		this.itemStack = new ItemStack(Items.PLAYER_HEAD.asItem()).copyAndEmpty();
		this.itemStack.getOrCreateNbt().putString("SkullOwner", name);
		this.number = number;
	}

	public static Person fromJson(JsonObject jsonObject) {
		UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
		List<Text> description = new ArrayList<>();
		String name = PlayerManager.getUserName(uuid);

		MutableText prefix;
		MutableText suffix;
		if (jsonObject.has("prefix")) {
			prefix = Optional.ofNullable(Text.Serializer.fromJson(jsonObject.getAsJsonObject("prefix"))).orElse(Text.empty());
		} else {
			prefix = Text.empty();
		}
		if (jsonObject.has("suffix")) {
			suffix = Optional.ofNullable(Text.Serializer.fromJson(jsonObject.getAsJsonObject("suffix"))).orElse(Text.empty());
		} else {
			suffix = Text.empty();
		}
		int number = Optional.ofNullable(jsonObject.get("number")).map(JsonElement::getAsInt).orElse(0);
		description.add(prefix.copy().append(name).append(suffix));
		description.addAll(StreamSupport.stream(
						jsonObject.getAsJsonArray("description").spliterator(),
						false
				)
				.map(Text.Serializer::fromJson).map(Text.class::cast).toList());


		return new Person(uuid, name, description, prefix, suffix, number);
	}

}
