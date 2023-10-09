package dev.morazzer.cookiesmod.features.mining;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.events.api.InventoryContentUpdateEvent;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.NumberFormat;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@LoadModule("mining/hotm_helper")
public class HotmHelper implements Module {
    Map<String, List<Integer>> perks = new LinkedHashMap<>();
    List<String> gemstonePowder = new LinkedList<>();

    @Override
    public void load() {
        RepositoryManager.addReloadCallback(this::update);
        if (RepositoryManager.isFinishedLoading()) {
            update();
        }

        AtomicInteger lastSyncId = new AtomicInteger(-1);

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof HandledScreen<?> handledScreen)) return;
            if (!ConfigManager.getConfig().miningCategory.heartOfTheMountain.showTotalPowder.getValue()
                    && !ConfigManager.getConfig().miningCategory.heartOfTheMountain.showNextTenPowder.getValue()
                    && !ConfigManager.getConfig().miningCategory.heartOfTheMountain.showLevelInCount.getValue()) {
                return;
            }

            ScreenHandler screenHandler = handledScreen.getScreenHandler();
            if (lastSyncId.get() == screenHandler.syncId) {
                return;
            }
            if (!handledScreen.getTitle().getString().equals("Heart of the Mountain")) return;
            lastSyncId.set(screenHandler.syncId);
            InventoryContentUpdateEvent.register(screenHandler, ExceptionHandler.wrap(this::updateSlot));

        });
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (!stack.hasNbt()) return;
            if (!stack.getOrCreateNbt().contains("cookies")) return;
            if (!stack.getOrCreateSubNbt("cookies").contains("type")) return;
            if (!stack.getOrCreateSubNbt("cookies").getString("type").equals("hotm")) return;
            NbtCompound cookies = stack.getOrCreateSubNbt("cookies");
            boolean gemstone = cookies.getBoolean("gemstone");
            String powder = gemstone ? " Gemstone " : " Mithril ";
            Formatting formatting = gemstone ? Formatting.LIGHT_PURPLE : Formatting.DARK_GREEN;
            int next10 = cookies.getInt("powder_for_next_10");
            int total = cookies.getInt("powder_till_max");
            for (int i = 0; i < lines.size(); i++) {
                Text line = lines.get(i);
                String literal = line.getString();
                if (!literal.equals("Cost")) continue;

                i += 2;
                if (ConfigManager.getConfig().miningCategory.heartOfTheMountain.showNextTenPowder.getValue()) {
                    lines.add(i++, Text.empty());
                    lines.add(i++, Text.literal("Cost for next 10").formatted(Formatting.GRAY));
                    lines.add(
                            i++,
                            Text.literal(NumberFormat.toFormattedString(next10)).append(powder).append("Powder")
                                    .formatted(formatting)
                    );
                }
                if (ConfigManager.getConfig().miningCategory.heartOfTheMountain.showTotalPowder.getValue()) {
                    lines.add(i++, Text.empty());
                    lines.add(i++, Text.literal("Total cost").formatted(Formatting.GRAY));
                    lines.add(
                            i,
                            Text.literal(NumberFormat.toFormattedString(total)).append(powder).append("Powder")
                                    .formatted(formatting)
                    );
                }
                return;
            }
        });
    }

    private void updateSlot(int slot, ItemStack itemStack) {
        if (slot > 53) return;
        if (!itemStack.hasCustomName()) return;
        String name = itemStack.getName().getString();
        String searchName = name.trim().replaceAll(" +", "_").toLowerCase();
        List<Integer> integers = this.perks.getOrDefault(searchName, Collections.emptyList());
        if (integers.isEmpty()) {
            return;
        }
        List<Text> lore = itemStack.getTooltip(MinecraftClient.getInstance().player, TooltipContext.BASIC);
        if (lore.size() == 1) return;
        String levelLine = lore.get(1).getString();
        int level = Integer.parseInt(levelLine.replaceAll("[^\\d/]", "").split("/")[0]);

        NbtCompound cookies = itemStack.getOrCreateSubNbt("cookies");
        cookies.putString("type", "hotm");
        cookies.putInt("level", level);
        if (ConfigManager.getConfig().miningCategory.heartOfTheMountain.showLevelInCount.getValue()) {
            itemStack.setCount(level);
        }
        int amount = 0;
        for (int i = level - 1; i < Math.min(level + 9, integers.size()); i++) {
            amount += integers.get(i);
        }
        int total = 0;
        for (Integer i : integers.subList(Math.min(level - 1, integers.size()), integers.size())) {
            total += i;
        }
        cookies.putInt("powder_for_next_10", amount);
        cookies.putInt("powder_till_max", total);
        cookies.putBoolean("gemstone", gemstonePowder.contains(searchName));
    }

    public void update() {
        Optional<byte[]> resource = RepositoryManager.getResource("constants/hotm_perks.json");
        if (resource.isEmpty()) return;
        String content = new String(resource.get(), StandardCharsets.UTF_8);
        JsonObject jsonObject = GsonUtils.gsonClean.fromJson(content, JsonObject.class);
        this.gemstonePowder.clear();
        this.perks.clear();
        for (String key : jsonObject.keySet()) {
            if (key.equals("gemstone")) {
                JsonArray gemstone = jsonObject.getAsJsonArray(key);
                for (JsonElement jsonElement : gemstone) {
                    this.gemstonePowder.add(jsonElement.getAsString());
                }
                continue;
            }

            JsonElement jsonElement = jsonObject.get(key);
            if (!jsonElement.isJsonArray()) return;
            this.perks.put(key, GsonUtils.gsonClean.fromJson(jsonElement, new TypeToken<List<Integer>>() {}.getType()));
        }
    }

    @Override
    public String getIdentifierPath() {
        return "mining/hotm_helper";
    }
}
