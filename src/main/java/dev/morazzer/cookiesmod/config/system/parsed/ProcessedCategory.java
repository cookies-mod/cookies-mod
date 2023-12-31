package dev.morazzer.cookiesmod.config.system.parsed;

import dev.morazzer.cookiesmod.config.system.Category;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import net.minecraft.text.Text;

/**
 * A processed category that is generic for all categories.
 */
public class ProcessedCategory {

    @Getter
    private final List<ProcessedOption<?, ?>> processedOptions = new ArrayList<>();
    private final Category category;

    /**
     * Creates a new processed category.
     *
     * @param category The category to be processed.
     */
    public ProcessedCategory(Category category) {
        this.category = category;
    }

    /**
     * Gets the name of the category.
     *
     * @return The name.
     */
    public Text getName() {
        return category.getName();
    }

    /**
     * Gets the description of the category.
     *
     * @return The description.
     */
    public Text getDescription() {
        return category.getDescription();
    }

    /**
     * Adds a processed option to the category.
     *
     * @param processedOption The option to add.
     */
    public void addOption(ProcessedOption<?, ?> processedOption) {
        this.processedOptions.add(processedOption);
    }

}
