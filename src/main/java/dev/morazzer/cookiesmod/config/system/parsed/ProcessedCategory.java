package dev.morazzer.cookiesmod.config.system.parsed;

import dev.morazzer.cookiesmod.config.system.Category;
import lombok.Getter;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ProcessedCategory {

	@Getter
	private final List<ProcessedOption<?, ?>> processedOptions = new ArrayList<>();
	private final Category category;

	public ProcessedCategory(Category category) {
		this.category = category;
	}

	public Text getName() {
		return category.getName();
	}

	public Text getDescription() {
		return category.getDescription();
	}

	public void addOption(ProcessedOption<?, ?> processedOption) {
		this.processedOptions.add(processedOption);
	}

}
