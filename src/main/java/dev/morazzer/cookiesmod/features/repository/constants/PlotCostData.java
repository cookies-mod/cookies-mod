package dev.morazzer.cookiesmod.features.repository.constants;

import dev.morazzer.cookiesmod.features.repository.files.RepositoryFileAccessor;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import java.util.Collections;
import java.util.List;

/**
 * The plot cost data for the repository.
 */
public record PlotCostData(List<Cost> center,
                           List<Cost> middle,
                           List<Cost> edges,
                           List<Cost> corners) {

    private static PlotCostData instance;

    public static PlotCostData getInstance() {
        return instance;
    }

    public static void load() {
        instance = JsonUtils.GSON
            .fromJson(RepositoryFileAccessor.getInstance().getFile("constants/plot_cost"), PlotCostData.class);
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
     * Record to save the cost for one plot.
     *
     * @param amount The number of items.
     * @param bundle If the item is a bundle or a normal compost.
     */
    public record Cost(
        int amount,
        boolean bundle
    ) {
    }

}
