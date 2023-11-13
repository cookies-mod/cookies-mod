package dev.morazzer.cookiesmod.features.repository.items;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.repository.files.RepositoryFileAccessor;
import dev.morazzer.cookiesmod.features.repository.items.item.SkyblockItem;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.util.Identifier;

/**
 * Manager to handle all repository items.
 */
@Slf4j
public class RepositoryItemManager {

    private static final ConcurrentHashMap<Identifier, SkyblockItem> itemMap = new ConcurrentHashMap<>();
    private static final Path items = RepositoryManager.getRepoRoot().resolve("items");
    private static final CopyOnWriteArrayList<Identifier> itemList = new CopyOnWriteArrayList<>();


    /**
     * Reloads all items from the local repository.
     */
    public static void reloadItems() {
        itemMap.clear();
        loadItems();
        updateItemList();
    }

    /**
     * Loads all items from the local repository copy.
     */
    public static void loadItems() {
        RepositoryFileAccessor.getInstance()
            .getDirectory(items)
            .stream()
            .filter(JsonElement::isJsonObject)
            .map(JsonElement::getAsJsonObject)
            .filter(Predicate.not(RepositoryItemManager::loadItem))
            .forEach(value -> log.warn("Failed to load item {}", value.get("")));

        log.info("Loaded {} items", itemMap.size());
        updateItemList();
    }

    /**
     * Loads one item from the local repository copy.
     *
     * @param jsonObject The object to load as item.
     * @return If the item was loaded successfully.
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean loadItem(JsonObject jsonObject) {
        return loadItem(jsonObject, (o) -> {
        });
    }

    /**
     * Loads one item from the local repository copy.
     *
     * @param jsonObject The object to load as item.
     * @param consumer   The callback to run after the item was loaded.
     * @return If the item was loaded successfully.
     */
    public static boolean loadItem(JsonObject jsonObject, Consumer<Identifier> consumer) {
        try {
            SkyblockItem repositoryItem = new SkyblockItem(jsonObject);
            SkyblockItem put = itemMap.put(repositoryItem.getSkyblockId(), repositoryItem);
            if (put != null) {
                log.error("Item was overridden {}", repositoryItem.getSkyblockId());
            }
            consumer.accept(repositoryItem.getSkyblockId());
            return true;
        } catch (Exception exception) {
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
    public static Optional<SkyblockItem> getItem(Identifier identifier) {
        return Optional.ofNullable(itemMap.get(identifier));
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
