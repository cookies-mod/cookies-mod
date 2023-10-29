package dev.morazzer.cookiesmod.features.price.bazaar;

import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ConcurrentUtils;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.HttpUtils;
import dev.morazzer.cookiesmod.utils.general.ItemUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.util.Identifier;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Helper to fetch the current prices of items from the bazaar.
 */
@LoadModule("prices/bazaar")
@Slf4j
public class Bazaar implements Module {

    private static final String bazaarApiEndpoint = "https://api.hypixel.net/skyblock/bazaar";
    @Getter
    private static Bazaar instance;
    private final ConcurrentHashMap<Identifier, ProductInformation> productMap = new ConcurrentHashMap<>();
    @Getter
    private long lastUpdated = -1;

    /**
     * Get the product information for a specific item if present.
     *
     * @param identifier The item.
     * @return The information.
     */
    public Optional<ProductInformation> getProductInformation(Identifier identifier) {
        return Optional.ofNullable(this.productMap.getOrDefault(identifier, null));
    }

    @Override
    public void load() {
        instance = this;
        ConcurrentUtils.scheduleAtFixedRate(this::updateProducts, 15, TimeUnit.MINUTES);
    }

    @Override
    public String getIdentifierPath() {
        return "prices/bazaar";
    }

    /**
     * Update the fetched data.
     */
    private void updateProducts() {
        byte[] responseBody = HttpUtils.getResponseBody(URI.create(bazaarApiEndpoint));
        String body = new String(responseBody, StandardCharsets.UTF_8);
        JsonObject jsonObject = GsonUtils.gsonClean.fromJson(body, JsonObject.class);
        if (!jsonObject.has("success") || !jsonObject.get("success").getAsBoolean()) {
            return;
        }
        this.productMap.clear();

        this.lastUpdated = jsonObject.get("lastUpdated").getAsLong();
        JsonObject products = jsonObject.getAsJsonObject("products");
        for (String key : products.keySet()) {
            if (!products.get(key).isJsonObject()) {
                continue;
            }
            JsonObject product = products.getAsJsonObject(key);
            Identifier identifier = ItemUtils.skyblockIdToIdentifier(product.get("product_id").getAsString())
                    .orElse(null);
            if (identifier == null) {
                continue;
            }
            ProductSummary[] buySummary = GsonUtils.gsonClean.fromJson(
                    product.getAsJsonArray("buy_summary"),
                    ProductSummary[].class
            );
            ProductSummary[] sellSummary = GsonUtils.gsonClean.fromJson(
                    product.getAsJsonArray("sell_summary"),
                    ProductSummary[].class
            );
            QuickStatus quickStatus = GsonUtils.gsonClean.fromJson(
                    product.getAsJsonObject("quick_status"),
                    QuickStatus.class
            );

            this.productMap.put(identifier, new ProductInformation(identifier, sellSummary, buySummary, quickStatus));
        }

        log.info("Added {} products to bazaar cache", this.productMap.size());
    }

}
