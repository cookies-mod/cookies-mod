package dev.morazzer.cookiesmod.config.system.parsed;

import dev.morazzer.cookiesmod.config.system.Category;
import dev.morazzer.cookiesmod.config.system.Config;
import dev.morazzer.cookiesmod.config.system.Foldable;
import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.config.system.options.FoldableOption;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ConfigReader {

	@Getter
	private Config<?> config;
	private ProcessedCategory currentCategory;
	@Getter
	private final LinkedList<ProcessedCategory> categories = new LinkedList<>();
	private final AtomicInteger foldableId = new AtomicInteger(0);
	private final Stack<Integer> foldableStack = new Stack<>();

	public void beginCategory(Category category) {
		this.currentCategory = new ProcessedCategory(category);
		this.categories.add(this.currentCategory);
	}

	public void endCategory() {
		this.foldableId.set(0);
		if (!this.foldableStack.isEmpty()) {
			log.warn("End Category with {} open foldables", this.foldableId);
		}
		this.foldableStack.clear();
	}

	public void beginFoldable(Foldable foldable) {
		//noinspection rawtypes

		ProcessedOption processedOption = new ProcessedOption<>(new FoldableOption(foldable, foldableId.incrementAndGet()));
		if (!this.foldableStack.isEmpty()) {
			processedOption.setFoldable(this.foldableStack.peek());
		}
		this.currentCategory.addOption(processedOption);
		this.foldableStack.push(this.foldableId.get());
	}

	public void endFoldable() {
		this.foldableStack.pop();
	}

	public void beginConfig(Config<?> config) {
		this.config = config;
	}

	public void endConfig() {

	}

	public <T, O extends Option<T, O>> void processOption(Option<T, O> option) {
		ProcessedOption<T, O> processedOption = new ProcessedOption<>(option);
		if (!this.foldableStack.isEmpty()) {
			processedOption.setFoldable(this.foldableStack.peek());
		}
		this.currentCategory.addOption(processedOption);
	}
}
