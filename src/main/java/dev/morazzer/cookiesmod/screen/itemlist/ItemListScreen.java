package dev.morazzer.cookiesmod.screen.itemlist;

import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.config.categories.ItemListConfig;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItem;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItemManager;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.screen.widgets.EnumCycleWidget;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

@LoadModule
public class ItemListScreen implements Module {


	private final int itemSize = 18;

	private ButtonWidget leftButton;
	private ButtonWidget rightButton;
	private int itemListStartX;
	private int itemListStartY;
	private int itemListXOffset;
	private int itemListWidth;
	private int itemListHeight;
	private int itemsPerRow;
	private int itemsOnPage;
	private int page;
	private int pageCount;
	private int pageNumberX;
	private int pageNumberY;

	private EnumCycleWidget<AlphabeticalSort, Comparator<Identifier>> sortAlphabetical;
	private EnumCycleWidget<RaritySort, Comparator<Identifier>> sortRarity;
	private EnumCycleWidget<RarityFilter, Predicate<Identifier>> filterRarity;
	private EnumCycleWidget<CategoryFilter, Predicate<Identifier>> filterCategory;
	private EnumCycleWidget<MuseumFilter, Predicate<Identifier>> filterMuseum;

	private TextFieldWidget textFieldWidget;

	CopyOnWriteArrayList<Identifier> items = new CopyOnWriteArrayList<>();

	public ItemListScreen() {
	}

	@Override
	public void load() {
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			ItemListConfig config = ConfigManager.getConfig().itemListConfig;
			if (!config.enableItemList) {
				return;
			}

			if (!(screen instanceof HandledScreen<?>)) {
				return;
			}

			this.textFieldWidget = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 20, 20, this.textFieldWidget, Text.empty());
			this.textFieldWidget.setChangedListener(o -> this.filterAndSort());
			this.items = new CopyOnWriteArrayList<>(RepositoryItemManager.getAllItems());
			filterAndSort();
			init(screen);
			ScreenEvents.afterRender(screen).register(this::render);
			ScreenMouseEvents.afterMouseClick(screen).register(this::mouseClick);
			ScreenKeyboardEvents.allowKeyPress(screen).register(this::keyPress);

