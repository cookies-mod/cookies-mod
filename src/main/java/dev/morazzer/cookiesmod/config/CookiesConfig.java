package dev.morazzer.cookiesmod.config;

import com.google.gson.annotations.Expose;
import dev.morazzer.cookiesmod.config.categories.DevCategory;
import dev.morazzer.cookiesmod.config.categories.GardenCategory;
import dev.morazzer.cookiesmod.config.categories.ItemListConfig;
import dev.morazzer.cookiesmod.config.categories.MainCategory;
import dev.morazzer.cookiesmod.config.categories.ProfileViewerConfig;
import dev.morazzer.cookiesmod.config.system.Config;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import net.minecraft.text.Text;

public class CookiesConfig extends Config<CookiesConfig> {
	@Expose
	public MainCategory mainCategory = new MainCategory();

	@Expose
	public ProfileViewerConfig profileViewerConfig = new ProfileViewerConfig();

	@Expose
	public ItemListConfig itemListConfig = new ItemListConfig();

	@Expose
	public GardenCategory gardenCategory = new GardenCategory();

	@Expose
	public DevCategory devCategory = new DevCategory();

	@Override
	public Text getTitle() {
		return Text.empty()
				.append(Text.literal("Cookies Mod "))
				.append("version ")
				.append(Text.literal("{version}"))
				.append(" by ")
				.append(Text.literal("Morazzer").styled(style -> style.withColor(ColorUtils.mainColor)))
				.append(", config design by ")
				.append(Text.literal("Moulberry").styled(style -> style.withColor(ColorUtils.successColor)))
				.append(" and ")
				.append(Text.literal("nea89").styled(style -> style.withColor(ColorUtils.successColor)))
				.styled(style -> style.withColor(0xababab));
	}
}
