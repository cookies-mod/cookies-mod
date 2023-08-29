package dev.morazzer.cookiesmod.features.repository.items.tags;

import com.google.gson.Gson;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class TagManager {

	public static ConcurrentHashMap<Identifier, Tag> concurrentHashMap = new ConcurrentHashMap<>();

	public static void loadTags() {
		concurrentHashMap.clear();
		Path tags = RepositoryManager.getRepoRoot().resolve("tags");
		try (Stream<Path> list = Files.list(tags)) {
			list.forEach(TagManager::load);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Identifier> grouped() {
		return concurrentHashMap.values().stream().map(Tag::identifiers).flatMap(List::stream).toList();
	}

	private static void load(Path path) {
		Gson gson = new Gson();
		try {
			Tag tag = gson.fromJson(Files.readString(path), Tag.class);
			concurrentHashMap.put(tag.key(), tag);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