			if (config.sort.enableAlphabeticalSort) this.sortAlphabetical.setEnumIndex(0);
			if (config.sort.enableItemRaritySort) this.sortRarity.setEnumIndex(0);
			if (config.filters.enableRarityFilter) this.sortRarity.setEnumIndex(0);
			if (config.filters.enableCategoryFilter) this.filterCategory.setEnumIndex(0);
			if (config.filters.enableMuseumFilter) this.filterMuseum.setEnumIndex(0);
		});

		this.sortAlphabetical = new EnumCycleWidget<>(
				0,
				0,
				this.itemSize + 2,
				this.itemSize + 2,
				AlphabeticalSort.values(),
				AlphabeticalSort::getIdentifier,
				AlphabeticalSort::getText,
				EnumCycleWidget.OnClick.identity(this::sort),
				alphabeticalSort -> switch (alphabeticalSort) {
					case NORMAL ->
							Comparator.comparing((Identifier o) -> RepositoryItemManager.getItem(o).getItemNameAlphaNumerical());
					case REVERSED ->
							Comparator.comparing((Identifier o) -> RepositoryItemManager.getItem(o).getItemNameAlphaNumerical()).reversed();
				}
		);

		this.sortRarity = new EnumCycleWidget<>(
				0,
				0,
				this.itemSize + 2,
				this.itemSize + 2,
				RaritySort.values(),
				RaritySort::getIdentifier,
				RaritySort::getText,
				EnumCycleWidget.OnClick.identity(this::sort),
				raritySort -> switch (raritySort) {
					case UNSORTED -> Comparator.comparingInt(value -> 0);
					case LOWEST_FIRST ->
							Comparator.comparingInt((Identifier o) -> RepositoryItemManager.getItem(o).getTier().ordinal());
					case HIGHEST_FIRST ->
							Comparator.comparingInt((Identifier o) -> RepositoryItemManager.getItem(o).getTier().ordinal()).reversed();
				}
		);

		this.filterRarity = new EnumCycleWidget<>(
				0,
				0,
				this.itemSize + 2,
				this.itemSize + 2,
				RarityFilter.values(),
				RarityFilter::getIdentifier,
				RarityFilter::getText,
				EnumCycleWidget.OnClick.identity(this::filterAndSort),
				rarityFilter -> switch (rarityFilter) {
					case NO_FILTER ->
							i -> RepositoryItemManager.getItem(i).getTier() != RepositoryItem.Tier.UNOBTAINABLE;
					case COMMON -> i -> RepositoryItemManager.getItem(i).getTier() == RepositoryItem.Tier.COMMON;
					case UNCOMMON -> i -> RepositoryItemManager.getItem(i).getTier() == RepositoryItem.Tier.UNCOMMON;
					case RARE -> i -> RepositoryItemManager.getItem(i).getTier() == RepositoryItem.Tier.RARE;
					case EPIC -> i -> RepositoryItemManager.getItem(i).getTier() == RepositoryItem.Tier.EPIC;
					case LEGENDARY -> i -> RepositoryItemManager.getItem(i).getTier() == RepositoryItem.Tier.LEGENDARY;
					case MYTHIC -> i -> RepositoryItemManager.getItem(i).getTier() == RepositoryItem.Tier.MYTHIC;
					case SPECIAL -> i -> RepositoryItemManager.getItem(i).getTier() == RepositoryItem.Tier.SPECIAL
							|| RepositoryItemManager.getItem(i).getTier() == RepositoryItem.Tier.VERY_SPECIAL;
					case ADMIN -> i -> RepositoryItemManager.getItem(i).getTier() == RepositoryItem.Tier.ADMIN;
					case UNOBTAINABLE ->
							i -> RepositoryItemManager.getItem(i).getTier() == RepositoryItem.Tier.UNOBTAINABLE;
				}
		);

		this.filterCategory = new EnumCycleWidget<>(
				0,
				0,
				this.itemSize + 2,
				this.itemSize + 2,
				CategoryFilter.values(),
				CategoryFilter::getIdentifier,
				CategoryFilter::getText,
				EnumCycleWidget.OnClick.identity(this::filterAndSort),
				CategoryFilter::getPredicate
		);

		this.filterMuseum = new EnumCycleWidget<>(
				0,
				0,
				this.itemSize + 2,
				this.itemSize + 2,
				MuseumFilter.values(),
				MuseumFilter::getIdentifier,
				MuseumFilter::getText,
				EnumCycleWidget.OnClick.identity(this::filterAndSort),
				museumFilter -> switch (museumFilter) {
					case UNFILTERED -> identifier -> true;
					case MUSEUM -> identifier -> RepositoryItemManager.getItem(identifier).isMuseum();
					case NON_MUSEUM -> identifier -> !RepositoryItemManager.getItem(identifier).isMuseum();
				}
		);

		this.leftButton = new ButtonWidget.Builder(Text.literal("<"), button -> this.page--).size(this.itemSize, this.itemSize).build();
		this.rightButton = new ButtonWidget.Builder(Text.literal(">"), button -> this.page++).size(this.itemSize, this.itemSize).build();

		RepositoryManager.addReloadCallback(this::filterAndSort);
	}

	@Override
	public String getIdentifierPath() {
		return "itemlist";
	}

	private boolean search(Identifier identifier) {
		RepositoryItem item = RepositoryItemManager.getItem(identifier);
		String content = this.textFieldWidget.getText();

		if (content.isBlank()) {
			return true;
		}

		if (content.startsWith("type:")) {
			String inputType = content.substring(5);
			return this.identifierMatches(item.getMaterial(), inputType);
		}

		if (content.startsWith("actual_type:")) {
			String inputIdentifier = content.substring(12);
			return this.identifierMatches(Registries.ITEM.getId(item.getItemStack().getValue().getItem()), inputIdentifier);
		}

		if (content.startsWith("id:")) {
			String inputId = content.substring(3);
			return this.identifierMatches(item.getSkyblockId(), inputId);
		}

		if (item.getItemNameAlphaNumerical().toLowerCase().contains(content.toLowerCase())) {
			return true;
		}

		return item.getTooltipAsString(TooltipContext.BASIC).toLowerCase().contains(content.toLowerCase());
	}

	private boolean identifierMatches(Identifier identifier, String identifierPart) {
		return identifier.getPath().startsWith(identifierPart)
				|| identifier.toString().startsWith(identifierPart);
	}

	private boolean keyPress(Screen screen, int keyCode, int scanCode, int modifiers) {
		boolean b = !this.textFieldWidget.keyPressed(keyCode, scanCode, modifiers);
		if (this.textFieldWidget.isFocused()) {
			return false;
		}

		return b;
	}

	private void mouseClick(Screen screen, double mouseX, double mouseY, int button) {
		this.leftButton.mouseClicked(mouseX, mouseY, button);
		this.rightButton.mouseClicked(mouseX, mouseY, button);

		if (this.sortAlphabetical.visible) this.sortAlphabetical.mouseClicked(mouseX, mouseY, button);
		if (this.sortRarity.visible) this.sortRarity.mouseClicked(mouseX, mouseY, button);
		if (this.filterRarity.visible) this.filterRarity.mouseClicked(mouseX, mouseY, button);
		if (this.filterCategory.visible) this.filterCategory.mouseClicked(mouseX, mouseY, button);
		if (this.filterMuseum.visible) this.filterMuseum.mouseClicked(mouseX, mouseY, button);

		this.textFieldWidget.setFocused(this.textFieldWidget.isMouseOver(mouseX, mouseY));
	}

	private void init(Screen screen) {
		screen.addSelectableChild(this.textFieldWidget);
		//screen.setInitialFocus(this.textFieldWidget);

		HandledScreen<?> handledScreen = (HandledScreen<?>) screen;

		this.itemListStartX = Math.max(screen.width / 2 + handledScreen.backgroundWidth / 2, screen.width - 300);
		this.itemListStartY = 6 + this.itemSize;
		boolean hitsInventoryOnTheLeftSide = this.itemListStartX - 10 <= screen.width / 2 + handledScreen.backgroundWidth / 2;
		if (hitsInventoryOnTheLeftSide) {
			this.itemListStartX += 10;
		}

		int tempX = screen.width - this.itemListStartX;
		int tempY = screen.height - 42;

		int itemListWidthMissing = (tempX % this.itemSize);
		int itemListHeightMissing = (tempY % this.itemSize);
		this.itemListWidth = tempX - itemListWidthMissing;
		this.itemListHeight = tempY - itemListHeightMissing;

		this.itemListStartY += itemListHeightMissing / 2;
		this.itemListXOffset = itemListWidthMissing / 2;

		this.itemsPerRow = this.itemListWidth / this.itemSize;
		int itemsPerColumn = this.itemListHeight / this.itemSize;
		this.itemsOnPage = itemsPerColumn * this.itemsPerRow;

		this.leftButton.setX(this.itemListStartX + this.itemListXOffset);
		this.leftButton.setY(this.itemListStartY / 2 - this.itemSize / 2);

		this.rightButton.setX(this.itemListStartX + this.itemListWidth - this.rightButton.getWidth() + this.itemListXOffset);
		this.rightButton.setY(this.itemListStartY / 2 - this.itemSize / 2);

		this.pageCount = this.items.size() / this.itemsOnPage;

		this.pageNumberX = this.itemListStartX + this.itemListXOffset + this.itemListWidth / 2;
		this.pageNumberY = this.itemListStartY / 2 - this.itemSize / 2 + 5;

		int index = 0;

		ItemListConfig config = ConfigManager.getConfig().itemListConfig;

		this.sortAlphabetical.visible = config.sort.enableAlphabeticalSort;
		this.sortRarity.visible = config.sort.enableItemRaritySort;

		if (this.sortAlphabetical.visible) {
			//noinspection ConstantValue
			this.sortAlphabetical.setPosition(this.itemListStartX + this.itemListXOffset - 1 + (this.itemSize + 5) * index++, screen.height - 22);
		}

		if (this.sortRarity.visible) {
			//noinspection UnusedAssignment
			this.sortRarity.setPosition(this.itemListStartX + this.itemListXOffset - 1 + (this.itemSize + 5) * index++, screen.height - 22);
		}

		index = 0;
		this.filterRarity.visible = config.filters.enableRarityFilter;
		this.filterCategory.visible = config.filters.enableCategoryFilter;
		this.filterMuseum.visible = config.filters.enableMuseumFilter;

		if (this.filterRarity.visible) {
			//noinspection ConstantValue
			this.filterRarity.setPosition(screen.width - this.itemSize - 5 - (this.itemSize + 5) * index++, screen.height - 22);
		}
		if (this.filterCategory.visible) {
			this.filterCategory.setPosition(screen.width - this.itemSize - 5 - (this.itemSize + 5) * index++, screen.height - 22);
		}
		if (this.filterMuseum.visible) {
			//noinspection UnusedAssignment - easier to just keep it in case something changes about the sorting
			this.filterMuseum.setPosition(screen.width - this.itemSize - 5 - (this.itemSize + 5) * index++, screen.height - 22);
		}


		this.textFieldWidget.setWidth(handledScreen.backgroundWidth);
		this.textFieldWidget.setPosition(screen.width / 2 - handledScreen.backgroundWidth / 2, screen.height - 22);
	}

	public void filterAndSort() {
		this.filter();
		this.sort();
	}

	public void sort() {
		this.items.sort(this.sortRarity.getValue()
				.thenComparing(this.sortAlphabetical.getValue())
		);
	}

	public void filter() {
		this.items.clear();
		this.items.addAll(RepositoryItemManager.getAllItems());
		this.items.removeIf(Predicate.not(this.filterRarity.getValue())
				.or(Predicate.not(this::search))
				.or(Predicate.not(this.filterCategory.getValue()))
				.or(Predicate.not(this.filterMuseum.getValue())));
		this.pageCount = this.items.size() / Math.max(this.itemsOnPage, 1);
	}

	public void ensurePageIsValid() {
		this.page = MathHelper.clamp(this.page, 0, this.pageCount);
	}

	private void render(Screen screen, DrawContext context, int mouseX, int mouseY, float tickDelta) {
		this.drawBackground(context, screen);

		this.ensurePageIsValid();

		this.leftButton.render(context, mouseX, mouseY, tickDelta);
		this.rightButton.render(context, mouseX, mouseY, tickDelta);
		this.drawPageText(context);

		this.drawItemList(screen, context, mouseX, mouseY);

		this.drawControlBar(context, mouseX, mouseY, tickDelta);
	}

	public void drawBackground(DrawContext context, Screen screen) {
		context.fill(this.itemListStartX, 0, screen.width, screen.height, 0x66404040);
	}

	public void drawPageText(DrawContext context) {
		context.drawCenteredTextWithShadow(
				MinecraftClient.getInstance().textRenderer,
				"Page %s of %s".formatted(this.page + 1, this.pageCount + 1),
				this.pageNumberX,
				this.pageNumberY,
				~0
		);
	}

	public void drawItemList(Screen screen, DrawContext context, int mouseX, int mouseY) {
		for (int x = 0; x < this.itemListWidth / this.itemSize; x++) {
			for (int y = 0; y < this.itemListHeight / this.itemSize; y++) {

				int itemX = this.itemListXOffset + this.itemListStartX + x * this.itemSize;
				int itemY = this.itemListStartY + y * this.itemSize;

				int itemIndex = this.itemsOnPage * this.page + this.itemsPerRow * y + x;

				if (itemIndex > this.items.size() - 1) {
					//context.fill(itemX, itemY, itemX + this.itemSize, itemY + this.itemSize, 0xFFFF << 16);
					continue;
				}

				//context.fill(itemX, itemY, itemX + this.itemSize, itemY + this.itemSize, (x % 2 ^ y % 2) == 0 ? 0x55FFFFFF : 0x55 << 24);

				ItemStack value = RepositoryItemManager.getItem(items.get(itemIndex)).getItemStack().getValue();
				if (mouseX >= itemX && mouseX < itemX + this.itemSize && mouseY >= itemY && mouseY < itemY + this.itemSize) {
					this.drawItemTooltip(screen, context, value, mouseX, mouseY);
				}

				((HandledScreen<?>) screen).drawItem(context, value, itemX + 1, itemY + 1, "");
			}
		}
	}

	public void drawItemTooltip(Screen screen, DrawContext context, ItemStack itemStack, int mouseX, int mouseY) {
		context.getMatrices().push();
		context.getMatrices().translate(0.0F, 0.0F, 234.0F);
		context.drawTooltip(screen.textRenderer, Screen.getTooltipFromItem(MinecraftClient.getInstance(), itemStack), Optional.empty(), mouseX, mouseY);
		context.getMatrices().pop();
	}

	public void drawControlBar(DrawContext context, int mouseX, int mouseY, float delta) {
		this.sortAlphabetical.render(context, mouseX, mouseY, delta);
		this.sortRarity.render(context, mouseX, mouseY, delta);
		this.filterRarity.render(context, mouseX, mouseY, delta);
		this.filterCategory.render(context, mouseX, mouseY, delta);
		this.filterMuseum.render(context, mouseX, mouseY, delta);

		this.textFieldWidget.render(context, mouseX, mouseY, delta);
	}
}
