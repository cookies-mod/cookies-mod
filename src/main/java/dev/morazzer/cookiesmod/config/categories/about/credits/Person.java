package dev.morazzer.cookiesmod.config.categories.about.credits;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.utils.general.CookiesUtils;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * A person that is mentioned in the {@linkplain dev.morazzer.cookiesmod.config.categories.about.credits.Credits}.
 * For a person to be listed, they must have a valid minecraft account.
 */
@Getter
public class Person {

    private final ItemStack itemStack;
    private final UUID uuid;
    private final Text name;
    private final List<Text> description;
    private final int number;

    /**
     * Create a new person.
     *
     * @param uuid        The uuid of the minecraft account.
     * @param name        The name of the person.
     * @param description A small description of what they do/did.
     * @param prefix      A prefix that will be set before the name.
     * @param suffix      A suffix that will be set after the name.
     * @param number      A number to support sorting.
     */
    private Person(
            @NotNull UUID uuid,
            @NotNull @NotBlank String name,
            @NotNull List<Text> description,
            @NotNull MutableText prefix,
            @NotNull MutableText suffix,
            int number
    ) {
        this.uuid = uuid;
        this.name = prefix.append(name).append(suffix);
        this.description = description;
        this.itemStack = new ItemStack(Items.PLAYER_HEAD.asItem()).copyAndEmpty();
        this.itemStack.getOrCreateNbt().putString("SkullOwner", name);
        this.number = number;
    }

    /**
     * Creates a new instance of a person from a json object
     */
    @NotNull
    public static Person fromJson(@NotNull JsonObject jsonObject) {
        UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
        List<Text> description = new ArrayList<>();
        String name = CookiesUtils.getUsername(uuid).orElse("Unknown");

        MutableText prefix;
        MutableText suffix;
        if (jsonObject.has("prefix")) {
            prefix = Optional
                    .ofNullable(Text.Serializer.fromJson(jsonObject.getAsJsonObject("prefix")))
                    .orElse(Text.empty());
        } else {
            prefix = Text.empty();
        }

        if (jsonObject.has("suffix")) {
            suffix = Optional
                    .ofNullable(Text.Serializer.fromJson(jsonObject.getAsJsonObject("suffix")))
                    .orElse(Text.empty());
        } else {
            suffix = Text.empty();
        }

        int number = Optional.ofNullable(jsonObject.get("number")).map(JsonElement::getAsInt).orElse(0);
        description.add(prefix.copy().append(name).append(suffix));
        description.addAll(StreamSupport
                .stream(jsonObject.getAsJsonArray("description").spliterator(), false)
                .map(Text.Serializer::fromJson)
                .map(Text.class::cast)
                .toList());

        return new Person(uuid, name, description, prefix, suffix, number);
    }

}
