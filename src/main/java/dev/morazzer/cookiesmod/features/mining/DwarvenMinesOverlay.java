package dev.morazzer.cookiesmod.features.mining;

import dev.morazzer.cookiesmod.commands.dev.subcommands.TestEntrypoint;
import dev.morazzer.cookiesmod.data.profile.ProfileData;
import dev.morazzer.cookiesmod.data.profile.ProfileStorage;
import dev.morazzer.cookiesmod.data.profile.mining.DwarvenMinesData;
import dev.morazzer.cookiesmod.events.api.PlayerListUpdateEvent;
import dev.morazzer.cookiesmod.features.hud.HudElement;
import dev.morazzer.cookiesmod.features.mining.commissions.CommissionManager;
import dev.morazzer.cookiesmod.utils.LocationUtils;
import dev.morazzer.cookiesmod.utils.NumberFormat;
import dev.morazzer.cookiesmod.utils.TabUtils;
import dev.morazzer.cookiesmod.utils.TimeUtils;
import dev.morazzer.cookiesmod.utils.general.CookiesUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;
import dev.morazzer.cookiesmod.utils.render.Position;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.profiler.Profiler;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Predicate;

public class DwarvenMinesOverlay extends HudElement {

    @TestEntrypoint("test_test_test")
    public static void test() {
        CookiesUtils.sendMessage(Text.translatable("skyblock.commissions.mithril_miner"));
    }

    private final List<Text> currentCommissions = new LinkedList<>();
    private volatile Text powderAmount = Text.empty();

