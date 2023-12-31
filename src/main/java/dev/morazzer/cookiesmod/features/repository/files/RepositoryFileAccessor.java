package dev.morazzer.cookiesmod.features.repository.files;

import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Accessor for the repository files.
 */
public abstract class RepositoryFileAccessor {

    protected static final Logger LOGGER = LoggerFactory.getLogger("repository-files");

    private static final RepositoryFileAccessor INSTANCE = createInstance();

    /**
     * Gets the global instance of the file accessor.
     *
     * @return The global instance.
     */
    public static RepositoryFileAccessor getInstance() {
        return INSTANCE;
    }

    /**
     * Creates the instance of the file accessor.
     *
     * @return The instance.
     */
    private static RepositoryFileAccessor createInstance() {
        if (Files.exists(RepositoryManager.getRepoRoot().resolve(".local"))) {
            LOGGER.debug("Using disperse file accessor.");
            return new DisperseFileAccessor();
        } else {
            LOGGER.debug("Using bundled file accessor.");
            return new BundledFileAccessor();
        }
    }

    /**
     * Gets a directory as a list of {@linkplain com.google.gson.JsonElement}s.
     *
     * @param path The path to the directory (relative to the repository).
     * @return The directory as list.
     */
    public abstract List<JsonElement> getDirectory(Path path);

    /**
     * Gets a file as a {@linkplain com.google.gson.JsonElement}.
     *
     * @param path The path to the file (without .json extension).
     * @return The file.
     */
    public abstract JsonElement getFile(Path path);

    public JsonElement getFile(String string) {
        return getFile(RepositoryManager.getRepoRoot().resolve(string));
    }

}
