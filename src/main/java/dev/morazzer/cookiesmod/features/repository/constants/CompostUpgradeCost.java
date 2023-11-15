package dev.morazzer.cookiesmod.features.repository.constants;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import dev.morazzer.cookiesmod.features.repository.files.RepositoryFileAccessor;
import dev.morazzer.cookiesmod.features.repository.items.recipe.Ingredient;
import dev.morazzer.cookiesmod.utils.general.ItemUtils;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import java.lang.reflect.Type;
import java.util.List;
import lombok.Getter;
import net.minecraft.util.JsonHelper;
import org.slf4j.LoggerFactory;

/**
 * Repository item for compost upgrades.
 */
public record CompostUpgradeCost(@SerializedName("speed") List<CompostUpgrade> speed,
                                 @SerializedName("multi_drop") List<CompostUpgrade> multiDrop,
                                 @SerializedName("fuel_cap") List<CompostUpgrade> fuelCap,
                                 @SerializedName("organic_matter_cap") List<CompostUpgrade> organicMatterCap,
                                 @SerializedName("cost_reduction") List<CompostUpgrade> costReduction) {

    @Getter
    private static CompostUpgradeCost instance;

    /**
     * Loads the compost upgrades.
     */
    public static void load() {
        JsonElement file = RepositoryFileAccessor.getInstance().getFile("constants/compost_upgrades");
        instance = JsonUtils.GSON.fromJson(file, CompostUpgradeCost.class);
    }

    /**
     * A compost upgrade.
     *
     * @param copper The amount of copper required.
     * @param cost   The items required.
     */
    public record CompostUpgrade(
        int copper,
        List<Ingredient> cost
    ) {

        /**
         * Deserializes a compost upgrade.
         *
         * @param jsonElement The json element.
         * @param typeOf      The type of the element.
         * @param context     The context.
         * @return The compost upgrade.
         */
        public static CompostUpgrade deserialize(JsonElement jsonElement, Type typeOf,
                                                 JsonDeserializationContext context) {
            if (jsonElement.isJsonObject()) {
                JsonObject cost = JsonHelper.getObject(jsonElement.getAsJsonObject(), "cost");
                return new CompostUpgrade(
                    JsonHelper.getInt(jsonElement.getAsJsonObject(), "copper"),
                    cost.keySet().stream().map(
                        s -> new Ingredient(
                            "%s:%s".formatted(ItemUtils.withNamespace(s), JsonHelper.getInt(cost, s)))).toList()
                );
            }
            LoggerFactory.getLogger("cookiesmod-repository").error("Invalid compost upgrade: {}", jsonElement);
            return null;
        }
    }

}
