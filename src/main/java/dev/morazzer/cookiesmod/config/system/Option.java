package dev.morazzer.cookiesmod.config.system;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private List<String> hiddenKeys = new ArrayList<>();

    /**
     * Create a new option
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
     * Set the value.
     *
     * @param value The value.
     */
    public void setValue(T value) {
        T oldValue = this.value;
        this.value = value;
        this.updateCallbacks(oldValue);
    }

    /**
     * Add a hidden key that can be searched for to the option.
     *
     * @param key The key to add.
     * @return The option.
     */
    public final O withHiddenKey(@NotNull String key) {
        return this.withHiddenKeys(key);
    }

    /**
     * Add multiple hidden keys that can all be searched for to the option.
     *
     * @param keys The keys to add.
     * @return The option.
     */
    public final O withHiddenKeys(@NotNull String... keys) {
        this.hiddenKeys.addAll(Arrays.asList(keys));
        //noinspection unchecked
        return (O) this;
    }

    /**
     * Load the option from a json element.
     *
     * @param jsonElement The json element.
     */
    public abstract void load(@NotNull JsonElement jsonElement);

    /**
     * Save the option to a json element.
     *
     * @return The json element.
     */
    @NotNull
    public abstract JsonElement save();

    /**
     * The config editor which will be used to render the option in the config.
     *
     * @return The editor.
     */
    @NotNull
    public abstract ConfigOptionEditor<T, O> getEditor();

    /**
     * If the option can be serialized or not.
     * Returning false will disable calls to {@link Option#save()} and {@link Option#load(com.google.gson.JsonElement)}
     *
     * @return If the option is serializable.
     */
    public boolean canBeSerialized() {
        return true;
    }

    /**
     * Add a callback that will be called when the value is changed.
     *
     * @param valueChangeCallback The callback to add.
     * @return The option.
     */
    @NotNull
    public final O withCallback(@NotNull ValueChangeCallback<T> valueChangeCallback) {
        this.callbacks.add(valueChangeCallback);
        //noinspection unchecked
        return (O) this;
    }

    /**
     * Cast the option to the type of the option, this should not be needed.
     *
     * @return The option.
     */
    @NotNull
    public O asOption() {
        //noinspection unchecked
        return (O) this;
    }

    /**
     * Run all callbacks that are currently registered.
     *
     * @param oldValue The old value.
     */
    protected void updateCallbacks(T oldValue) {
        this.callbacks.forEach(callbacks -> callbacks.valueChanged(oldValue, this.value));
    }

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
