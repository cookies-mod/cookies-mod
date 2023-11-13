package dev.morazzer.cookiesmod.features.mining.crystalhollows.hud;

import dev.morazzer.cookiesmod.data.profile.ProfileData;
import dev.morazzer.cookiesmod.data.profile.ProfileStorage;
import dev.morazzer.cookiesmod.data.profile.mining.DwarvenMinesData;
import dev.morazzer.cookiesmod.features.hud.HudElement;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import dev.morazzer.cookiesmod.utils.TimeUtils;
import dev.morazzer.cookiesmod.utils.render.Position;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

/**
 * HUD element that shows the time remaining on the mining pass.
 */
public class MiningPassDurationHud extends HudElement {
    private static final long PASS_DURATION = 1000 * 60 * 60 * 6;

    public MiningPassDurationHud() {
        super(new Position(200, -200, true, true));
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public String getIdentifierPath() {
        return "mining/crystalhollows/pass_duration";
    }

    @Override
    public boolean shouldRender() {
        return LocationUtils.getCurrentIsland() == LocationUtils.Islands.CRYSTAL_HOLLOWS;
    }

    @Override
    protected void renderOverlay(DrawContext drawContext, float tickDelta) {
        Text literal = getText();
        drawContext.drawText(MinecraftClient.getInstance().textRenderer, literal, 0, 0, -1, true);
    }

    protected Text getText() {
        MutableText literal = Text.literal("Pass Time Remaining: ");
        long storedTime = ProfileStorage.getCurrentProfile().map(
            ProfileData::getDwarvenMinesData).map(DwarvenMinesData::getCrystalHollowsPassBoughtTime).orElse(-1L);

        if (storedTime == -1L) {
            literal.append(Text.literal("Unknown"));
            return literal;
        }

        long timeRemaining = PASS_DURATION - (System.currentTimeMillis() - storedTime);

        if (timeRemaining <= 0) {
            literal.append(Text.literal("Expired"));
            return literal;
        }

        literal.append(TimeUtils.toFormattedTimeText(timeRemaining / 1000).setStyle(
            Style.EMPTY.withColor(
                ColorUtils.getColor(timeRemaining, PASS_DURATION, ColorUtils.failColor, ColorUtils.successColor))));
        return literal;
    }
}
