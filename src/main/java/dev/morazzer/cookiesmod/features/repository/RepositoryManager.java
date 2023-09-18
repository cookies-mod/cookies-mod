package dev.morazzer.cookiesmod.features.repository;

import dev.morazzer.cookiesmod.features.repository.items.RepositoryItemManager;
import dev.morazzer.cookiesmod.features.repository.items.recipe.RepositoryRecipeManager;
import dev.morazzer.cookiesmod.features.repository.items.tags.TagManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Slf4j
public class RepositoryManager {

	private static final CopyOnWriteArrayList<Runnable> reloadCallbacks = new CopyOnWriteArrayList<>();

	@Getter
	private static final Path repoRoot = Path.of("cookiesmod/repo");

	public static void addReloadCallback(Runnable runnable) {
		reloadCallbacks.add(runnable);
	}

	public static void reload() {
		RepositoryItemManager.reloadItems();
		reloadCallbacks.forEach(Runnable::run);
	}

	public static void load() {
		if (!Files.exists(repoRoot)) {
			try {
				Files.createDirectories(repoRoot.getParent());
				Git.cloneRepository()
						.setDirectory(repoRoot.toFile())
						.setURI("https://github.com/Morazzer/cookies-mod-repo.git")
						.setBranch("master")
						.call();
			} catch (Exception e) {
				throw new RuntimeException("Unable to clone repository", e);
			}
		}

		RepositoryItemManager.loadItems();
		TagManager.loadTags();
		RepositoryRecipeManager.loadRecipes();
		reloadCallbacks.forEach(Runnable::run);
	}
}
