package dev.morazzer.cookiesmod.features.farming.garden.desk;

import com.google.common.collect.Lists;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.features.farming.garden.Garden;
import dev.morazzer.cookiesmod.features.repository.constants.CompostData;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

@LoadModule
public class PlotPriceDisplay implements Module {

	private static final int[][] costs = new int[][]{
			{13, 21, 23, 31}, // inner
			{12, 14, 30, 32}, // middle
			{3, 4, 5, 11, 15, 20, 24, 29, 33, 39, 40, 41}, // edges
			{2, 6, 38, 42} // corner
	};

	private final NumberFormat numberFormat = new DecimalFormat();
	private long lastRefresh = System.currentTimeMillis();
	private List<OrderedText> lines;
	private int lineWidth = 0;

	@Override
	public void load() {
		ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (!ConfigManager.getConfig().gardenCategory.plotBreakdown.getValue()) return;
			if (!(screen instanceof HandledScreen<?>)) return;
			if (!Garden.isOnGarden()) return;
			if (!screen.getTitle().getString().equals("Configure Plots")) return;
			if (!CompostData.loaded()) return;

			ScreenEvents.afterRender(screen)
					.register(ExceptionHandler.wrap(this::modifyItems));
		});
	}

	private void modifyItems(Screen screen, DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
		HandledScreen<?> handledScreen = (HandledScreen<?>) screen;

		if (this.lastRefresh + 10000 < System.currentTimeMillis()) {
			updateList(handledScreen);
		}

		drawContext.drawTooltip(
				MinecraftClient.getInstance().textRenderer,
				this.lines,
				HoveredTooltipPositioner.INSTANCE,
				handledScreen.x - this.lineWidth - 32,
				handledScreen.y + 16
		);
	}

	public void updateList(HandledScreen<?> handledScreen) {
		int missingCompost = 0;
		int missingBundles = 0;
		int missingPlots = 0;
		int ownedPlots = 0;
		for (int index = 0; index < 4; index++) {
			List<CompostData.Cost> cost = CompostData.getInstance().getByIndex(index);
			int amountBought = 0;
			int amountMissing = 0;
			for (int groupIndex = 0; groupIndex < costs[index].length; groupIndex++) {
				Slot slot = handledScreen.getScreenHandler().slots.get(costs[index][groupIndex]);
				if (slot.getStack().isEmpty()) {
					continue;
				}
				if (slot.getStack().getItem() == Items.OAK_BUTTON
						|| slot.getStack().getItem() == Items.RED_STAINED_GLASS_PANE) {
					amountMissing++;
				} else {
					amountBought++;
				}
			}
			missingPlots += amountMissing;
			ownedPlots += amountBought;

			for (int compostIndex = 0; compostIndex < amountMissing; compostIndex++) {
				CompostData.Cost slotCost = cost.get(amountBought + compostIndex);
				if (slotCost.bundle()) {
					missingBundles += slotCost.amount();
				} else {
					missingCompost += slotCost.amount();
				}
			}
		}

		int regularCompost = missingCompost + missingBundles * 160;

		List<MutableText> breakdown = new LinkedList<>();

		breakdown.add(Text.empty());
		breakdown.add(Text.literal("Plots missing: ").formatted(Formatting.GRAY)
				.append(Text.literal(String.valueOf(missingPlots)).formatted(
						Formatting.GOLD)));
		breakdown.add(Text.literal("Plots owned: ").formatted(Formatting.GRAY)
				.append(Text.literal(String.valueOf(ownedPlots)).formatted(
						Formatting.GOLD)));
		breakdown.add(Text.empty());
		breakdown.add(Text.empty());
		breakdown.add(Text.empty().append(Text.literal(numberFormat.format(missingCompost)).formatted(Formatting.GOLD)
				.append("x ")).append(Text.literal("Compost").formatted(Formatting.GREEN)));
		breakdown.add(Text.empty().append(Text.literal(numberFormat.format(missingBundles)).formatted(Formatting.GOLD)
				.append("x ")).append(Text.literal("Compost Bundle").formatted(Formatting.BLUE)));
		breakdown.add(Text.literal(" -> ")
				.append(Text.literal(numberFormat.format(missingBundles * 160L)).formatted(Formatting.GOLD)
						.append("x "))
				.append(Text.literal("Compost").formatted(Formatting.GREEN)).formatted(Formatting.DARK_GRAY));
		breakdown.add(Text.literal("Missing: ").formatted(Formatting.GRAY)
				.append(Text.literal(numberFormat.format(regularCompost)).formatted(Formatting.GOLD)
						.append("x ")).append(Text.literal("Compost").formatted(Formatting.GREEN)));


		int maxLineWidth = 0;
		for (MutableText mutableText : breakdown) {
			maxLineWidth = Math.max(maxLineWidth, MinecraftClient.getInstance().textRenderer.getWidth(mutableText));
		}

		int equalsWidth = MinecraftClient.getInstance().textRenderer.getWidth(" ");
		breakdown.add(
				breakdown.size() - 1,
				Text.literal(StringUtils.leftPad("", maxLineWidth / equalsWidth, " "))
						.formatted(Formatting.STRIKETHROUGH, Formatting.BLUE)
		);

		Text breakdownLine = Text.literal("Breakdown").formatted(Formatting.BOLD, Formatting.BLUE);
		int breakdownWidth = MinecraftClient.getInstance().textRenderer.getWidth(breakdownLine);

		breakdown.add(
				0,
				Text.literal(StringUtils.leftPad("", ((maxLineWidth - breakdownWidth) / 2) / equalsWidth))
						.append(breakdownLine)
		);

		Text compostBreakdownLine = Text.literal("Compost Breakdown").formatted(Formatting.BOLD, Formatting.BLUE);
		int compostBreakdownWidth = MinecraftClient.getInstance().textRenderer.getWidth(compostBreakdownLine);
		breakdown.add(
				5,
				Text.literal(StringUtils.leftPad("", ((maxLineWidth - compostBreakdownWidth) / 2) / equalsWidth))
						.append(compostBreakdownLine)
		);


		this.lines = Lists.transform(breakdown, Text::asOrderedText);
		this.lastRefresh = System.currentTimeMillis();
		this.lineWidth = maxLineWidth;
	}

	@Override
	public String getIdentifierPath() {
		return "farming/garden/desk/price_breakdown";
	}
}
