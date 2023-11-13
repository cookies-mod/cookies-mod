package dev.morazzer.cookiesmod.features.farming.garden;

import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.config.categories.farming.VisitorFoldable;
import dev.morazzer.cookiesmod.features.price.bazaar.Bazaar;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItemManager;
import dev.morazzer.cookiesmod.features.repository.items.item.SkyblockItem;
import dev.morazzer.cookiesmod.features.repository.items.recipe.Ingredient;
import dev.morazzer.cookiesmod.features.repository.items.recipe.RepositoryRecipeManager;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.NumberFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

/**
 * Helper to show items required for garden visitors.
 */
@LoadModule("garden/visitor_helper")
public class VisitorHelper implements Module {

    @Getter
    private static VisitorHelper visitorHelper;

    private final DecimalFormat numberFormatter = new DecimalFormat("###,##0");

    @Override
    public void load() {
        visitorHelper = new VisitorHelper();
        ItemTooltipCallback.EVENT.register(visitorHelper::modifyAcceptItem);
    }

    @Override
    public String getIdentifierPath() {
        return "garden/visitor_helper";
    }

    /**
     * Modifies the accept item stack to contain the total amounts of the lower versions.
     *
     * @param itemStack      The item to modify.
     * @param tooltipContext The tooltip context.
     * @param texts          The current lore.
     */
    private void modifyAcceptItem(ItemStack itemStack, TooltipContext tooltipContext, List<Text> texts) {
        if (!Garden.isOnGarden()) {
            return;
        }
        if (!Plot.getCurrentPlot().isBarn()) {
            return;
        }
        if (!ConfigManager.getConfig().gardenCategory.visitors.shouldAddItems.getValue()) {
            return;
        }
        if (!itemStack.hasCustomName()) {
            return;
        }
        Text name = itemStack.getName();

        if (!name.getString().equals("Accept Offer")) {
            return;
        }

        boolean finishedItems = false;

        ArrayList<Text> newTooltip = new ArrayList<>();

        for (Text text : texts) {
            newTooltip.add(text);
            String literalContent = text.getString().trim();
            if (literalContent.equals("Items Required: ")) {
                continue;
            }
            if (literalContent.equals(texts.get(0).getString().trim())) {
                continue;
            }
            if (literalContent.equals("Rewards:")) {
                finishedItems = true;
            }

            if (!finishedItems && !literalContent.isEmpty() && literalContent.matches("([A-Za-z ]+)(?: x[\\d,]+)?")) {
                newTooltip.remove(text);
                Optional<Identifier> item = RepositoryItemManager.findByName(literalContent.replaceAll(
                    "([A-Za-z ]+[a-z]) ?(x[\\d,]+)",
                    "$1"
                ));
                if (item.isEmpty()) {
                    newTooltip.add(Text.literal(" -> ")
                        .append(Text.literal("Could not find item").formatted(Formatting.RED))
                        .formatted(Formatting.GRAY));
                    continue;
                }

                String amountString = literalContent.replaceAll("\\D", "");
                int amount = Integer.parseInt(amountString.isEmpty() ? "1" : amountString);

                MutableText originalItem = Text.empty().formatted(Formatting.DARK_GRAY).append(text);
                if (ConfigManager.getConfig().gardenCategory.visitors.showPrice.getValue()) {
                    createPrice(item.get(), amount).ifPresent(mutableText -> originalItem.append(" (")
                        .append(mutableText).append(")"));
                }

                newTooltip.add(originalItem);
                newTooltip.addAll(createMaterialList(new Ingredient(item.get(), amount), 0, new ArrayList<>()));
            }
        }

        texts.clear();
        texts.addAll(newTooltip);
    }

    /**
     * Create a list of all materials that are required to craft the root ingredient.
     *
     * @param rootIngredient The ingredient to get the items for.
     * @param deep           The depth of the recursive call.
     * @param visited        The ingredients that have been visited to prevent cyclic calls.
     * @return A list of items that are required.
     */
    private List<Text> createMaterialList(Ingredient rootIngredient, int deep, ArrayList<Ingredient> visited) {
        ArrayList<Text> texts = new ArrayList<>();
        if (rootIngredient.getPath().equals("items/wheat") || rootIngredient.getPath().equals("items/golden_carrot")) {
            return texts;
        }
        List<Ingredient> ingredientListSorted = RepositoryRecipeManager.getIngredientListSorted(rootIngredient);

        for (Ingredient ingredient : ingredientListSorted) {
            Ingredient finalIngredient = ingredient.withAmount(ingredient.getAmount() * rootIngredient.getAmount());
            Optional<SkyblockItem> item = RepositoryItemManager.getItem(finalIngredient);


            if (visited.contains(finalIngredient)) {
                continue;
            }
            visited.add(finalIngredient);

            if (item.isEmpty()) {
                texts.add(Text.literal(StringUtils.leftPad("", deep)).append(" -> ")
                    .append(Text.literal("Can not find item with internal id " + finalIngredient)
                        .formatted(Formatting.RED)).formatted(Formatting.GRAY));
                continue;
            }

            MutableText text = Text.literal(StringUtils.leftPad("", deep)).append(" -> ")
                .formatted(Formatting.DARK_GRAY);

            Text amount = Text.literal(numberFormatter.format(finalIngredient.getAmount()));
            Text name;
            if (ConfigManager.getConfig().gardenCategory.visitors.useItemRarityColor.getValue()) {
                name = item.get().getName();
            } else {
                name = Text.literal(item.get().getName().getString()).formatted(Formatting.GRAY);
            }

            if (ConfigManager.getConfig().gardenCategory.visitors.countPosition.getValue()
                == VisitorFoldable.CountPosition.LEFT) {
                text.append(amount).append("x ").append(name);
            } else {
                text.append(name).append(" x").append(amount);
            }

            if (ConfigManager.getConfig().gardenCategory.visitors.showPrice.getValue()) {
                createPrice(
                    finalIngredient,
                    finalIngredient.getAmount()
                ).ifPresent(mutableText -> text.append(Text.literal(" (").append(mutableText).append(")")
                    .formatted(Formatting.DARK_GRAY)));
            }


            texts.add(text);

            if (deep == 10) {
                continue;
            }
            texts.addAll(createMaterialList(finalIngredient, deep + 1, new ArrayList<>(visited)));
        }

        return texts;
    }

    /**
     * Get the price of an ingredient if possible.
     *
     * @param ingredient The item to get the price for.
     * @param amount     The amount that is needed.
     * @return The price as text.
     */
    private Optional<MutableText> createPrice(Identifier ingredient, int amount) {
        return Bazaar.getInstance().getProductInformation(ingredient)
            .map(
                productInformation -> (double) switch (ConfigManager.getConfig()
                    .gardenCategory.visitors.buyType.getValue()) {
                    case ORDER -> {
                        if (productInformation.sellSummary().length > 0) {
                            yield productInformation.sellSummary()[0].pricePerUnit();
                        }
                        yield 0d;
                    }
                    case INSTANT -> productInformation.quickStatus().buyPrice();
                }).map(itemPrice -> itemPrice * amount).map(NumberFormat::toString).map("%s Coins"::formatted)
            .map(Text::literal).map(priceText -> priceText.formatted(Formatting.GOLD));
    }

}
