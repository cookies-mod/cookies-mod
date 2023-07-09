package dev.morazzer.morassbmod.gui.screen.config;

import dev.morazzer.morassbmod.MorasSbMod;
import dev.morazzer.morassbmod.gui.screen.element.ClickableTextWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConfigScreenCategoryListWidget extends ElementListWidget<ConfigScreenCategoryListWidget.Entry> {

    private final ConfigScreen parent;

    public ConfigScreenCategoryListWidget(ConfigScreen parent, MinecraftClient minecraftClient) {
        super(minecraftClient, parent.eighthWidth, parent.halfHeight, (int) Math.nextUp(parent.eighthHeight + parent.eighthHeight * 0.33), parent.height - parent.eighthHeight, 20);
        System.out.println(parent.eighthWidth);
        System.out.println(parent.width / 8);
        this.parent = parent;
        Set<Identifier> availableTools = IntStream.rangeClosed(0, 20).mapToObj(i -> new Identifier("morassbmod", "debug/" + i)).collect(Collectors.toSet());
        this.setRenderBackground(false);
        this.setRenderHeader(false, 0);
        this.setRenderHorizontalShadows(false);
        this.width = parent.eighthWidth;
        this.setLeftPos(parent.eighthWidth);

        for (Identifier availableTool : availableTools) {
            this.addEntry(new Entry(availableTool, this));
        }
    }

    public void updateSize() {
        super.updateSize(parent.eighthWidth, (int) Math.nextUp(parent.eighthHeight + parent.eighthHeight * 0.33), parent.eighthHeight, parent.height - parent.eighthHeight);
        this.setLeftPos(parent.eighthWidth);
        this.children().forEach(Entry::resize);
    }

    public void setSelected(Identifier identifier) {
        this.children().stream().filter(e -> e.isSelected).forEach(Entry::unselect);
    }

    @Override
    protected int getScrollbarPositionX() {
        return parent.quarterWidth;
    }

    @Override
    public int getRowWidth() {
        return parent.eighthWidth;
    }


    @Environment(EnvType.CLIENT)
    public class Entry extends ElementListWidget.Entry<ConfigScreenCategoryListWidget.Entry> implements AutoCloseable {
        private final Identifier identifier;
        private ClickableWidget textWidget = null;
        boolean isSelected;

        public Entry(Identifier categoryIdentifier, ConfigScreenCategoryListWidget parent) {
            this.identifier = categoryIdentifier;
            MutableText translatable = Text.translatable(categoryIdentifier.toTranslationKey());
            this.textWidget = new ClickableTextWidget(
                    translatable,
                    parent.client.textRenderer,
                    () -> {
                        parent.setSelected(categoryIdentifier);
                        this.isSelected = true;
                        this.textWidget.setMessage(Text.translatable(categoryIdentifier.toTranslationKey()).styled(style -> style.withColor(MorasSbMod.color).withUnderline(true)));
                    },
                    parent.parent.eighthWidth,
                    21
            );
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of();
        }

        public void close() {
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.textWidget.mouseClicked(mouseX, mouseY, button);
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.textWidget.setPosition(x - 2, y);
            this.textWidget.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends Element> children() {
            return List.of();
        }

        public void resize() {
            this.textWidget.setWidth(parent.eighthWidth);
        }

        public void unselect() {
            this.textWidget.setMessage(Text.translatable(identifier.toTranslationKey()));
        }
    }

}
