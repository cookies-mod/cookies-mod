package dev.morazzer.cookiesmod.features.repository.constants;

import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class PlotCostData {

	List<Cost> center;
	List<Cost> middle;
	List<Cost> edges;
	List<Cost> corners;

	@Getter
	private static PlotCostData instance;

	public static boolean loaded() {
		if (instance == null && Files.exists(RepositoryManager.getRepoRoot().resolve("constants/plot_cost.json")) ) {
			instance = GsonUtils.gson.fromJson(ExceptionHandler.removeThrows(() -> Files.readString(RepositoryManager.getRepoRoot()
					.resolve("constants/plot_cost.json"), StandardCharsets.UTF_8)), PlotCostData.class);
		}
		return instance != null;
	}

	public List<Cost> getByIndex(int index) {
		return switch (index) {
			case 0 -> center;
			case 1 -> middle;
			case 2 -> edges;
			case 3 -> corners;
			default -> Collections.emptyList();
		};
	}

	public record Cost(int amount, boolean bundle) {
	}

}