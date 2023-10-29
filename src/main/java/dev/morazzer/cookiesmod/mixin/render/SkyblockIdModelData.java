package dev.morazzer.cookiesmod.mixin.render;

import dev.morazzer.cookiesmod.mixin.ItemModelOverrides;
import dev.morazzer.cookiesmod.utils.general.ItemUtils;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ModelPredicateProviderRegistry.class)
public abstract class SkyblockIdModelData {

    @Shadow
    @Final
    private static Map<Identifier, ModelPredicateProvider> GLOBAL;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void staticBlock(CallbackInfo ci) {
        GLOBAL.put(
                ItemModelOverrides.ITEM_MODEL_PREDICATE_SKYBLOCK_ID,
                (stack, world, entity, seed) -> ItemUtils
                        .getSkyblockId(stack)
                        .map(String::hashCode)
                        .map(Integer::floatValue)
                        .orElse(Float.NEGATIVE_INFINITY)
        );
        GLOBAL.put(ItemModelOverrides.ITEM_MODEL_PREDICATE_REFORGE, (stack, world, entity, seed) -> {
            Optional<NbtCompound> skyblockAttributes = ItemUtils.getSkyblockAttributes(stack);
            Optional<String> s = skyblockAttributes.map(attributes -> attributes.getString("modifier"));
            return s
                    .filter(Predicate.not(String::isEmpty))
                    .map(String::hashCode)
                    .map(Integer::floatValue)
                    .orElse(Float.NEGATIVE_INFINITY);
        });
        GLOBAL.put(
                ItemModelOverrides.ITEM_MODEL_PREDICATE_DISPLAY_NAME,
                (stack, world, entity, seed) -> stack.getName().getString().hashCode()
        );
        GLOBAL.put(
                ItemModelOverrides.ITEM_MODEL_PREDICATE_DISABLE_RESPECT_FOR_OTHER,
                (stack, world, entity, seed) -> Float.NEGATIVE_INFINITY
        );
    }

}
