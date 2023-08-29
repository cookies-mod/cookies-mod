package dev.morazzer.cookiesmod.config;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.categories.DevCategory;
import dev.morazzer.cookiesmod.config.categories.ItemListConfig;
import dev.morazzer.cookiesmod.config.categories.MainCategory;
import dev.morazzer.cookiesmod.config.categories.ProfileViewerConfig;
import io.github.moulberry.moulconfig.Config;
import io.github.moulberry.moulconfig.Social;
import io.github.moulberry.moulconfig.annotations.Category;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

import static dev.morazzer.cookiesmod.utils.ColorUtils.mainColor;

public class CookiesConfig extends Config {

    @Override
    public void executeRunnable(int runnableId) {
        super.executeRunnable(runnableId);
    }

    @Override
    public List<Social> getSocials() {
        return List.of();
    }

    @Override
    public boolean shouldAutoFocusSearchbar() {
        return true;
    }

    @Override
    public Text getTitle() {
        return Text.literal("Cookies mod %version% by ")
                .append(Text.literal("Morazzer").setStyle(Style.EMPTY.withColor(mainColor)))
                .append(", config by ")
                .append(Text.literal("Moulberry").formatted(Formatting.RED))
                .append(" and ")
                .append(Text.literal("nea89").formatted(Formatting.RED))
                .append(" ported by ")
                .append(Text.literal("Morazzer").setStyle(Style.EMPTY.withColor(mainColor)))
                .formatted(Formatting.GRAY);
    }

    @Expose
    @Category(name = "Updates", description = "a")
    public MainCategory mainCategory = new MainCategory();

    @Expose
    @Category(name = "Profile Viewer", description = "Settings related to the /pv command")
    public ProfileViewerConfig profileViewerConfig = new ProfileViewerConfig();


    @Expose
    @Category(name = "Item List", description = "Settings related to the item list")
    public ItemListConfig itemListConfig = new ItemListConfig();

    @Expose
    @Category(name = "Dev", description = "Development related settings, most of them are for debug or ONLY for development")
    public DevCategory devCategory = new DevCategory();

}
