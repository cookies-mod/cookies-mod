package dev.morazzer.cookiesmod.features.repository.files;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Accessor for dispersed files used for local development of the repository.
 */
public class DisperseFileAccessor extends RepositoryFileAccessor {

    @Override
    public List<JsonElement> getDirectory(Path path) {
        try (Stream<Path> list = Files.list(path)) {
            return list.toList().stream().map(file -> {
                try {
                    return JsonUtils.CLEAN_GSON.fromJson(Files.readString(file), JsonElement.class);
                } catch (Exception exception) {
                    ExceptionHandler.handleException(new RuntimeException("Error while loading file %s".formatted(file
                        .getFileName()
                        .toString()), exception));
                }
                return null;
            }).filter(Objects::nonNull).toList();
        } catch (Exception e) {
            ExceptionHandler.handleException(new RuntimeException("Failed loading files", e));
        }
        return Collections.emptyList();
    }

    @Override
    public JsonElement getFile(Path path) {
        try {
            return JsonUtils.CLEAN_GSON.fromJson(Files.readString(
                path.resolveSibling(path.getFileName().toString() + ".json"),
                StandardCharsets.UTF_8
            ), new TypeToken<>() {
            });
        } catch (Exception e) {
            ExceptionHandler.handleException(new RuntimeException("Failed loading files", e));
        }

        return null;
    }

}
