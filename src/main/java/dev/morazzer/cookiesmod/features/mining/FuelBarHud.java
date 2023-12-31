package dev.morazzer.cookiesmod.features.mining;

import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.features.hud.HudElement;
import dev.morazzer.cookiesmod.features.repository.constants.MiningData;
import dev.morazzer.cookiesmod.utils.general.ItemUtils;
import dev.morazzer.cookiesmod.utils.maths.LinearInterpolatedInteger;
import dev.morazzer.cookiesmod.utils.render.Position;
import java.util.Optional;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * Hud element to render the fuel in a drill as bar.
 */
public class FuelBarHud extends HudElement {

    private static final LinearInterpolatedInteger interpolatedFuelAmount = new LinearInterpolatedInteger(10000, 0);
    private static final Identifier FUEL_BAR_EMPTY = new Identifier("cookiesmod:hud/mining/fuel/bar.png");
    private static final Identifier FUEL_BAR_FILLED = new Identifier("cookiesmod:hud/mining/fuel/bar_filled.png");

    public FuelBarHud() {
        super(new Position(0, -100, true, true));
    }

    @Override
    public int getWidth() {
        return 183;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public String getIdentifierPath() {
        return "mining/fuel_bar";
    }

    @Override
    public boolean shouldRender() {
        return ItemUtils.hasSkyblockItemInMainHand() && ItemUtils
            .getMainHand()
            .flatMap(ItemUtils::getSkyblockIdAsIdentifier)
            .map(MiningData.getInstance().getDrills()::contains)
            .orElse(false);
    }

    @Override
    protected void renderOverlay(DrawContext drawContext, float delta) {
        if (isCurrentlyEditing()) {
            if (interpolatedFuelAmount.hasReachedTarget()) {
                interpolatedFuelAmount.setTargetValue(interpolatedFuelAmount.getTarget() == 0 ? 100000 : 0);
            }
            interpolatedFuelAmount.tick();
            renderBar(drawContext, interpolatedFuelAmount.getValue() / 100000f);
            return;
        }

        Optional<NbtCompound> optionalAttributes = ItemUtils.getMainHand().flatMap(ItemUtils::getSkyblockAttributes);
        if (optionalAttributes.isEmpty()) {
            return;
        }
        NbtCompound extraAttributes = optionalAttributes.get();

        final int maxFuel = ItemUtils.skyblockIdToIdentifier(extraAttributes.getString("drill_part_fuel_tank"))
            .map(MiningData.getInstance().getParts()::get)
            .orElse(3000);

        if (!extraAttributes.contains("drill_fuel")) {
            return;
        }

        final int currentFuel = extraAttributes.getInt("drill_fuel");
        final float fuelPercentage = (float) currentFuel / maxFuel;

        renderBar(drawContext, fuelPercentage);
    }

    /**
     * Renders the fuel bar.
     *
     * @param drawContext    The current draw context.
     * @param fuelPercentage The percentage of fuel in the drill.
     */
    private void renderBar(DrawContext drawContext, float fuelPercentage) {
        drawContext.setShaderColor(1 - fuelPercentage, fuelPercentage, 1 / 255f, 1);


        drawContext.drawTexture(FUEL_BAR_EMPTY, 0, 5, 0, 0, 0, 182, 5, 182, 5);
        if (fuelPercentage > 0) {
            drawContext.drawTexture(
                FUEL_BAR_FILLED,
                0,
                5,
                0,
                0,
                (int) (fuelPercentage * 182.0f),
                5,
                183,
                5
            );
        }
        String text = "Drill Fuel";
        if (ConfigManager.getConfig().miningCategory.showDrillFuelPercentage.getValue()) {
            text += " (%.2f%s)".formatted(fuelPercentage * 100, "%");
        }

        drawContext.drawText(
            MinecraftClient.getInstance().textRenderer,
            text,
            3,
            -2,
            -1,
            true
        );
        drawContext.setShaderColor(1, 1, 1, 1);
    }

}
