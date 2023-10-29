package dev.morazzer.cookiesmod.features.repository;

import dev.morazzer.cookiesmod.features.repository.items.RepositoryItemManager;
import dev.morazzer.cookiesmod.features.repository.items.recipe.RepositoryRecipeManager;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manager to handle all repository-related actions.
 */
@Getter
@Slf4j
public class RepositoryManager {

    private static final CopyOnWriteArrayList<Runnable> reloadCallbacks = new CopyOnWriteArrayList<>();
    @Getter
    private static final Path repoRoot = Path.of("cookiesmod/repo");
    @Getter
    private static boolean finishedLoading = false;

    /**
     * Add a callback that is run if the repository is reloaded.
     *
     * @param runnable The callback to run.
     */
    public static void addReloadCallback(Runnable runnable) {
        reloadCallbacks.add(runnable);
    }

    /**
     * Reload all repository entries.
     */
    public static void reload() {
        RepositoryItemManager.reloadItems();
        reloadCallbacks.forEach(Runnable::run);
    }

    /**
     * Load the repository and download if not present.
     */
    public static void load() {
        if (!Files.exists(repoRoot)) {
            try {
                Files.createDirectories(repoRoot.getParent());
                Git
                        .cloneRepository()
                        .setDirectory(repoRoot.toFile())
                        .setURI("https://github.com/Morazzer/cookies-mod-repo.git")
                        .setBranch("master")
                        .call();
            } catch (Exception e) {
                throw new RuntimeException("Unable to clone repository", e);
            }
        } else {
            try {
                Git.open(repoRoot.toFile()).pull().call();
            } catch (Exception exception) {
                ExceptionHandler.handleException(exception);
            }
        }

        RepositoryItemManager.loadItems();
        //TagManager.loadTags();
        RepositoryRecipeManager.loadRecipes();
        reloadCallbacks.forEach(Runnable::run);
        finishedLoading = true;
    }

    /**
     * Get a resource from the repository as byte[].
     *
     * @param path The path relative to the repository root.
     * @return The resource.
     */
    public static Optional<byte[]> getResource(String path) {
        return Optional.of(ExceptionHandler.removeThrows(() -> Files.readAllBytes(repoRoot.resolve(path))));
    }

}
