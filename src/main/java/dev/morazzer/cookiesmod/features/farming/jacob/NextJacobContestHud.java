package dev.morazzer.cookiesmod.features.farming.jacob;

import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.config.categories.farming.JacobFoldable;
import dev.morazzer.cookiesmod.config.system.options.SliderOption;
import dev.morazzer.cookiesmod.features.farming.Crop;
import dev.morazzer.cookiesmod.features.farming.garden.Garden;
import dev.morazzer.cookiesmod.features.hud.HudElement;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItemManager;
import dev.morazzer.cookiesmod.utils.TimeUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;
import dev.morazzer.cookiesmod.utils.render.Position;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.SimpleDateFormat;
import java.time.Instant;

/**
 * Hud element to display the next n contests on the screen.
 */
public class NextJacobContestHud extends HudElement {

    final SimpleDateFormat formatAmPm = new SimpleDateFormat("h:mm a");
    final SimpleDateFormat format24h = new SimpleDateFormat("kk:mm");
    int amountToDisplay = 0;
    JacobFoldable.TimestampFormat timestampFormat;
    boolean onGarden;

    public NextJacobContestHud() {
        super(new Position(100, 100));
    }

    /**
     * Changes the used time format.
     *
     * @param format The new format
     */
    public void updateTimestampFormat(JacobFoldable.TimestampFormat format) {
        this.timestampFormat = format;
    }

    @Override
    public void init() {
        SliderOption<Integer> showContestAmount = ConfigManager.getConfig().gardenCategory.jacobFoldable.showContestAmount;
        this.amountToDisplay = showContestAmount.getNumberTransformer().parseNumber(showContestAmount.getValue());
        this.timestampFormat = ConfigManager.getConfig().gardenCategory.jacobFoldable.nextContestTimestampFormat.getValue();
        this.onGarden = ConfigManager.getConfig().gardenCategory.jacobFoldable.onlyShowOnGarden.getValue();
        ConfigManager.getConfig().gardenCategory.jacobFoldable.nextContestTimestampFormat
                .withCallback((oldValue, newValue) -> this.updateTimestampFormat(newValue));
        ConfigManager.getConfig().gardenCategory.jacobFoldable.showContestAmount
                .withCallback((oldValue, newValue) -> this.updateContestAmount(newValue));
        ConfigManager.getConfig().gardenCategory.jacobFoldable.onlyShowOnGarden.withCallback((oldValue, newValue) -> this.onGarden = newValue);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return this.amountToDisplay * 16;
    }

    @Override
    public String getIdentifierPath() {
        return "farming/next_jacob_contest";
    }

    @Override
    public boolean shouldRender() {
        return SkyblockUtils.isCurrentlyInSkyblock() && (this.onGarden || Garden.isOnGarden());
    }

    @Override
    protected void renderOverlay(DrawContext drawContext, float delta) {
        int row = 0;
        for (Contest contest : JacobsContests.getInstance().getNextNContestsActiveOrFuture(this.amountToDisplay)) {
            int cropIndex = 0;
            for (Crop crop : contest.crops()) {
                drawContext.drawItem(
                        RepositoryItemManager.getItem(crop.getIdentifier()).getItemStack(),
                        cropIndex * 16,
                        row * 16
                );
                cropIndex++;
            }

            final Text text;
            if (contest.time().isCurrentDay()) {
                text = Text.literal("NOW").formatted(Formatting.DARK_GREEN);
            } else {
                text = switch (this.timestampFormat) {
                    case RELATIVE -> Text.literal("in ").append(TimeUtils.toFormattedTime(contest.time().getInstant()
                            .minusSeconds(Instant.now().getEpochSecond()).getEpochSecond()).trim());
                    case ABSOLUTE_AM_PM ->
                            Text.literal(formatAmPm.format(contest.time().getInstant().getEpochSecond() * 1000));
                    case ABSOLUTE ->
                            Text.literal(format24h.format(contest.time().getInstant().getEpochSecond() * 1000));
                };

            }


            drawContext.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    Text.literal("(").append(text)
                            .append(")"),
                    cropIndex * 16 + 2,
                    row * 16 + 4,
                    -1,
                    true
            );
            row++;
        }
    }

    /**
     * Updates the number of contests to display.
     *
     * @param number The number of contests.
     */
    private void updateContestAmount(int number) {
        this.amountToDisplay = number;
    }

}
