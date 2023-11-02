package dev.morazzer.cookiesmod.config.system.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.editor.BooleanEditor;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.features.hud.HudElement;
import dev.morazzer.cookiesmod.features.hud.HudManager;
import dev.morazzer.cookiesmod.utils.render.Position;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Used for simple toggle buttons in the config.
 */
@Slf4j
public class BooleanOption extends Option<Boolean, BooleanOption> {

    private HudElement hudElement = null;

    public BooleanOption(Text name, Text description, Boolean value) {
        super(name, description, value);
    }

    /**
     * Adds a HUD element to the button. This element will only be visible when the button is toggled on.
     *
     * @param hudElement The hud element to attach.
     * @return The option.
     */
    public BooleanOption withHudElement(HudElement hudElement) {
        HudManager.registerHudElement(hudElement);
        this.hudElement = hudElement;
        return this.withCallback((oldValue, newValue) -> hudElement.toggle(newValue));
    }

    @Override
    public void load(@NotNull JsonElement jsonElement) {
        if (jsonElement instanceof JsonObject jsonObject) {
            if (!jsonObject.has("value")) {
                log.warn("Error while loading config value, boolean object doesnt have a value");
                return;
            }
            this.value = jsonObject.get("value").getAsBoolean();
            if (this.hudElement == null) {
                return;
            }
            double x = 0;
            double y = 0;
            boolean centeredX = false;
            boolean centeredY = false;
            if (jsonObject.has("x")) {
                x = jsonObject.get("x").getAsDouble();
            }
            if (jsonObject.has("y")) {
                y = jsonObject.get("y").getAsDouble();
            }
            if (jsonObject.has("centered_x")) {
                centeredX = jsonObject.get("centered_x").getAsBoolean();
            }
            if (jsonObject.has("centered_y")) {
                centeredY = jsonObject.get("centered_y").getAsBoolean();
            }
            this.hudElement.setPosition(new Position(x, y, centeredX, centeredY));
            this.hudElement.toggle(this.value);
            return;
        }
        if (!jsonElement.isJsonPrimitive()) {
            log.warn("Error while loading config value, expected boolean got %s".formatted(
                jsonElement.isJsonObject() ? "json-object" : "json-array"));
            return;
        }
        if (!jsonElement.getAsJsonPrimitive().isBoolean()) {
            log.warn("Error while loading config value, expected boolean got %s".formatted(jsonElement.getAsString()));
            return;
        }
        this.value = jsonElement.getAsBoolean();
    }

    @Override
    public @NotNull JsonElement save() {
        if (this.hudElement != null) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("value", this.value);
            jsonObject.addProperty("x", this.hudElement.getPosition().x());
            jsonObject.addProperty("y", this.hudElement.getPosition().y());
            jsonObject.addProperty("centered_x", this.hudElement.getPosition().centeredX());
            jsonObject.addProperty("centered_y", this.hudElement.getPosition().centeredY());
            return jsonObject;
        }
        return new JsonPrimitive(this.value);
    }

    @Override
    public @NotNull ConfigOptionEditor<Boolean, BooleanOption> getEditor() {
        return new BooleanEditor(this);
    }

}
