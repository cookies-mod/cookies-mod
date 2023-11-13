package dev.morazzer.cookiesmod.features.farming.garden.composter;

import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.config.categories.farming.CompostFoldable;
import dev.morazzer.cookiesmod.features.farming.garden.Garden;
import dev.morazzer.cookiesmod.features.farming.garden.Plot;
import dev.morazzer.cookiesmod.features.repository.constants.CompostUpgradeCost;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItemManager;
import dev.morazzer.cookiesmod.features.repository.items.item.SkyblockItem;
import dev.morazzer.cookiesmod.features.repository.items.recipe.Ingredient;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.RomanNumerals;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Breakdown for the composter cost in the garden.
 */
@LoadModule("farming/garden/compost_upgrades")
public class CompostUpgradeBreakdown implements Module {

    @Override
    public void load() {
        ItemTooltipCallback.EVENT.register(ExceptionHandler.wrap(this::tooltips));
    }

    @Override
    public String getIdentifierPath() {
        return "farming/garden/compost_upgrades";
    }

    /**
     * Checks if an item is a composter upgrade and if so, then add lore to it.
     *
     * @param itemStack      The item to check for.
     * @param tooltipContext The tooltip context.
     * @param texts          The current lore.
     */
    private void tooltips(ItemStack itemStack, TooltipContext tooltipContext, List<Text> texts) {
        if (!Garden.isOnGarden()) {
            return;
        }
        if (!Plot.getCurrentPlot().isBarn()) {
            return;
        }
        if (!ConfigManager.getConfig().gardenCategory.compostFoldable.showUpgradeCost.getValue()) {
            return;
        }
        if (!CompostUpgradeCost.loaded()) {
            return;
        }
        if (!itemStack.hasCustomName()) {
            return;
        }

        LinkedList<Text> list = new LinkedList<>();

        String name = itemStack.getName().getString();
        if (!name.startsWith("Composter Speed")
            && !name.startsWith("Multi Drop")
            && !name.startsWith("Fuel Cap")
            && !name.startsWith("Organic Matter Cap")
            && !name.startsWith("Cost Reduction")) {
            return;
        }
        String upgradeName = name.replaceAll(
            "(Composter Speed|Multi Drop|Fuel Cap|Organic Matter Cap|Cost Reduction).*",
            "$1"
        );
        String lastPart = name.substring(upgradeName.length()).trim();
        int currentLevel;
        if (lastPart.isEmpty()) {
            currentLevel = 0;
        } else {
            currentLevel = RomanNumerals.romanToArabic(lastPart.trim());
        }

        String maxAmount;
        List<CompostUpgradeCost.CompostUpgrade> upgrades;
        switch (upgradeName.trim()) {
            case "Composter Speed" -> {
                maxAmount = "500%";
                upgrades = CompostUpgradeCost.getInstance().getSpeed();
            }
            case "Multi Drop" -> {
                maxAmount = "75%";
                upgrades = CompostUpgradeCost.getInstance().getMultiDrop();
            }
            case "Fuel Cap" -> {
                maxAmount = "850,000";
                upgrades = CompostUpgradeCost.getInstance().getFuelCap();
            }
            case "Organic Matter Cap" -> {
                maxAmount = "540,000";
                upgrades = CompostUpgradeCost.getInstance().getOrganicMatterCap();
            }
            case "Cost Reduction" -> {
                maxAmount = "25%";
                upgrades = CompostUpgradeCost.getInstance().getCostReduction();
            }
            default -> {
                return;
            }
        }

        for (Text text : texts) {
            list.add(text);
            if (text.getString().startsWith("Next Tier: ")) {
                list.add(Text.literal("Max Tier: ").formatted(Formatting.GRAY)
                    .append(Text.literal(maxAmount).formatted(Formatting.GREEN)));
            } else if (text.getString().startsWith("+")) {
                list.add(Text.empty());
                list.add(Text.literal("Remaining Upgrade Cost: ").formatted(Formatting.GRAY));
                List<CompostUpgradeCost.CompostUpgrade> subList = upgrades.subList(Math.min(
                    currentLevel,
                    upgrades.size()
                ), upgrades.size());
                List<Ingredient> subListIngredients = subList.stream()
                    .flatMap(compostUpgrade -> compostUpgrade.cost().stream()).toList();
                Stream<Ingredient> stream = Ingredient.mergeToList(subListIngredients).stream()
                    .sorted(Comparator.comparingInt(Ingredient::getAmount));

                if (ConfigManager.getConfig().gardenCategory.compostFoldable.itemSort.getValue()
                    == CompostFoldable.ItemSortMode.DOWN) {
                    stream = stream.sorted(Comparator.comparingInt(Ingredient::getAmount).reversed());
                }

                stream.forEach(ingredient -> {
                    MutableText entry = Text.literal("  ");
                    Optional<SkyblockItem> item = RepositoryItemManager.getItem(ingredient);
                    if (item.isEmpty()) {
                        list.add(Text.literal("Can't find item %s".formatted(ingredient.toString())));
                        return;
                    }
                    entry.append(Text.literal(String.valueOf(ingredient.getAmount())).append("x ")
                        .formatted(Formatting.DARK_GRAY));
                    entry.append(item.get().getName());
                    list.add(entry);
                });
                int sum = subList.stream().mapToInt(CompostUpgradeCost.CompostUpgrade::copper).sum();
                list.add(Text.literal("  ")
                    .append(Text.literal(String.valueOf(sum)).append(" Copper").formatted(Formatting.RED)));
                break;
            }
        }

        texts.clear();
        texts.addAll(list);
    }

}
