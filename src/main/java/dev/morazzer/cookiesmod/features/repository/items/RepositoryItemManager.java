package dev.morazzer.cookiesmod.features.repository.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.HttpUtils;
import kotlin.Lazy;
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
import java.util.stream.Stream;

@Slf4j
public class RepositoryItemManager {
	private static final ConcurrentHashMap<Identifier, RepositoryItem> itemMap = new ConcurrentHashMap<>();
	private static final Path items = RepositoryManager.getRepoRoot().resolve("items");


	@SuppressWarnings("unused")
	public static boolean loadOfficialItemList() {
		JsonObject jsonObject = GsonUtils.gson.fromJson(new String(HttpUtils.getResponseBody(URI.create("https://api.hypixel.net/resources/skyblock/items"))), JsonObject.class);

		if (!jsonObject.get("success").getAsBoolean()) {
			return false;
		}

		JsonArray items = jsonObject.get("items").getAsJsonArray();


		for (JsonElement item : items) {
			JsonObject itemObject = item.getAsJsonObject();
			Path itemPath = RepositoryItemManager.items.resolve(itemObject.get("id").getAsString().replace(":", "_") + ".json");
			if (!ExceptionHandler.tryCatch(() -> Files.createDirectories(itemPath.getParent()))) continue;
			boolean result = ExceptionHandler.tryCatch(() -> Files.write(itemPath, GsonUtils.gson.toJson(itemObject).getBytes(), StandardOpenOption.CREATE_NEW));
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

	public static void loadItems() {
		try (Stream<Path> list = Files.list(items)) {
			list.forEach(RepositoryItemManager::loadItem);
		} catch (IOException e) {
			ExceptionHandler.handleException(e);
		}
		log.info("Loaded {} items", itemMap.size());
		updateItemList();
		long count = itemMap.values().stream().map(RepositoryItem::getItemStack).map(Lazy::getValue).count();
		log.info("{}", count);
	}

	@SuppressWarnings("UnusedReturnValue")
	public static boolean loadItem(Path path) {
		return loadItem(path, (o) -> {
		});
	}

	public static boolean loadItem(Path path, Consumer<Identifier> consumer) {
		try {
			log.debug("loading {}", path);
			JsonObject jsonObject = GsonUtils.gson.fromJson(Files.readString(path), JsonObject.class);
			RepositoryItem repositoryItem = new RepositoryItem(jsonObject);
			itemMap.put(repositoryItem.getSkyblockId(), repositoryItem);
			consumer.accept(repositoryItem.getSkyblockId());
			return true;
		} catch (Exception exception) {
			System.err.println(path);
			ExceptionHandler.handleException(exception);
			return false;
		}
	}


	public static RepositoryItem getItem(Identifier identifier) {
		return itemMap.get(identifier);
	}

	public static Optional<Identifier> findByName(String name) {
		return itemMap.values().stream()
				.filter(item -> item.getItemNameAlphaNumerical().equalsIgnoreCase(name))
				.map(RepositoryItem::getSkyblockId).findFirst();
	}

	private static void updateItemList() {
		itemList.clear();
		itemList.addAll(itemMap.keySet());
	}

	private static final CopyOnWriteArrayList<Identifier> itemList = new CopyOnWriteArrayList<>();

	public static List<Identifier> getAllItems() {
		return itemList;
	}



}
