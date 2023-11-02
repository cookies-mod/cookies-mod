package dev.morazzer.cookiesmod.config.system;

import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.config.system.editor.FoldableEditor;
import dev.morazzer.cookiesmod.config.system.parsed.ConfigReader;
import dev.morazzer.cookiesmod.config.system.parsed.ProcessedCategory;
import dev.morazzer.cookiesmod.config.system.parsed.ProcessedOption;
import dev.morazzer.cookiesmod.utils.maths.LinearInterpolatedInteger;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * The config screen to modify and change the values of each respective config option.
 */
public class ConfigScreen extends Screen {

    private final ConfigReader configReader;
    private final LinkedList<ProcessedOption<?, ?>> hiddenOptions = new LinkedList<>();
    private final LinkedList<ProcessedCategory> visibleCategories = new LinkedList<>();
    private final LinkedList<ProcessedCategory> allCategories = new LinkedList<>();
    private final LinearInterpolatedInteger categoryScrollbar = new LinearInterpolatedInteger(150, 0);
    private final LinearInterpolatedInteger optionsScrollbar = new LinearInterpolatedInteger(150, 0);
    private final ConcurrentHashMap<Integer, Integer> activeFoldables = new ConcurrentHashMap<>();
    private Optional<ProcessedCategory> selectedCategory;
    private TextFieldWidget searchField;
    private int sizeX;
    private int sizeY;
    private int windowX;
    private int windowY;
    private int categoryLeft;
    private int categoryTop;
    private int categoryRight;
    private int categoryBottom;
    private int optionsLeft;
    private int optionsTop;
    private int optionsRight;
    private int optionsBottom;
    private int optionDefaultWidth;
    private int innerPadding;
    private int optionsViewport;
    private int optionsAllSize;
    private int optionsScrollbarMin;
    private float optionScrollbarScale;
    private int optionsScrollbarPart;
    private int optionsScrollbarTop;
    private int optionsScrollbarLeft;
    private int optionsScrollbarBottom;
    private int optionsScrollbarRight;
    private int categoryViewport;
    private int categoryAllSize;
    private int categoryScrollbarMin;
    private float categoryScrollbarScale;
    private int categoryScrollbarPart;
    private int categoryScrollbarTop;
    private int categoryScrollbarLeft;
    private int categoryScrollbarBottom;
    private int categoryScrollbarRight;

