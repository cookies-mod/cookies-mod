package dev.morazzer.cookiesmod.mixin.render;

import dev.morazzer.cookiesmod.mixin.ItemModelOverrides;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ModelOverrideList.class)
public class ModelOverrideListMixin {

    @Shadow
    @Final
    private Identifier[] conditionTypes;

    @Inject(
        method = "apply",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/model/json/ModelOverrideList$BakedOverride;test([F)Z"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    private void inject(
        BakedModel model,
        ItemStack stack,
        ClientWorld world,
        LivingEntity entity,
        int seed,
        CallbackInfoReturnable<BakedModel> cir,
        Item item,
        int i,
        float[] fs,
        ModelOverrideList.BakedOverride[] var9,
        int var10,
        int var11,
        ModelOverrideList.BakedOverride bakedOverride
    ) {
        boolean respectOthers = true;
        for (Identifier conditionType : conditionTypes) {
            if (ItemModelOverrides.ITEM_MODEL_PREDICATE_DISABLE_RESPECT_FOR_OTHER.equals(conditionType)) {
                respectOthers = false;
                break;
            }
        }
        byte value = -1;
        for (int i1 = 0; i1 < bakedOverride.conditions.length; i1++) {
            ModelOverrideList.InlinedCondition condition = bakedOverride.conditions[i1];
            Identifier conditionType = this.conditionTypes[condition.index];
            if (ItemModelOverrides.shouldMatchPrecise(conditionType)) {
                boolean b = condition.threshold == fs[condition.index];
                fs[condition.index] = Float.NEGATIVE_INFINITY;
                if (respectOthers && ItemModelOverrides.shouldRespectOthers(conditionType)) {
                    if (value < 0) {
                        value = b ? (byte) 1 : (byte) 0;
                        continue;
                    }
                    value = (byte) ((b ? (byte) 1 : (byte) 0) * value);
                    continue;
                }
                if (b) {
                    cir.setReturnValue(bakedOverride.model);
                    cir.cancel();
                }
            }
        }
        if (value > 0) {
            cir.setReturnValue(bakedOverride.model);
            cir.cancel();
        }
    }

}

