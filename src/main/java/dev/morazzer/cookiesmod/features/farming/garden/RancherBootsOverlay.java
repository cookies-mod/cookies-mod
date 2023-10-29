package dev.morazzer.cookiesmod.features.farming.garden;

import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.features.farming.garden.speed.Speeds;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItemManager;
import dev.morazzer.cookiesmod.features.repository.items.item.SkyblockItem;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.DevUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Feature to render a list of speeds in the rancher boots overlay.
 */
@LoadModule("garden/optimal_speed")
public class RancherBootsOverlay implements Module {

    private static final Identifier SHOW_BUTTON_BORDERS = DevUtils.createIdentifier("garden/rancher_boots/show_borders");
    private static final Identifier SKIP_RANCHER_BOOTS_CHECK = DevUtils.createIdentifier(
            "garden/rancher_boots/disable_boots_check");

    private final Set<Identifier> crops = Set.of(
            new Identifier("skyblock:items/wheat"),
            new Identifier("skyblock:items/carrot_item"),
            new Identifier("skyblock:items/potato_item"),
            new Identifier("skyblock:items/nether_stalk"),
            new Identifier("skyblock:items/pumpkin"),
            new Identifier("skyblock:items/melon"),
            new Identifier("skyblock:items/ink_sack_3"),
            new Identifier("skyblock:items/sugar_cane"),
            new Identifier("skyblock:items/cactus"),
            new Identifier("skyblock:items/huge_mushroom_2")
    );

    private List<SpeedEntry> entries;

    @Override
    public void load() {
        ScreenEvents.BEFORE_INIT.register(this::openScreen);
    }

    @Override
    public String getIdentifierPath() {
        return "garden/optimal_speed";
    }

    /**
     * Check if the current sign is a valid rancher boots sign.
     *
     * @param screen The sign.
     * @return If the sign is a rancher boots sign.
     */
    private boolean isRancherBootsScreen(SignEditScreen screen) {
        return screen.messages[1].trim().equals("^^^^^^")
                && screen.messages[2].trim().equals("Set your")
                && screen.messages[3].trim().equals("speed cap!");
    }

    /**
     * Check if the newly opened screen is a sign and if so initialize the rendering.
     *
     * @param minecraftClient The minecraft client.
     * @param screen          The screen.
     * @param width           The width of the window.
     * @param height          The height of the window.
     */
    private void openScreen(MinecraftClient minecraftClient, Screen screen, int width, int height) {
        if (!(screen instanceof SignEditScreen)) {
            return;
        }
        if (!SkyblockUtils.isCurrentlyInSkyblock()) return;
        if (!ConfigManager.getConfig().gardenCategory.speed.showSpeeds.getValue()) {
            return;
        }
        if (!DevUtils.isEnabled(SKIP_RANCHER_BOOTS_CHECK) && !isRancherBootsScreen((SignEditScreen) screen)) {
            return;
        }

        Speeds speeds = Speeds.merge(
                ConfigManager.getConfig().gardenCategory.speed.speedPresets.getValue().getSpeeds(),
                ConfigManager.getConfig().gardenCategory.speed.speeds
        );

        List<AbstractMap.SimpleEntry<Integer, Identifier>> list = crops.stream()
                .map(identifier -> new AbstractMap.SimpleEntry<>(speeds.getValue(identifier), identifier)).toList();

        this.entries = new ArrayList<>();

        if (ConfigManager.getConfig().gardenCategory.speed.mergeEqualSpeeds.getValue()) {
            HashMap<Integer, List<Identifier>> tempMap = new HashMap<>();
            list.forEach(integerIdentifierSimpleEntry -> tempMap.computeIfAbsent(
                    integerIdentifierSimpleEntry.getKey(),
                    integer -> new ArrayList<>()
            ).add(integerIdentifierSimpleEntry.getValue()));
            tempMap.forEach((integer, identifiers) -> {
                Text text = createText(
                        identifiers.stream().map(RepositoryItemManager::getItem).map(SkyblockItem::getName)
                                .map(MutableText::getString).collect(Collectors.joining(", ")),
                        integer
                );
                this.entries.add(new SpeedEntry(identifiers, text, integer));
            });
        } else {
            for (AbstractMap.SimpleEntry<Integer, Identifier> integerIdentifierSimpleEntry : list) {
                Text text = createText(RepositoryItemManager.getItem(integerIdentifierSimpleEntry.getValue()).getName()
                        .getString(), integerIdentifierSimpleEntry.getKey());
                this.entries.add(new SpeedEntry(
                        List.of(integerIdentifierSimpleEntry.getValue()),
                        text,
                        integerIdentifierSimpleEntry.getKey()
                ));
            }
        }

        ScreenEvents.afterRender(screen).register(this::renderOverlay);
        ScreenMouseEvents.afterMouseClick(screen).register(this::mouseClick);
    }

