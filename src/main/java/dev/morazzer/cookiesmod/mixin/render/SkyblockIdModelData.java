package dev.morazzer.cookiesmod.mixin.render;

import dev.morazzer.cookiesmod.utils.general.ItemUtils;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelPredicateProviderRegistry.class)
public abstract class SkyblockIdModelData {


    @Shadow @Final private static Map<Identifier, ModelPredicateProvider> GLOBAL;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void staticBlock(CallbackInfo ci) {
        GLOBAL.put(
                RenderUtils.itemModelPredicate,
                (stack, world, entity, seed) -> ItemUtils.getSkyblockId(stack)
                        .map(String::hashCode)
                        .map(f -> f / 1000000f)
                        .orElse(Float.NEGATIVE_INFINITY)
        );
    }

}
