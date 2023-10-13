package dev.morazzer.cookiesmod.features.dungeons;

import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.events.api.ExplosionLargeParticleCallback;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.general.CookiesUtils;
import dev.morazzer.cookiesmod.utils.general.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.Optional;

@LoadModule("misc/implosion_hider")
public class ImplosionHider implements Module {
    @Override
    public void load() {
        ExplosionLargeParticleCallback.EVENT.register((world, x, y, z, d, spriteProvider) -> {
            if (!ConfigManager.getConfig().dungeonCategory.implosionHider.getValue()) return true;
            Optional<ClientPlayerEntity> player = CookiesUtils.getPlayer();
            if (player.isEmpty()) return true;
            Vec3d vec3d = new Vec3d(x, y, z);
            try {
                for (PlayerEntity playerEntity : MinecraftClient.getInstance().world != null ? MinecraftClient.getInstance().world.getPlayers() : Collections.<PlayerEntity>emptyList()) {
                    ItemStack mainHandStack = playerEntity.getMainHandStack();
                    NbtCompound orCreateNbt = mainHandStack.getOrCreateNbt();
                    if (orCreateNbt.isEmpty()) continue;
                    Optional<String> skyblockId = ItemUtils.getSkyblockId(mainHandStack);
                    if (skyblockId.isEmpty()) return true;
                    String id = skyblockId.get();
                    if (!(id.equals("NECRON_BLADE") || id.equals("HYPERION") || id.equals("ASTRAEA") || id.equals(
                            "VALKYRIE") || id.equals("SCYLLA"))) {
                        return true;
                    }
                    if (vec3d.squaredDistanceTo(playerEntity.getPos()) < 16) {
                        return false;
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.handleException(e);
            }
            //double v = .distanceTo(clientPlayerEntity.getPos());
            return true;
        });
    }

    @Override
    public String getIdentifierPath() {
        return "misc/implosion_hider";
    }
}
