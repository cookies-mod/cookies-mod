package dev.morazzer.cookiesmod.features.farming.garden;

import dev.morazzer.cookiesmod.features.hud.HudElement;
import dev.morazzer.cookiesmod.utils.general.CookiesUtils;
import dev.morazzer.cookiesmod.utils.general.ItemUtils;
import dev.morazzer.cookiesmod.utils.render.Position;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

/**
 * HUD element to display the current yaw/pitch on the screen.
 */
public class YawPitchDisplay extends HudElement {

    private final Text yaw = Text.literal("Yaw: ").formatted(Formatting.AQUA);
    private final Text pitch = Text.literal("Pitch: ").formatted(Formatting.AQUA);

    double lastYaw = 0;
    double lastPitch = 0;
    long lastUpdated = System.currentTimeMillis();

    public YawPitchDisplay() {
        super(new Position(0, 100));
    }

    @Override
    public int getWidth() {
        return 70;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public String getIdentifierPath() {
        return "farming/yaw_pitch";
    }

    @Override
    public boolean shouldRender() {
        this.updateLastYawPitch();
        return Garden.isOnGarden()
            && this.lastUpdated + 5000 > System.currentTimeMillis()
            && ItemUtils.hasSkyblockItemInMainHand()
            && ItemUtils.doesCurrentItemHaveEnchantments("cultivating", "replenish");
    }

    @Override
    protected void renderOverlay(DrawContext drawContext, float delta) {
        this.updateLastYawPitch();
        double yaw = MathHelper.wrapDegrees(CookiesUtils.getPlayer().map(ClientPlayerEntity::getYaw).orElse(0f));
        double pitch = CookiesUtils.getPlayer().map(ClientPlayerEntity::getPitch).orElse(0f);

        drawContext.drawText(
            MinecraftClient.getInstance().textRenderer,
            this.yaw.copy().append(Text.literal("%.4f".formatted(yaw))
                .formatted(Formatting.WHITE)),
            0,
            0,
            -1,
            true
        );
        drawContext.drawText(
            MinecraftClient.getInstance().textRenderer,
            this.pitch.copy().append(Text.literal("%.4f".formatted(pitch))
                .formatted(Formatting.WHITE)),
            0,
            8,
            -1,
            true
        );
    }

    /**
     * Update yaw and pitch that is saved.
     */
    private void updateLastYawPitch() {
        double yaw = MathHelper.wrapDegrees(CookiesUtils.getPlayer().map(ClientPlayerEntity::getYaw).orElse(0f));
        double pitch = CookiesUtils.getPlayer().map(ClientPlayerEntity::getPitch).orElse(0f);
        if (this.lastYaw != yaw
            || this.lastPitch != pitch) {
            this.lastPitch = pitch;
            this.lastYaw = yaw;
            this.lastUpdated = System.currentTimeMillis();
        }
    }

}