    public DwarvenMinesOverlay() {
        super(new Position(0, 0));

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (overlay) return;
            if (!SkyblockUtils.isCurrentlyInSkyblock()) return;
            if (LocationUtils.getCurrentIsland() != LocationUtils.Islands.DWARVEN_MINES) return;
            switch (message.getString()) {
                case "\u00A7e[NPC] Fetchur\u00A7f: thanks thats probably what i needed", "\u00A7e[NPC] Fetchur\u00A7f: take some gifts!" ->
                        ProfileStorage.getCurrentProfile()
                                .ifPresent(profileData -> profileData.getDwarvenMinesData().lastFetchurTime = System.currentTimeMillis());
                case "\u00A7e[NPC] \u00A7dPuzzler\u00A7f: \u00A7a\u270C\u2713", "\u00A7e[NPC] \u00A7dPuzzler\u00A7f: \u25B6\u25B6Nice!  \u25B2Here, \u25C0have \u25BCsome\u25C0 \u25B6Mithril Powder!\u25B2" ->
                        ProfileStorage.getCurrentProfile()
                                .ifPresent(profileData -> profileData.getDwarvenMinesData().lastPuzzlerTime = System.currentTimeMillis());
            }
        });

        PlayerListUpdateEvent.UPDATE_NAME.register(currentEntry -> {
            if (!SkyblockUtils.isCurrentlyInSkyblock()) return;
            if (LocationUtils.getCurrentIsland() != LocationUtils.Islands.DWARVEN_MINES && SkyblockUtils.getLastServerSwap() + 5000 < System.currentTimeMillis()) {
                return;
            }

            if (TabUtils.getColumn(currentEntry) != 2) return;
            switch (TabUtils.getRow(currentEntry)) {
                case 6 -> this.handleMithrilPowder(currentEntry);
                case 10, 11, 12, 13 -> this.handleCommission(currentEntry);
            }
        });
    }

    private void handleCommission(PlayerListEntry currentEntry) {
        String[] split = Objects.requireNonNull(currentEntry.getDisplayName()).getString().split(":");
        if (split.length != 2) {
            return;
        }
        String commissionName = split[0];
        String commissionKey = CommissionManager.instance.getCommissionKey(commissionName.trim());
        if (commissionKey == null) return;
        int commissionAmount = CommissionManager.instance.getCommissionAmount(commissionKey);

        int index = TabUtils.getRow(currentEntry) - 10;
        if (currentCommissions.size() - 1 >= index) {
            currentCommissions.remove(index);
        }

        MutableText commission = Text.empty();
        commission.append(Text.translatable("skyblock.commission.%s".formatted(commissionName.trim().toLowerCase())))
                .append(": ");
        float percentage;
        if (split[1].trim().equals("DONE")) {
            percentage = 1f;
        } else {
            percentage = Float.parseFloat(split[1].replaceAll("[^0-9.]", ""));
        }
        int amountDone = (int) (commissionAmount * (percentage / 100f));

        commission.append(Text.literal(String.valueOf(amountDone)).append("/")
                .append(String.valueOf(commissionAmount)));


        if (currentCommissions.size() >= index - 1) {
            currentCommissions.add(index, commission);
        } else {
            currentCommissions.add(commission);
        }
    }

    private void handleMithrilPowder(PlayerListEntry currentEntry) {
        int powder = Integer.parseInt(Optional.ofNullable(currentEntry.getDisplayName()).map(Text::getString)
                .filter(Predicate.not(String::isBlank))
                .orElse("0").replaceAll("[^0-9]", ""));
        this.powderAmount = Text.literal("Mithril Powder: ")
                .append(Text.literal(NumberFormat.toFormattedString(powder)).formatted(Formatting.DARK_GREEN));
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    public String getIdentifierPath() {
        return "mining/dwarven_overlay";
    }

    @Override
    public boolean shouldRender() {
        return LocationUtils.getCurrentIsland() == LocationUtils.Islands.DWARVEN_MINES;
    }

    private int index = 0;

    @Override
    protected void renderOverlay(DrawContext drawContext, float delta) {
        this.index = 0;
        Profiler profiler = MinecraftClient.getInstance().getProfiler();
        profiler.push("puzzler");
        this.renderPuzzlerTimers(drawContext);
        profiler.swap("fetchur");
        this.renderFetchurTimer(drawContext);
        profiler.swap("commissions");
        this.renderCommissions(drawContext);
        profiler.swap("powder");
        this.renderPowder(drawContext);
        profiler.pop();
    }

    long nextFetchur = 0;

    private void renderFetchurTimer(DrawContext drawContext) {
        if (this.nextFetchur < System.currentTimeMillis()) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Canada/Eastern")));
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.DATE, 1);
            this.nextFetchur = calendar.getTimeInMillis();
        }

        long lastFetchurTime = ProfileStorage.getCurrentProfile().map(ProfileData::getDwarvenMinesData)
                .map(DwarvenMinesData::getLastFetchurTime).orElse(-1L);

        MutableText fetchur = Text.literal("Fetchur: ").formatted(Formatting.DARK_GREEN);
        long timeDelta = this.nextFetchur - System.currentTimeMillis();

        if (lastFetchurTime == -1) {
            fetchur.append(Text.literal("Unknown").formatted(Formatting.RED));
        } else if (timeDelta <= 0) {
            fetchur.append(Text.literal("Ready").formatted(Formatting.GREEN));
        } else {
            fetchur.append(TimeUtils.toFormattedTimeText(timeDelta / 1000)
                    .setStyle(Style.EMPTY.withColor(getColor(timeDelta / 1000))));
        }

        drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                fetchur,
                0,
                10 * this.index++,
                -1,
                true
        );
    }

    private void renderPowder(DrawContext drawContext) {
        drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                this.powderAmount,
                0,
                10 * this.index++,
                -1,
                true
        );
    }

    private void renderCommissions(DrawContext drawContext) {
        for (Text currentCommission : this.currentCommissions) {
            drawContext.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    currentCommission,
                    0,
                    10 * this.index++,
                    -1,
                    true
            );
        }
    }

    private int getColor(long seconds) {
        float DAY = 60 * 60 * 24;
        float percentage = seconds / DAY;
        int g = 0x8DF3B1;
        int r = 0xFF5757;

        int redStart = (g >> 16) & 0xFF;
        int greenStart = (g >> 8) & 0xFF;
        int blueStart = (g) & 0xFF;

        int red = (int) ((((r >> 16) & 0xFF) - redStart) * percentage);
        int green = (int) ((((r >> 8) & 0xFF) - greenStart) * percentage);
        int blue = (int) ((((r) & 0xFF) - blueStart) * percentage);

        return (redStart + red) << 16 | (greenStart + green) << 8 | (blueStart + blue);
    }

    private void renderPuzzlerTimers(DrawContext drawContext) {
        long lastPuzzlerTime = ProfileStorage.getCurrentProfile().map(ProfileData::getDwarvenMinesData)
                .map(dwarvenMinesData -> dwarvenMinesData.lastPuzzlerTime).orElse(-1L);
        MutableText puzzler = Text.literal("Puzzler: ").formatted(Formatting.DARK_PURPLE);
        long timeDelta = (24 * 60 * 60) - ((System.currentTimeMillis() - lastPuzzlerTime) / 1000);

        if (lastPuzzlerTime == -1) {
            puzzler.append(Text.literal("Unknown").formatted(Formatting.RED));
        } else if (timeDelta <= 0) {
            puzzler.append(Text.literal("Ready").formatted(Formatting.GREEN));
        } else {
            puzzler.append(TimeUtils.toFormattedTimeText(timeDelta)
                    .setStyle(Style.EMPTY.withColor(getColor(timeDelta))));
        }

        drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                puzzler,
                0,
                10 * this.index++,
                -1,
                true
        );
    }
}
