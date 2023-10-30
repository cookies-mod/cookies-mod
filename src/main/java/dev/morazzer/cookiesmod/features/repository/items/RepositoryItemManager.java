package dev.morazzer.cookiesmod.features.repository.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.repository.items.item.SkyblockItem;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Manager to handle all repository items.
 */
@Slf4j
public class RepositoryItemManager {

    private static final ConcurrentHashMap<Identifier, SkyblockItem> itemMap = new ConcurrentHashMap<>();
    private static final Path items = RepositoryManager.getRepoRoot().resolve("items");
    private static final CopyOnWriteArrayList<Identifier> itemList = new CopyOnWriteArrayList<>();

    /**
     * Loads all items from the official item list.
     *
     * @return If the items where loaded successfully.
     */
    public static boolean loadOfficialItemList() {
        JsonObject jsonObject = GsonUtils.gson.fromJson(new String(HttpUtils.getResponseBody(URI.create(
                "https://api.hypixel.net/resources/skyblock/items"))), JsonObject.class);

        if (!jsonObject.get("success").getAsBoolean()) {
            return false;
        }

        JsonArray items = jsonObject.get("items").getAsJsonArray();


        for (JsonElement item : items) {
            JsonObject itemObject = item.getAsJsonObject();
            Path itemPath = RepositoryItemManager.items.resolve(itemObject
                    .get("id")
                    .getAsString()
                    .replace(":", "_") + ".json");
            if (!ExceptionHandler.tryCatch(() -> Files.createDirectories(itemPath.getParent()))) continue;
            boolean result = ExceptionHandler.tryCatch(() -> Files.write(
                    itemPath,
                    GsonUtils.gson.toJson(itemObject).getBytes(),
                    StandardOpenOption.CREATE_NEW
            ));
            if (!result) {
                log.warn("Failed saving {}", itemPath);
                continue;
            }
            log.info("Successfully saved {}", itemPath);
        }

        return true;
    }

    public static void reloadItems() {
        itemMap.clear();
        loadItems();
        updateItemList();
    }

    /**
     * Loads all items from the local repository copy.
     */
    public static void loadItems() {
        try (Stream<Path> list = Files.list(items)) {
            list.filter(Predicate.not(RepositoryItemManager::loadItem)).forEach(value -> log.warn("Failed to load item {}", value));
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
        log.info("Loaded {} items", itemMap.size());
        updateItemList();
    }

    /**
     * Loads one item from the local repository copy.
     *
     * @param path The path to load the item from.
     * @return If the item was loaded successfully.
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean loadItem(Path path) {
        return loadItem(path, (o) -> {});
    }

    /**
     * Load one item from the local repository copy.
     *
     * @param path     The path to load the item from.
     * @param consumer The callback to run after the item was loaded.
     * @return If the item was loaded successfully.
     */
    public static boolean loadItem(Path path, Consumer<Identifier> consumer) {
        try {
            log.debug("loading {}", path);
            JsonObject jsonObject = GsonUtils.gson.fromJson(Files.readString(path), JsonObject.class);
            SkyblockItem repositoryItem = new SkyblockItem(jsonObject);
            itemMap.put(repositoryItem.getSkyblockId(), repositoryItem);
            consumer.accept(repositoryItem.getSkyblockId());
            return true;
        } catch (Exception exception) {
            System.err.println(path);
            ExceptionHandler.handleException(exception);
            return false;
        }
    }

    /**
     * Gets one item by its internal id.
     *
     * @param identifier The item id.
     * @return The item.
     */
    public static SkyblockItem getItem(Identifier identifier) {
        return itemMap.get(identifier);
    }

    /**
     * Finds one item by its name.
     *
     * @param name The name of the item.
     * @return The item.
     */
    public static Optional<Identifier> findByName(String name) {
        return itemMap.values().stream()
                .filter(item -> item.getItemNameAlphanumerical().equalsIgnoreCase(name))
                .map(SkyblockItem::getSkyblockId).findFirst();
    }

    /**
     * Updates the item list.
     */
    private static void updateItemList() {
        itemList.clear();
        itemList.addAll(itemMap.keySet());
    }

    /**
     * Gets all items in the item list.
     *
     * @return The item list.
     */
    public static List<Identifier> getAllItems() {
        return itemList;
    }

}
