package dev.morazzer.cookiesmod.features.repository;

import dev.morazzer.cookiesmod.features.repository.constants.Constants;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItemManager;
import dev.morazzer.cookiesmod.features.repository.items.recipe.RepositoryRecipeManager;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.HttpUtils;
import dev.morazzer.cookiesmod.utils.StringUtils;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHAsset;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

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
     * Adds a callback that's run if the repository is reloaded.
     *
     * @param runnable The callback to run.
     */
    public static void addReloadCallback(Runnable runnable) {
        reloadCallbacks.add(runnable);
    }

    /**
     * Reloads all repository entries.
     */
    public static void reload() {
        RepositoryItemManager.reloadItems();
        reloadCallbacks.forEach(Runnable::run);
    }

    /**
     * Loads the repository and download if not present.
     */
    public static void load() {
        try {
            GitHub gitHub = new GitHubBuilder().build();
            GHRepository repository = gitHub.getRepository("cookies-mod/cookies-mod-repo");
            if (!Files.exists(repoRoot)) {
                Files.createDirectories(repoRoot);
                List<GHAsset> list = repository.getLatestRelease().listAssets().toList();
                for (GHAsset ghAsset : list) {
                    if (ghAsset.getName().endsWith(".sha256")) {
                        continue;
                    }
                    Files.write(
                        repoRoot.resolve(ghAsset.getName()),
                        HttpUtils.getResponseBody(new URI(ghAsset.getBrowserDownloadUrl())),
                        StandardOpenOption.CREATE_NEW
                    );
                }
            } else {
                List<GHAsset> list = repository.getLatestRelease().listAssets().toList();
                List<GHAsset> hashes = new ArrayList<>(list.stream()
                    .filter(ghAsset -> ghAsset.getName().endsWith(".sha256"))
                    .toList());
                List<GHAsset> files = new ArrayList<>(list.stream().filter(Predicate.not(hashes::contains)).toList());
                MessageDigest sha256 = MessageDigest.getInstance("sha256");

                hashes.removeIf(hash -> {
                    String name = hash.getName();
                    Path path;
                    if (Files.exists(path = repoRoot.resolve(name.substring(0, name.lastIndexOf("."))))) {
                        String localHash;
                        try {
                            localHash = StringUtils.hex(sha256.digest(Files.readAllBytes(path))).substring(0, 40);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        String remoteHash = new String(
                            HttpUtils.getResponseBody(URI.create(hash.getBrowserDownloadUrl())),
                            StandardCharsets.UTF_8
                        );
                        return remoteHash.equalsIgnoreCase(localHash);
                    }
                    return false;
                });

                List<String> stringStream = hashes.stream().map(GHAsset::getName).toList();
                files.removeIf(ghAsset -> !stringStream.contains(ghAsset.getName() + ".sha256"));

                for (GHAsset file : files) {
                    log.info("Downloading file {} from {}", file.getName(), file.getBrowserDownloadUrl());
                    Path path = repoRoot.resolve(file.getName());
                    Files.writeString(
                        path,
                        new String(
                            HttpUtils.getResponseBody(new URI(file.getBrowserDownloadUrl())),
                            StandardCharsets.UTF_8
                        ),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                    );
                }
            }
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception);
        }

        RepositoryItemManager.loadItems();
        RepositoryRecipeManager.loadRecipes();
        Constants.load();
        reloadCallbacks.forEach(Runnable::run);
        finishedLoading = true;
    }

}
