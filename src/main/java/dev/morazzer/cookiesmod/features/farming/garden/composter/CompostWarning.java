package dev.morazzer.cookiesmod.features.farming.garden.composter;

import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.config.categories.farming.CompostFoldable;
import dev.morazzer.cookiesmod.events.api.PlayerListUpdateEvent;
import dev.morazzer.cookiesmod.features.farming.garden.Garden;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.NumberFormat;
import dev.morazzer.cookiesmod.utils.TabUtils;
import dev.morazzer.cookiesmod.utils.general.CookiesUtils;
import lombok.Getter;
import net.minecraft.client.network.PlayerListEntry;

@LoadModule("farming/garden/compost_warning")
public class CompostWarning implements Module {

    @Getter
    private static CompostWarning instance;
    boolean lowMatter = false;
    boolean lowFuel = false;

    int organicMatterAmount = 0;
    int fuelAmount = 0;

    @Override
    public void load() {
        instance = this;
        CompostFoldable compostFoldable = ConfigManager.getConfig().gardenCategory.compostFoldable;
        this.organicMatterAmount = compostFoldable.organicMatterAmount.getNumberTransformer()
                .parseNumber(compostFoldable.organicMatterAmount.getValue());
        this.fuelAmount = compostFoldable.fuelWarningAmount.getNumberTransformer()
                .parseNumber(compostFoldable.fuelWarningAmount.getValue());
        compostFoldable.organicMatterAmount.withCallback((oldValue, newValue) -> this.organicMatterAmount = newValue);
        compostFoldable.fuelWarningAmount.withCallback((oldValue, newValue) -> this.fuelAmount = newValue);
        PlayerListUpdateEvent.UPDATE_NAME.register(this::updateNames);
    }

    private void updateNames(PlayerListEntry entry) {
        if (!Garden.isOnGarden()) return;
        if (!TabUtils.isInRange(2, 7, 11, entry)) return;
        if (!ConfigManager.getConfig().gardenCategory.compostFoldable.showWarnings.getValue()) return;
        if (entry.getDisplayName() == null) return;

        String name = entry.getDisplayName().getString().trim();

        switch (TabUtils.getRow(entry)) {
            case 8 -> {
                if (!name.matches("Organic Matter: \\d+(?:.\\d+)?.?")) return;
                long l = NumberFormat.fromString(name.substring(16));
                if (l <= this.organicMatterAmount) {
                    if (this.lowMatter) {
                        return;
                    }
                    CookiesUtils.sendMessage(CookiesMod.createPrefix(ColorUtils.failColor)
                            .append("Low Organic Matter!"));
                    this.lowMatter = true;
                    return;
                }
                this.lowMatter = false;
            }
            case 9 -> {
                if (!name.matches("Fuel: \\d+(?:.\\d+)?.?")) return;
                long l = NumberFormat.fromString(name.substring(6));
                if (l <= this.fuelAmount) {
                    if (this.lowFuel) {
                        return;
                    }
                    CookiesUtils.sendMessage(CookiesMod.createPrefix(ColorUtils.failColor)
                            .append("Low on fuel!"));
                    this.lowFuel = true;
                    return;
                }
                this.lowFuel = false;
            }
        }
    }

    @Override
    public String getIdentifierPath() {
        return "farming/garden/compost_warning";
    }
}
