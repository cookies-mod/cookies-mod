package dev.morazzer.cookiesmod.features.repository.constants;

import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

/**
 * The plot cost data for the repository.
 */
public class PlotCostData {

    @Getter
    private static PlotCostData instance;
    List<Cost> center;
    List<Cost> middle;
    List<Cost> edges;
    List<Cost> corners;

    /**
     * Whether the data was loaded, if not try to load it.
     *
     * @return Whether the data was successfully loaded.
     */
    public static boolean loaded() {
        if (instance == null && Files.exists(RepositoryManager.getRepoRoot().resolve("constants/plot_cost.json"))) {
            instance = GsonUtils.gson.fromJson(ExceptionHandler.removeThrows(() -> Files.readString(RepositoryManager
                    .getRepoRoot()
                    .resolve("constants/plot_cost.json"), StandardCharsets.UTF_8)), PlotCostData.class);
        }
        return instance != null;
    }

    /**
     * Gets the corresponding plot cost list by the index.
     *
     * @param index The index of the plot list.
     * @return The list.
     */
    public List<Cost> getByIndex(int index) {
        return switch (index) {
            case 0 -> center;
            case 1 -> middle;
            case 2 -> edges;
            case 3 -> corners;
            default -> Collections.emptyList();
        };
    }

    /**
     * Record to save the cost for one plot
     *
     * @param amount The number of items.
     * @param bundle If the item is a bundle or a normal compost.
     */
    public record Cost(
            int amount,
            boolean bundle
    ) {}

}