    /**
     * Creates a new option screen.
     *
     * @param configReader The config reader.
     */
    public ConfigScreen(ConfigReader configReader) {
        super(Text.empty());
        this.configReader = configReader;
        this.allCategories.addAll(configReader.getCategories());
        this.visibleCategories.addAll(this.allCategories);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta) {
        if (this.selectedCategory.isEmpty() && !this.visibleCategories.isEmpty()) {
            this.setSelectedCategory(this.visibleCategories.peekFirst());
        }

        this.categoryScrollbar.tick();
        this.optionsScrollbar.tick();

        RenderUtils.renderRectangle(
            drawContext,
            (drawContext.getScaledWindowWidth() - this.sizeX) / 2,
            (drawContext.getScaledWindowHeight() - this.sizeY) / 2,
            this.sizeX, this.sizeY, true
        );
        drawContext.enableScissor(
            (drawContext.getScaledWindowWidth() - this.sizeX) / 2,
            (drawContext.getScaledWindowHeight() - this.sizeY) / 2,
            (drawContext.getScaledWindowWidth() + this.sizeX) / 2,
            (drawContext.getScaledWindowHeight() + this.sizeY) / 2
        );

        RenderUtils.renderRectangle(drawContext, this.windowX + 5, this.windowY + 5, sizeX - 10, 20, false);
        RenderUtils.renderRectangle(drawContext, this.windowX + 5, this.windowY + 29, 140, sizeY - 34, false);

        drawContext.fill(
            this.categoryLeft,
            this.categoryTop,
            this.categoryRight,
            this.categoryBottom,
            0x6008080E
        ); //Middle
        drawContext.fill(
            this.categoryRight - 1,
            this.categoryTop,
            this.categoryRight,
            this.categoryBottom,
            0xff28282E
        ); //Right
        drawContext.fill(
            this.categoryLeft,
            this.categoryBottom - 1,
            this.categoryRight,
            this.categoryBottom,
            0xff28282E
        ); //Bottom
        drawContext.fill(
            this.categoryLeft,
            this.categoryTop,
            this.categoryLeft + 1,
            this.categoryBottom,
            0xff08080E
        ); //Left
        drawContext.fill(
            this.categoryLeft,
            this.categoryTop,
            this.categoryRight,
            this.categoryTop + 1,
            0xff08080E
        ); //Top

        RenderUtils.renderCenteredTextWithMaxWidth(
            drawContext,
            configReader.getConfig().getTitle(),
            sizeX - 25,
            this.windowX + (int) (sizeX / 2.0f),
            this.windowY + 15,
            -1,
            true
        );

        drawContext.enableScissor(0, this.categoryTop + 1, drawContext.getScaledWindowWidth(),
            this.categoryBottom - 1
        );


        int categoryStartY = this.categoryScrollbar.getValue();
        for (ProcessedCategory visibleCategory : this.visibleCategories) {
            drawContext.drawCenteredTextWithShadow(
                this.textRenderer,
                visibleCategory.getName().copy().formatted(this.selectedCategory.map(visibleCategory::equals)
                    .orElse(false) ? Formatting.AQUA : Formatting.GRAY),
                this.windowX + 76,
                this.windowY + 70 + categoryStartY,
                ~0
            );
            categoryStartY += 15;
        }

        drawContext.fill(
            this.categoryScrollbarLeft,
            this.categoryScrollbarTop,
            this.categoryScrollbarRight,
            this.categoryScrollbarBottom,
            0xff << 24
        );

        int startCategories =
            (int) (this.categoryScrollbarTop + this.categoryScrollbar.getValue() * this.categoryScrollbarScale);

        drawContext.fill(
            this.categoryScrollbarLeft,
            startCategories,
            this.categoryScrollbarRight,
            Math.max(startCategories + this.categoryScrollbarPart, this.categoryScrollbarMin),
            0xffffffff
        );

        drawContext.disableScissor();

        drawContext.drawCenteredTextWithShadow(this.textRenderer, "Categories",
            this.windowX + 75, this.windowY + 44, 0xa368ef
        );


        RenderUtils.renderRectangle(drawContext, this.windowX + 149, this.windowY + 29, sizeX - 154, sizeY - 34, false);

        this.selectedCategory.filter(this.visibleCategories::contains)
            .ifPresent(processedCategory -> RenderUtils.renderTextWithMaxWidth(
                drawContext,
                processedCategory.getDescription(),
                this.optionsRight - 35 - this.optionsLeft,
                this.optionsLeft + 5,
                this.windowY + 40,
                ~0,
                true
            ));

        drawContext.fill(this.optionsLeft, this.optionsTop, this.optionsRight, this.optionsBottom, 0x6008080E); //Middle
        drawContext.fill(
            this.optionsRight - 1,
            this.optionsTop,
            this.optionsRight,
            this.optionsBottom,
            0xff303036
        ); //Right
        drawContext.fill(
            this.optionsLeft,
            this.optionsBottom - 1,
            this.optionsRight,
            this.optionsBottom,
            0xff303036
        ); //Bottom
        drawContext.fill(
            this.optionsLeft,
            this.optionsTop,
            this.optionsLeft + 1,
            this.optionsBottom,
            0xff08080E
        ); //Left
        drawContext.fill(this.optionsLeft, this.optionsTop, this.optionsRight, this.optionsTop + 1, 0xff08080E); //Top

        drawContext.fill(
            this.optionsScrollbarLeft,
            this.optionsScrollbarTop,
            this.optionsScrollbarRight,
            this.optionsScrollbarBottom,
            0xff << 24
        );

        int startOptions =
            (int) (this.optionsScrollbarTop + this.optionsScrollbar.getValue() * this.optionScrollbarScale);

        drawContext.fill(
            this.optionsScrollbarLeft,
            startOptions,
            this.optionsScrollbarRight,
            Math.max(startOptions + this.optionsScrollbarPart, this.optionsScrollbarMin),
            0xffffffff
        );

        this.executeForEachVisibleNotHidden((processedOption, positionX, positionY, optionWidth) -> {
            ConfigOptionEditor<?, ?> editor = processedOption.getEditor();

            drawContext.enableScissor(
                this.optionsLeft + 1,
                this.optionsTop + 1,
                this.optionsRight - 1,
                this.optionsBottom - 1
            );
            drawContext.enableScissor(
                positionX,
                positionY,
                positionX + optionWidth,
                positionY + editor.getHeight(optionWidth)
            );
            drawContext.getMatrices().push();
            drawContext.getMatrices().translate(positionX, positionY, 1);

            int localMouseX = mouseX - positionX;
            int localMouseY = mouseY - positionY;
            editor.render(drawContext, localMouseX, localMouseY, tickDelta, optionWidth);
            drawContext.disableScissor();
            drawContext.disableScissor();
            drawContext.getMatrices().translate(0, 0, 10);
            int finalLocalMouseX = ((mouseY >= this.optionsTop) && (mouseY < this.optionsBottom)) ? localMouseX : -1;

            editor.renderOverlay(drawContext, finalLocalMouseX, localMouseY, tickDelta, optionWidth);
            drawContext.getMatrices().pop();
        });
        drawContext.disableScissor();

        this.searchField.render(drawContext, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_ENTER && this.searchField.active) {
            this.searchField.active = false;
            this.searchField.setFocused(false);
            return true;
        }
        if (this.searchField.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        this.executeForEachVisibleNotHidden((processedOption, positionX, positionY, optionWidth) -> processedOption
            .getEditor()
            .keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        ConfigManager.saveConfig(true, "config-screen");
        super.close();
    }

    @Override
    protected void init() {
        this.searchField = new TextFieldWidget(textRenderer, 0, 0, 0, 18, Text.of(""));
        this.setSelectedCategory(this.allCategories.peekFirst());
        executeForEach(
            (processedOption, positionX, positionY, optionWidth) -> processedOption.getEditor().init(),
            true
        );
        resize(MinecraftClient.getInstance(), this.width, this.height);
        this.searchField.setChangedListener(text -> {
            this.repopulateActiveFoldables();
            this.updateSearchResults();
            this.setSearchBarWidth();
            this.repopulateHiddenOptions();
            this.recalculateOptionBarSize();
        });
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        int scaleFactor = (int) MinecraftClient.getInstance().getWindow().getScaleFactor();

        this.sizeX = Math.min(MinecraftClient.getInstance().getWindow().getScaledWidth() - 100 / scaleFactor, 500);
        this.sizeY = Math.min(MinecraftClient.getInstance().getWindow().getScaledHeight() - 100 / scaleFactor, 400);

        this.windowX = (MinecraftClient.getInstance().getWindow().getScaledWidth() - sizeX) / 2;
        this.windowY = (MinecraftClient.getInstance().getWindow().getScaledHeight() - sizeY) / 2;

        int adjustmentFactor = Math.max(2, scaleFactor);

        this.innerPadding = 20 / adjustmentFactor;
        this.categoryLeft = this.windowX + 5 + this.innerPadding;
        this.categoryRight = this.windowX + 145 - this.innerPadding;
        this.categoryTop = this.windowY + 49 + this.innerPadding;
        this.categoryBottom = this.windowY + sizeY - 5 - this.innerPadding;
        this.categoryViewport = this.categoryBottom - this.categoryTop;

        this.optionsTop = this.categoryTop;
        this.optionsLeft = this.windowX + 149 + this.innerPadding;
        this.optionsRight = this.windowX + sizeX - 5 - this.innerPadding;
        this.optionsBottom = this.windowY + sizeY - 5 - this.innerPadding;
        this.optionsViewport = this.optionsBottom - this.optionsTop;

        this.optionDefaultWidth = this.optionsRight - this.optionsLeft - 20;

        this.optionsScrollbarTop = this.optionsTop + 5;
        this.optionsScrollbarBottom = this.optionsBottom - 5;
        this.optionsScrollbarLeft = this.optionsRight - 10;
        this.optionsScrollbarRight = this.optionsRight - 5;


        this.categoryScrollbarTop = this.categoryTop + 5;
        this.categoryScrollbarBottom = this.categoryBottom - 5;
        this.categoryScrollbarLeft = this.categoryLeft + 5;
        this.categoryScrollbarRight = this.categoryLeft + 10;


        this.recalculateOptionBarSize();
        this.recalculateCategoryBarSize();
        this.setSearchBarWidth();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.searchField.isMouseOver(mouseX, mouseY) && button == 0) {
            this.searchField.setFocused(true);
            this.searchField.active = true;
            return true;
        }
        if ((mouseX > this.categoryLeft) && (mouseX < this.categoryRight)
            && (mouseY > this.categoryTop) && (mouseY < this.categoryBottom)) {
            int categoryStartY = this.categoryScrollbar.getValue();
            if (mouseX > this.categoryLeft + 20) {
                for (ProcessedCategory visibleCategory : this.visibleCategories) {
                    int categoryTextY = this.windowY + 70 + categoryStartY;
                    if (mouseY > categoryTextY && mouseY < categoryTextY + 15) {
                        setSelectedCategory(visibleCategory);
                        return true;
                    }

                    categoryStartY += 15;
                }
            }
        }

        this.searchField.setFocused(false);
        this.searchField.active = false;

        if (mouseY > this.optionsTop && mouseY < this.optionsBottom
            && mouseX > this.optionsLeft && mouseX < this.optionsRight) {
            AtomicBoolean consumed = new AtomicBoolean(false);
            this.executeForEachVisibleNotHidden((processedOption, positionX, positionY, optionWidth) -> {
                if (consumed.get()) {
                    return;
                }
                consumed.set(processedOption.getEditor()
                    .mouseClicked(mouseX - positionX, mouseY - positionY, button, optionWidth));
            });
        }


        this.repopulateActiveFoldables();
        this.recalculateOptionBarSize();
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.executeForEachVisibleNotHidden((processedOption, positionX, positionY, optionWidth) -> processedOption
            .getEditor()
            .mouseReleased(mouseX - positionX, mouseY - positionY, button));
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (mouseY > this.optionsTop && mouseY < this.optionsBottom
            && mouseX > this.optionsLeft && mouseX < this.optionsRight) {
            this.executeForEachVisibleNotHidden((processedOption, positionX, positionY, optionWidth) -> processedOption
                .getEditor()
                .mouseDragged(mouseX - positionX, mouseY - positionY, button, deltaX, deltaY, optionWidth));
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.executeForEachVisibleNotHidden((processedOption, positionX, positionY, optionWidth) -> processedOption
            .getEditor()
            .mouseScrolled(mouseX - positionX, mouseY - positionY, horizontalAmount, verticalAmount));
        if ((mouseY > this.optionsTop) && (mouseY < this.optionsBottom)
            && (mouseX > this.optionsLeft) && (mouseX < this.optionsRight)) {
            int newTarget = (int) (this.optionsScrollbar.getTarget() - verticalAmount * 30);
            this.optionsScrollbar.setTargetValue(Math.max(
                0,
                Math.min(this.optionsAllSize - this.optionsViewport, newTarget)
            ));
        }
        if ((mouseY > this.categoryTop) && (mouseY < this.categoryBottom)
            && (mouseX > this.categoryLeft) && (mouseX < this.categoryRight)) {
            int newTarget = (int) (this.categoryScrollbar.getTarget() - verticalAmount * 30);
            this.categoryScrollbar.setTargetValue(Math.max(
                0,
                Math.min(this.categoryAllSize - this.categoryViewport, newTarget)
            ));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        this.executeForEachVisibleNotHidden((processedOption, positionX, positionY, optionWidth) -> processedOption
            .getEditor()
            .keyReleased(keyCode, scanCode, modifiers));
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char character, int modifiers) {
        if (this.searchField.charTyped(character, modifiers)) {
            return true;
        }
        this.executeForEachVisibleNotHidden((processedOption, positionX, positionY, optionWidth) -> processedOption
            .getEditor()
            .charTyped(character, modifiers));
        return super.charTyped(character, modifiers);
    }

    /**
     * Recalculates all variables used to display the category scrollbar.
     */
    private void recalculateCategoryBarSize() {
        this.categoryScrollbarMin = this.categoryViewport / 20;

        if (this.selectedCategory.isEmpty()) {
            this.categoryAllSize = this.optionsViewport - 10;
            this.categoryScrollbarPart = this.optionsViewport;
            return;
        }

        this.categoryAllSize = 5 + this.visibleCategories.size() * 15;
        int scrollbarHeight = (this.categoryBottom - this.categoryTop - 10);

        this.categoryScrollbarScale = scrollbarHeight / (float) this.categoryAllSize;

        this.categoryScrollbarPart = (int) Math.min(
            (this.categoryViewport * this.categoryScrollbarScale),
            this.categoryViewport - 10
        );

        this.categoryScrollbar.setValue(Math.max(
            0,
            Math.min(this.categoryAllSize - this.categoryViewport, this.categoryScrollbar.getValue())
        ));
    }

    /**
     * Recalculates all variables used to display the option scrollbar.
     */
    private void recalculateOptionBarSize() {
        this.optionsScrollbarMin = this.optionsViewport / 20;

        if (this.selectedCategory.isEmpty()) {
            this.optionsAllSize = this.optionsViewport - 10;
            this.optionsScrollbarPart = this.optionsViewport - 10;
            return;
        }

        this.optionsAllSize = 5;
        for (ProcessedOption<?, ?> processedOption : this.selectedCategory.get().getProcessedOptions()) {
            if ((processedOption.getFoldable() >= 0 && !activeFoldables.containsKey(processedOption.getFoldable()))
                || this.hiddenOptions.contains(processedOption)) {
                continue;
            }
            int optionWidth = getOptionSize(processedOption);
            this.optionsAllSize += processedOption.getEditor().getHeight(optionWidth) + 5;
        }
        int scrollbarHeight = (this.optionsBottom - this.optionsTop - 10);

        this.optionScrollbarScale = scrollbarHeight / (float) this.optionsAllSize;

        this.optionsScrollbarPart = (int) Math.min(
            (this.optionsViewport * this.optionScrollbarScale),
            this.optionsViewport - 10
        );

        this.optionsScrollbar.setValue(Math.max(
            0,
            Math.min(this.optionsAllSize - this.optionsViewport, this.optionsScrollbar.getValue())
        ));
    }

    /**
     * Sets the search bar width to a newly calculated value.
     */
    private void setSearchBarWidth() {
        int length = this.textRenderer.getWidth(this.searchField.getText());
        this.searchField.setWidth(length + 20);
        this.searchField.setPosition(
            this.optionsRight - 25 - length,
            this.optionsTop - (20 + this.innerPadding) / 2 - 9
        );
    }

    /**
     * Repopulates the active foldable list to reflect the current state.
     */
    private void repopulateActiveFoldables() {
        this.activeFoldables.clear();
        if (this.selectedCategory.isEmpty()) {
            return;
        }
        for (ProcessedOption<?, ?> processedOption : this.selectedCategory.get().getProcessedOptions()) {
            if (processedOption.getFoldable() >= 0) {
                if (!this.activeFoldables.containsKey(processedOption.getFoldable())) {
                    continue;
                }
            }
            ConfigOptionEditor<?, ?> editor = processedOption.getEditor();
            if (editor == null) {
                continue;
            }
            if (editor instanceof FoldableEditor foldableEditor) {
                if (foldableEditor.isActive() || !this.searchField.getText().isEmpty()) {
                    int depth = 0;
                    if (processedOption.getFoldable() >= 0) {
                        depth = this.activeFoldables.get(processedOption.getFoldable()) + 1;
                    }
                    this.activeFoldables.put(foldableEditor.getFoldableId(), depth);
                }
            }
        }
    }

    /**
     * Gets the size that an option has to be rendered in.
     *
     * @param processedOption The option to get the size for.
     * @return The size the option has to be rendered in.
     */
    private int getOptionSize(ProcessedOption<?, ?> processedOption) {
        if (processedOption.getFoldable() >= 0) {
            if (!activeFoldables.containsKey(processedOption.getFoldable())) {
                return -1;
            }

            int foldableDepth = activeFoldables.get(processedOption.getFoldable());
            return this.optionDefaultWidth - (2 * this.innerPadding) * (foldableDepth + 1);
        }

        return this.optionDefaultWidth;
    }

    /**
     * Updates the current search results to include/exclude new changes.
     */
    private void updateSearchResults() {
        String search = this.searchField.getText();
        this.visibleCategories.clear();
        this.visibleCategories.addAll(allCategories);
        if (search.isEmpty()) {
            return;
        }
        this.visibleCategories.removeIf(Predicate.not(processedCategory -> processedCategory.getName().getString()
            .contains(search)
            || processedCategory.getDescription().getString().contains(search)
            || processedCategory.getProcessedOptions().stream()
            .anyMatch(option -> option.getEditor().doesMatchSearch(search))));
        if (this.visibleCategories.isEmpty()) {
            return;
        }
        if (this.selectedCategory.filter(this.visibleCategories::contains).isEmpty()) {
            this.setSelectedCategory(this.visibleCategories.peekFirst());
        }
    }

    /**
     * Changes the selected category.
     *
     * @param processedCategory The new category.
     */
    private void setSelectedCategory(ProcessedCategory processedCategory) {
        this.selectedCategory = Optional.ofNullable(processedCategory);
        this.selectedCategory.ifPresent(processedCategory1 -> processedCategory1.getProcessedOptions()
            .forEach(processedOption -> processedOption.getEditor().init()));

        this.repopulateHiddenOptions();
        this.repopulateActiveFoldables();
        this.recalculateOptionBarSize();
        this.updateSearchResults();
    }

    /**
     * Repopulates a list of all currently hidden options, which have been excluded through the search.
     */
    private void repopulateHiddenOptions() {
        String search = this.searchField.getText();
        this.hiddenOptions.clear();
        if (search.isEmpty()) {
            return;
        }
        List<Integer> matchedFoldables = new ArrayList<>();
        this.selectedCategory.ifPresent(processedCategory -> {
            for (ProcessedOption<?, ?> processedOption : processedCategory.getProcessedOptions()) {
                if (!processedOption.getEditor()
                    .doesMatchSearch(search) && !matchedFoldables.contains(processedOption.getFoldable())) {
                    this.hiddenOptions.add(processedOption);
                } else if (processedOption.getEditor() instanceof FoldableEditor editor) {
                    matchedFoldables.add(editor.foldableId);
                }
            }
        });
    }

    /**
     * Executes an operation for all options that are in the current category.
     *
     * @param executor The executor to be called.
     * @param all      If all or only, the visible options should be used.
     */
    private void executeForEach(ProcessedOptionExecutor executor, @SuppressWarnings("SameParameterValue") boolean all) {
        if (this.selectedCategory.isEmpty()) {
            return;
        }
        int optionsY = -this.optionsScrollbar.getValue();
        for (ProcessedOption<?, ?> processedOption : this.selectedCategory.get().getProcessedOptions()) {
            int optionWidth = getOptionSize(processedOption);
            if (optionWidth == -1) {
                continue;
            }
            ConfigOptionEditor<?, ?> editor = processedOption.getEditor();
            if (editor == null) {
                continue;
            }

            int finalX = (this.optionsLeft + this.optionsRight - optionWidth) / 2 - 5;
            int finalY = this.optionsTop + 5 + optionsY;

            if (all || ((finalY + editor.getHeight(optionWidth) > this.optionsTop + 1)
                && (finalY < this.optionsBottom - 1))) {
                executor.execute(processedOption, finalX, finalY, optionWidth);
            }


            optionsY += editor.getHeight(optionWidth) + 5;
        }
    }

    /**
     * Executes an operation for all options that are in the current category that are visible and not hidden.
     *
     * @param executor The executor to be called.
     */
    private void executeForEachVisibleNotHidden(ProcessedOptionExecutor executor) {
        if (this.selectedCategory.isEmpty()) {
            return;
        }
        int optionsY = -this.optionsScrollbar.getValue();
        for (ProcessedOption<?, ?> processedOption : this.selectedCategory.get().getProcessedOptions()) {
            int optionWidth = getOptionSize(processedOption);
            if (optionWidth == -1) {
                continue;
            }
            ConfigOptionEditor<?, ?> editor = processedOption.getEditor();
            if (editor == null) {
                continue;
            }
            if (this.hiddenOptions.contains(processedOption)) {
                continue;
            }

            int finalX = (this.optionsLeft + this.optionsRight - optionWidth) / 2 - 5;
            int finalY = this.optionsTop + 5 + optionsY;

            if (((finalY + editor.getHeight(optionWidth)) > (this.optionsTop + 1))
                && (finalY < (this.optionsBottom - 1))) {
                executor.execute(processedOption, finalX, finalY, optionWidth);
            }

            optionsY += editor.getHeight(optionWidth) + 5;
        }
    }

    @FunctionalInterface
    private interface ProcessedOptionExecutor {

        /**
         * Called for a specific option to provide more generic values.
         *
         * @param processedOption The option.
         * @param positionX       The x position of the option.
         * @param positionY       The y position of the option.
         * @param optionWidth     The width the option has to be rendered in.
         */
        void execute(ProcessedOption<?, ?> processedOption, int positionX, int positionY, int optionWidth);

    }

}
