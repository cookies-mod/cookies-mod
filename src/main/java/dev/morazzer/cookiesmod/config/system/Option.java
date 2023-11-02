package dev.morazzer.cookiesmod.config.system;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.Text;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

/**
 * An option that can be displayed in the config.
 *
 * @param <T> The type of the value.
 * @param <O> The type of the option.
 */
@Getter
@Setter
public abstract class Option<T, O extends Option<T, O>> {

    private final Text name;
    private final Text description;
    protected T value;
    protected List<ValueChangeCallback<T>> callbacks = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    /**
     * Creates a new option.
     *
     * @param name        The name.
     * @param description The description.
     * @param value       The initial value.
     */
    public Option(@NotNull Text name, @NotNull Text description, T value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    /**
     * Sets the value.
     *
     * @param value The value.
     */
    public void setValue(T value) {
        T oldValue = this.value;
        this.value = value;
        this.updateCallbacks(oldValue);
    }

    /**
     * Adds a hidden key that can be searched for to the option.
     *
     * @param key The key to add.
     * @return The option.
     */
    @Deprecated(forRemoval = true)
    public final O withHiddenKey(@NotNull String key) {
        return this.withHiddenKeys(key);
    }

    /**
     * Adds multiple hidden keys that can all be searched for to the option.
     *
     * @param keys The keys to add.
     * @return The option.
     */
    @Deprecated(forRemoval = true)
    public final O withHiddenKeys(@NotNull String... keys) {
        return this.withTags(keys);
    }

    /**
     * Adds a tag that cna be searched for to the option.
     *
     * @param tag The tag to add.
     * @return The option.
     */
    public final O withTag(@NotNull @NotBlank String tag) {
        return this.withTags(tag);
    }

    /**
     * Adds multiple tags that can all be searched for to the option.
     *
     * @param tags The tags to add.
     * @return The option.
     */
    public final O withTags(@NotNull @NotBlank String... tags) {
        this.tags.addAll(Arrays.asList(tags));
        return this.asOption();
    }

    /**
     * Loads the option from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     */
    public abstract void load(@NotNull JsonElement jsonElement);

    /**
     * Saves the option to a {@linkplain com.google.gson.JsonElement}.
     *
     * @return The json element.
     */
    @NotNull
    public abstract JsonElement save();

    /**
     * Gets the config editor which will be used to render the option in the config.
     *
     * @return The editor.
     */
    @NotNull
    public abstract ConfigOptionEditor<T, O> getEditor();

    /**
     * Whether the option can be serialized or not. Returning false will disable calls to {@link Option#save()} and
     * {@link Option#load(com.google.gson.JsonElement)}.
     *
     * @return If the option is serializable.
     */
    public boolean canBeSerialized() {
        return true;
    }

    /**
     * Adds a callback that will be called when the value is changed.
     *
     * @param valueChangeCallback The callback to add.
     * @return The option.
     */
    @NotNull
    public final O withCallback(@NotNull ValueChangeCallback<T> valueChangeCallback) {
        this.callbacks.add(valueChangeCallback);
        return this.asOption();
    }

    /**
     * Casts the option to the type of the option.
     *
     * @return The option.
     */
    @NotNull
    public O asOption() {
        //noinspection unchecked
        return (O) this;
    }

    /**
     * Runs all callbacks that are currently registered.
     *
     * @param oldValue The old value.
     */
    protected void updateCallbacks(T oldValue) {
        this.callbacks.forEach(callbacks -> callbacks.valueChanged(oldValue, this.value));
    }

    /**
     * Functional interface to listen to value changes.
     *
     * @param <T> The type of the value.
     */
    @FunctionalInterface
    public interface ValueChangeCallback<T> {

        /**
         * Called when the value of an option changed.
         *
         * @param oldValue The old value.
         * @param newValue The new value.
         */
        void valueChanged(T oldValue, T newValue);

    }

}
