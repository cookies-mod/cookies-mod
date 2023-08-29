package dev.morazzer.cookiesmod.features.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItem;
import dev.morazzer.cookiesmod.features.repository.items.tags.TagManager;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.HttpUtils;
import kotlin.Lazy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Getter
@Slf4j
public class RepositoryManager {
	private static final ConcurrentHashMap<Identifier, RepositoryItem> itemMap = new ConcurrentHashMap<>();

	private static final CopyOnWriteArrayList<Runnable> reloadCallbacks = new CopyOnWriteArrayList<>();

	@Getter
	private static final Path repoRoot = Path.of("cookiesmod/repo");

	public static Path mergeAllItems() {
		JsonObject jsonObject = GsonUtils.gson.fromJson(new String(HttpUtils.getResponseBody(URI.create("https://api.hypixel.net/resources/skyblock/items"))), JsonObject.class);

		JsonArray items = jsonObject.getAsJsonArray("items");

		Path path = repoRoot.resolve("merged.json");

		JsonObject finalItem = new JsonObject();

		int i = 0;

		for (JsonElement item : items) {
			System.out.printf("%s/%s: ", i++, items.size());
			createDeepCopy(finalItem, item.getAsJsonObject(), "root." + item.getAsJsonObject().get("id").getAsString());
		}

		ExceptionHandler.removeThrows(() -> Files.write(path, GsonUtils.gson.toJson(finalItem).getBytes(), StandardOpenOption.CREATE_NEW));

		return path;
	}

	public static void addReloadCallback(Runnable runnable) {
		reloadCallbacks.add(runnable);
	}

	public static boolean loadOfficialItemList() {
		JsonObject jsonObject = GsonUtils.gson.fromJson(new String(HttpUtils.getResponseBody(URI.create("https://api.hypixel.net/resources/skyblock/items"))), JsonObject.class);

		if (!jsonObject.get("success").getAsBoolean()) {
			return false;
		}

		JsonArray items = jsonObject.get("items").getAsJsonArray();


		for (JsonElement item : items) {
			JsonObject itemObject = item.getAsJsonObject();
			Path itemPath = repoRoot.resolve("items/" + itemObject.get("id").getAsString().replace(":", "_") + ".json");
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
		reloadCallbacks.forEach(Runnable::run);
	}

	public static void loadItems() {
		try (Stream<Path> list = Files.list(repoRoot.resolve("items"))) {
			list.forEach(RepositoryManager::loadItem);
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
		return loadItem(path, (o) -> {});
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

	private static void updateItemList() {
		itemList.clear();
		itemList.addAll(itemMap.keySet());
	}

	private static final CopyOnWriteArrayList<Identifier> itemList = new CopyOnWriteArrayList<>();

	public static List<Identifier> getAllItems() {
		return itemList;
	}

	private static void createDeepCopy(JsonElement destination, JsonElement source, String name) {
		System.out.printf("%s\r", name);
		if (source.isJsonObject()) {
			JsonObject jsonObject = source.getAsJsonObject();
			JsonObject object = destination.getAsJsonObject();
			for (String s : jsonObject.keySet()) {
				JsonElement jsonElement = jsonObject.get(s);
				if (jsonElement.isJsonObject()) {
					JsonObject childObject = jsonElement.getAsJsonObject();
					JsonObject newObject = object.has(s) ? object.get(s).getAsJsonObject() : new JsonObject();
					createDeepCopy(newObject, childObject, name + "." + s);
					object.add(s, newObject);
				} else if (jsonElement.isJsonArray()) {
					JsonArray childObject = jsonObject.getAsJsonArray();
					JsonArray newArray = object.has(s) ? object.getAsJsonArray(s) : new JsonArray();
					createDeepCopy(newArray, childObject, name + "." + s);
					object.add(s, newArray);
				} else {
					if (!object.has(s)) {
						object.add(s, jsonElement);
					} else if (object.get(s).getAsJsonPrimitive().isString()) {
						JsonElement element = object.getAsJsonPrimitive(s);
						if (element.getAsString().contains(jsonElement.getAsString())) continue;
						object.add(s, new JsonPrimitive(element.getAsString() + " | " + jsonElement.getAsString()));
					}
				}
			}
		} else if (source instanceof JsonArray sourceArray && destination instanceof JsonArray destinationArray) {
			int i = 0;

			for (JsonElement jsonElement : sourceArray) {
				if (jsonElement.isJsonObject()) {
					JsonObject childObject = jsonElement.getAsJsonObject();
					JsonObject newObject = destinationArray.asList()
							.stream()
							.filter(JsonElement::isJsonObject)
							.map(JsonElement::getAsJsonObject)
							.findFirst()
							.map(d -> {
								destinationArray.remove(d);
								return d;
							}).orElse(new JsonObject());


					createDeepCopy(newObject, childObject, name + ".[" + i++ + "]");
					destinationArray.add(newObject);
				} else if (jsonElement.isJsonArray()) {
					JsonArray childObject = jsonElement.getAsJsonArray();
					JsonArray newArray = destinationArray.asList()
							.stream()
							.filter(JsonElement::isJsonArray)
							.map(JsonElement::getAsJsonArray)
							.findFirst()
							.map(d -> {
								destinationArray.remove(d);
								return d;
							}).orElse(new JsonArray());

					createDeepCopy(newArray, childObject, name + ".[" + i++ + "]");
					destinationArray.add(newArray);
				} else {
					JsonPrimitive childObject = jsonElement.getAsJsonPrimitive();
					if (!childObject.isString()) {
						destinationArray.add(childObject);
						continue;
					}

					JsonPrimitive jsonPrimitive = destinationArray.asList()
							.stream()
							.filter(JsonElement::isJsonPrimitive)
							.map(JsonElement::getAsJsonPrimitive)
							.findFirst()
							.map(d -> {
								destinationArray.remove(d);
								return d;
							}).orElse(new JsonPrimitive(""));

					if (jsonPrimitive.getAsString().contains(childObject.getAsString())) continue;
					destinationArray.add(new JsonPrimitive(jsonPrimitive.getAsString() + " | " + childObject.getAsString()));
				}
			}
		}
	}

	public static void load() {
		loadItems();
		TagManager.loadTags();
	}
}
