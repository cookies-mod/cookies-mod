package dev.morazzer.cookiesmod.features.repository.files;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

/**
 * Accessor for bundled files from the repository releases.
 */
public class BundledFileAccessor extends RepositoryFileAccessor {

    private final Map<String, JsonObject> map = new ConcurrentHashMap<>();

    @Override
    public List<JsonElement> getDirectory(Path path) {
        Path jsonFile = path.resolveSibling(path.getFileName().toString() + ".json");
        try {
            return StreamSupport.stream(JsonUtils.CLEAN_GSON.fromJson(
                Files.readString(jsonFile, StandardCharsets.UTF_8),
                JsonArray.class
            ).spliterator(), false).toList();
        } catch (IOException e) {
            ExceptionHandler.handleException(new RuntimeException("Failed loading files", e));
        }

        return null;
    }

    @Override
    public JsonElement getFile(Path path) {
        String file = path.getFileName().toString();
        String key = path.getParent().getFileName().toString();
        String jsonFile = key + ".json";
        if (map.containsKey(key)) {
            return map.get(key).get(file);
        }

        try {
            JsonObject jsonObject = JsonUtils.CLEAN_GSON.fromJson(Files.readString(path
                .getParent()
                .resolveSibling(jsonFile), StandardCharsets.UTF_8), JsonObject.class);
            this.map.put(key, jsonObject);
            return jsonObject.get(file);
        } catch (IOException e) {
            ExceptionHandler.handleException(new RuntimeException("Failed loading files", e));
        }
        return null;
    }

}
