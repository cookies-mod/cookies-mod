package dev.morazzer.cookiesmod.features.mining;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.morazzer.cookiesmod.features.hud.HudElement;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItemManager;
import dev.morazzer.cookiesmod.features.repository.items.item.SkyblockItem;
import dev.morazzer.cookiesmod.features.repository.items.recipe.Ingredient;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import dev.morazzer.cookiesmod.utils.NpcUtils;
import dev.morazzer.cookiesmod.utils.render.Position;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Renders the current fetchur item as a hud element.
 */
public class FetchurHud extends HudElement {

    final List<Ingredient> items = new LinkedList<>();

    long lastUpdate = System.currentTimeMillis();
    Ingredient currentItem = new Ingredient("skyblock:items/stained_glass_4:20");
    private PlayerEntity entity;

    public FetchurHud() {
        super(new Position(0, 0));
        RepositoryManager.addReloadCallback(this::updateItems);
        if (RepositoryManager.isFinishedLoading()) {
            this.updateItems();
        }
    }

    @Override
    public int getWidth() {
        return 40;
    }

    @Override
    public int getHeight() {
        return 40;
    }

    @Override
    public String getIdentifierPath() {
        return "mining/fetchur";
    }

    @Override
    public boolean shouldRender() {
        return true;
    }

    @Override
    protected void renderOverlay(DrawContext drawContext, float delta) {
        if (this.entity == null) {
            this.entity = NpcUtils.createRenderableNpc(
                    "Fetchur",
                    "ewogICJ0aW1lc3RhbXAiIDogMTYwODMxMzQwOTk1NSwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTdiMzU1MmM1YWYyYWIyZGM0ZTFmODRkNzhjYTRjNGQ2NzZkZWMwNjgxNTcyMjVhM2MyNjc0ZTU1NzRkMjM0OCIKICAgIH0KICB9Cn0="
            );
        }
        if (this.lastUpdate < System.currentTimeMillis()) {
            this.update();
            this.lastUpdate = System.currentTimeMillis() + 10000;
        }
        this.entity.setCustomName(Text.literal("test"));
        this.entity.setCustomNameVisible(true);

        SkyblockItem item = RepositoryItemManager.getItem(this.currentItem);
        if (item == null) {
            drawContext.drawText(MinecraftClient.getInstance().textRenderer, Text.literal("Item not found!").formatted(
                    Formatting.RED), 0, 0, -1, true);
            return;
        }

        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadows(false);

        if (entityRenderDispatcher.getRenderer(entity) == null) return;
        entity.getInventory().setStack(entity.getInventory().selectedSlot, item.getItemStack());
        drawContext.getMatrices().pop();
        InventoryScreen.drawEntity(
                drawContext,
                getX(),
                getY(),
                getX() + 40,
                getY() + 40,
                15,
                0.0625f,
                getX() + 20,
                getY() + 20,
                entity
        );
        RenderUtils.renderTextWithMaxWidth(
                drawContext,
                Text.literal(String.valueOf(this.currentItem.getAmount())).append(" ").append(item.getName()),
                40,
                getX(),
                getY() + 40,
                -1,
                false
        );
        drawContext.getMatrices().push();
    }

    /**
     * Update the current item list/load it from the repository.
     */
    private void updateItems() {
        this.items.clear();
        Optional<byte[]> resource = RepositoryManager.getResource("constants/fetchur_items.json");
        if (resource.isEmpty()) {
            return;
        }
        String path = new String(resource.get());
        JsonArray jsonElements = JsonUtils.gsonClean.fromJson(path, JsonArray.class);
        for (JsonElement jsonElement : jsonElements) {
            if (!jsonElement.isJsonPrimitive()) {
                continue;
            }
            if (!jsonElement.getAsJsonPrimitive().isString()) {
                continue;
            }

            String stringIdentifier = jsonElement.getAsString();
            this.items.add(new Ingredient(stringIdentifier));
        }
    }

    /**
     * Updates the current item.
     */
    private void update() {
        int dayOfMonth = ZonedDateTime.now(ZoneId.of("Canada/Eastern")).getDayOfMonth();
        int currentItem = (dayOfMonth - 1) % this.items.size();

        this.currentItem = this.items.get(currentItem);
    }

}
