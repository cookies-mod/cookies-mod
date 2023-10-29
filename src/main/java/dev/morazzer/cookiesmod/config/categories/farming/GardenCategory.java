package dev.morazzer.cookiesmod.config.categories.farming;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.options.BooleanOption;
import dev.morazzer.cookiesmod.features.farming.garden.YawPitchDisplay;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Category that contains all settings related to the garden/farming.
 */
public class GardenCategory extends Category {

    @Expose
    public BooleanOption yawPitchDisplay = new BooleanOption(
            Text.literal("Yaw and Pitch"),
            Text.literal("Shows the Yaw and Pitch for when you farm (only if cultivating or replenish is on the tool)"),
            false
    ).withHudElement(new YawPitchDisplay());
    @Expose
    public final BooleanOption plotBreakdown = new BooleanOption(
            Text.literal("Plot price breakdown"),
            Text.literal("Shows a breakdown of the overall price for plots"),
            false
    );
    @Expose
    public final VisitorFoldable visitors = new VisitorFoldable();
    @Expose
    public final SpeedFoldable speed = new SpeedFoldable();
    @Expose
    public final JacobFoldable jacobFoldable = new JacobFoldable();
    public final CompostFoldable compostFoldable = new CompostFoldable();

    @Override
    public Text getName() {
        return Text.literal("Garden").formatted(Formatting.DARK_GREEN);
    }

    @Override
    public Text getDescription() {
        return Text.literal("All settings related with the garden features");
    }

}
