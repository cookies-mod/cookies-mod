package dev.morazzer.cookiesmod.features.repository.items.tags;

import com.google.gson.Gson;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Manager to load all tags from the repository.
 */
public class TagManager {

    public static final ConcurrentHashMap<Identifier, Tag> concurrentHashMap = new ConcurrentHashMap<>();

    /**
     * Loads all tags from the repository into objects.
     */
    public static void loadTags() {
        concurrentHashMap.clear();
        Path tags = RepositoryManager.getRepoRoot().resolve("tags");
        try (Stream<Path> list = Files.list(tags)) {
            list.forEach(TagManager::load);
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }

    /**
     * Loads all tags from a specific path.
     *
     * @param path The path.
     */
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
