package dev.morazzer.cookiesmod.config.categories.farming;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import dev.morazzer.cookiesmod.config.system.options.EnumDropdownOption;
import lombok.Getter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * Foldable that contains all settings related to visitors.
 */
public class VisitorFoldable extends Foldable {

    @Expose
    public final BooleanOption shouldAddItems = new BooleanOption(
            Text.literal("Show required items"),
            Text.literal("Whether the items should be added to the description"),
            true
    );

    @Expose
    public final BooleanOption useItemRarityColor = new BooleanOption(
            Text.literal("Use Item Rarity As Color"),
            Text.literal("Uses the item rarity for the color of the required items"),
            true
    ).withTags("tier", "visitors");

    @Expose
    public final EnumDropdownOption<CountPosition> countPosition = new EnumDropdownOption<>(
            Text.literal("Show required amount"),
            Text.literal("Where to show the required amount for the items"),
            CountPosition.RIGHT
    );

    @Expose
    public final BooleanOption showPrice = new BooleanOption(
            Text.literal("Show crop price"),
            Text.literal("Shows the price of the required items"),
            true
    ).withTags("coins", "bazaar", "cost");

    @Expose
    public final EnumDropdownOption<BazaarBuyType> buyType = new EnumDropdownOption<>(
            Text.literal("Bazaar buy type"),
            Text.literal("Change the behaviour to either show instant buy or buy order price"),
            BazaarBuyType.INSTANT
    ).withSupplier(BazaarBuyType::getText).withTags("coins", "cost");

    @Override
    public Text getName() {
        return Text.literal("Visitor Helper");
    }

    @Getter
    public enum BazaarBuyType {
        INSTANT(Text.literal("Instant Buy")),
        ORDER(Text.literal("Buy Order"));

        private final Text text;

        BazaarBuyType(MutableText text) {
            this.text = text;
        }
    }

    public enum CountPosition {
        LEFT,
        RIGHT
    }

}
