package dev.morazzer.cookiesmod.utils;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.commands.dev.subcommands.TestEntrypoint;
import dev.morazzer.cookiesmod.utils.general.CookiesUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.session.telemetry.TelemetrySender;
import net.minecraft.client.session.telemetry.WorldSession;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class NpcUtils {

    /**
     * Print the textures of the targeted player.
     */
    @TestEntrypoint("get_texture_from_player")
    public static void printTextureFromPlayer() {
        HitResult raycast = MinecraftClient.getInstance().crosshairTarget;
        if (raycast == null) {
            return;
        }
        if (raycast.getType() != HitResult.Type.ENTITY) {
            return;
        }

        EntityHitResult entityHitResult = (EntityHitResult) raycast;
        Entity entity = entityHitResult.getEntity();
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        PlayerEntity playerEntity = (PlayerEntity) entity;
        Collection<Property> textures = playerEntity.getGameProfile().getProperties().get("textures");
        for (Property texture : textures) {
            String value = new String(Base64.getDecoder().decode(texture.value()));
            JsonObject jsonObject = GsonUtils.gsonClean.fromJson(value, JsonObject.class);
            CookiesUtils.sendMessage(TextUtils.prettyPrintJson(jsonObject));
            CookiesUtils.sendMessage(CookiesMod.createPrefix(-1).append("Base64 encoded profile: ")
                    .append(Text.literal("[COPY]")
                            .setStyle(
                                    Style.EMPTY.withClickEvent(new ClickEvent(
                                            ClickEvent.Action.COPY_TO_CLIPBOARD,
                                            texture.value()
                                    )).withHoverEvent(new HoverEvent(
                                            HoverEvent.Action.SHOW_TEXT,
                                            Text.literal(texture.value())
                                    )).withColor(Formatting.YELLOW)
                            )
                    )
            );
        }
        for (String key : playerEntity.getGameProfile().getProperties().keys()) {
            Collection<Property> properties = playerEntity.getGameProfile().getProperties().get(key);
            if (key.equals("textures")) {
                continue;
            }
            CookiesUtils.sendMessage(CookiesMod.createPrefix(-1).append("Values for key ").append(key)
                    .append(Arrays.toString(properties.toArray())));
        }
    }

    /**
     * Create a npc that can be rendered onto the hud.
     *
     * @param name          The name of the npc.
     * @param textureString The texture of the npc.
     * @return The npc.
     */
    public static PlayerEntity createRenderableNpc(String name, String textureString) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
        gameProfile.getProperties().put("textures", new Property("textures", textureString));
        return new CookiesRenderablePlayer(gameProfile);
    }

    private static class CookiesRenderablePlayer extends ClientPlayerEntity {

        private SkinTextures skinTextures;

        public CookiesRenderablePlayer(GameProfile gameProfile) {
            super(
                    MinecraftClient.getInstance(),
                    Optional.ofNullable(MinecraftClient.getInstance().world).orElseThrow(),
                    new ClientPlayNetworkHandler(MinecraftClient.getInstance(), new ClientConnection(
                            NetworkSide.CLIENTBOUND), new ClientConnectionState(
                            gameProfile,
                            new WorldSession(TelemetrySender.NOOP, false,
                                    Duration.ZERO, ""
                            ),
                            MinecraftClient.getInstance().world.getRegistryManager().toImmutable(),
                            MinecraftClient.getInstance().world.getEnabledFeatures(),
                            "Cookies player",
                            null,
                            null
                    )),
                    null,
                    null,
                    false,
                    false
            );
            this.skinTextures = DefaultSkinHelper.getTexture(gameProfile);
            this.client.getSkinProvider().fetchSkinTextures(this.getGameProfile())
                    .thenAccept(skinTextures -> this.skinTextures = skinTextures);
        }

        @Override
        public boolean isPartVisible(PlayerModelPart modelPart) {
            return true;
        }

        @Nullable
        @Override
        protected PlayerListEntry getPlayerListEntry() {
            return null;
        }

        @Override
        public SkinTextures getSkinTextures() {
            return this.skinTextures;
        }

    }

}
