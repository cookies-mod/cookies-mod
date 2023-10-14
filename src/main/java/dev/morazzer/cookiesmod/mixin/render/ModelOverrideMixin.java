package dev.morazzer.cookiesmod.mixin.render;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.DataResult;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(targets = "net.minecraft.client.render.model.json.ModelOverride$Deserializer")
public class ModelOverrideMixin {

    @Inject(method = "deserializeMinPropertyValues", at = @At(value = "INVOKE", target = "Ljava/util/Map$Entry;getValue()Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void allowStringPropertiesForAllCookiesmodIdentifiers(
            JsonObject object, CallbackInfoReturnable<List<ModelOverride.Condition>> cir, Map<Identifier, Float> map,
            JsonObject jsonObject, Iterator<?> iterator, Map.Entry<String, JsonElement> entry) {
        DataResult<Identifier> identifier = Identifier.validate(entry.getKey());
        if (identifier.result().isEmpty() || !identifier.result().get().getNamespace().equals("cookiesmod")) {
            return;
        }
        if (entry.getValue().isJsonPrimitive() && entry.getValue().getAsJsonPrimitive().isString()) {
            entry.setValue(new JsonPrimitive(entry.getValue().getAsString().hashCode()));
        }
    }
}