    /**
     * Create a text for the crop and speed.
     *
     * @param crop  The crop.
     * @param speed The speed.
     * @return The text.
     */
    private Text createText(String crop, int speed) {
        MutableText text = Text.empty();

        if (ConfigManager.getConfig().gardenCategory.speed.showNames.getValue()) {
            text.append(crop);
        }
        text.append(" - ").append(String.valueOf(speed));

        return text;
    }

    /**
     * Render the speed list onto the sign.
     *
     * @param screen    The screen.
     * @param context   The current draw context.
     * @param mouseX    The current x position of the mouse.
     * @param mouseY    The current y position of the mouse.
     * @param tickDelta The difference in time between the last tick and now.
     */
    private void renderOverlay(Screen screen, DrawContext context, int mouseX, int mouseY, float tickDelta) {
        if (!SkyblockUtils.isCurrentlyInSkyblock()) return;
        if (!ConfigManager.getConfig().gardenCategory.speed.showSpeeds.getValue()) {
            return;
        }

        context.getMatrices().push();

        int difference = screen.height / 4 + 55;
        int negativeOffsetY = 0;
        if (difference < 160) {
            negativeOffsetY = 160 - difference;
        }
        context.getMatrices().translate(screen.width / 2.0f + 55, 90.0f - 23 - negativeOffsetY, 50.0f);

        int translatedMouseX = mouseX - (screen.width / 2 + 55);
        int translatedMouseY = mouseY - (90 - 23 - negativeOffsetY);

        for (int i = 0; i < this.entries.size(); i++) {
            SpeedEntry entry = this.entries.get(i);
            int items = 0;
            for (Identifier identifier : entry.identifiers) {
                context.drawItem(RepositoryItemManager.getItem(identifier).getItemStack(), items * 16, i * 16);
                items++;
            }

            Formatting formatting;
            int topLeftX = 0;
            int topLeftY = i * 16;
            int bottomRightX = items * 16 + 4 + screen.textRenderer.getWidth(entry.text);
            int bottomRightY = i * 16 + 15;
            if (topLeftX <= translatedMouseX && bottomRightX >= translatedMouseX
                    && topLeftY <= translatedMouseY && bottomRightY >= translatedMouseY) {
                formatting = Formatting.UNDERLINE;
            } else {
                formatting = Formatting.WHITE;
            }

            context.drawText(
                    screen.textRenderer,
                    entry.text.copy().formatted(formatting),
                    items * 16 + 4,
                    i * 16 + 4,
                    ~0,
                    true
            );

            if (DevUtils.isEnabled(SHOW_BUTTON_BORDERS)) {
                int endX = items * 16 + 4 + screen.textRenderer.getWidth(entry.text);
                int color = i % 2 == 0 ? ~0 : (0xFF << 24) ^ 0xFF00FF;
                context.drawHorizontalLine(0, endX, i * 16, color);
                context.drawVerticalLine(0, i * 16, i * 16 + 15, color);
                context.drawVerticalLine(endX, i * 16, i * 16 + 15, color);
                context.drawHorizontalLine(0, endX, i * 16 + 15, color);
            }
        }

        context.getMatrices().pop();
    }

    /**
     * Check if the mouse was clicked on a text and if so, then change the speed to it.
     * @param screen The screen.
     * @param mouseX The current x position of the mouse.
     * @param mouseY The current y position of the mouse.
     * @param button The button that was clicked.
     */
    private void mouseClick(Screen screen, double mouseX, double mouseY, int button) {
        if (button != 0) {
            return;
        }

        SignEditScreen signEditScreen = (SignEditScreen) screen;
        int difference = screen.height / 4 + 55;
        int negativeOffsetY = 0;
        if (difference < 160) {
            negativeOffsetY = 160 - difference;
        }
        int translatedMouseX = (int) (mouseX - (screen.width / 2 + 55));
        int translatedMouseY = (int) (mouseY - (90 - 23 - negativeOffsetY));

        int index = translatedMouseY / 16;

        if (index < 0 || this.entries.size() - 1 < index) {
            return;
        }
        SpeedEntry speedEntry = this.entries.get(index);

        int widthX = screen.textRenderer.getWidth(speedEntry.text) + speedEntry.identifiers.size() * 16 + 4;

        if (translatedMouseX <= 0 || widthX <= translatedMouseX) {
            return;
        }

        signEditScreen.messages[0] = String.valueOf(speedEntry.speed);
    }

    private record SpeedEntry(
            List<Identifier> identifiers,
            Text text,
            int speed
    ) {
    }

}
