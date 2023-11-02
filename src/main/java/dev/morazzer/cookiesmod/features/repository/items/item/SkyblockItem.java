package dev.morazzer.cookiesmod.features.repository.items.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.data.dungeons.EssenceType;
import dev.morazzer.cookiesmod.data.enums.GemstoneSlotTypes;
import dev.morazzer.cookiesmod.data.enums.Origin;
import dev.morazzer.cookiesmod.data.enums.Stats;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.CatacombsRequirement;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.CatacombsRequirementDungeonType;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.CatacombsRequirementType;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.Category;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.Crystal;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.DungeonItemConversionCost;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.GemstoneSlot;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.GemstoneSlotRequirement;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.GemstoneSlotRequirementType;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.Prestige;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.PrivateIsland;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.Requirement;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.SalvageType;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.SalvageUpgrade;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.Soulbound;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.SwordType;
import dev.morazzer.cookiesmod.features.repository.items.item.attributes.Tier;
import dev.morazzer.cookiesmod.mixin.ItemStackTooltip;
import dev.morazzer.cookiesmod.utils.general.ItemUtils;
import java.awt.Color;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class representing a SykBlock item with all its attributes.
 */
public class SkyblockItem {

    private static final Logger logger = LoggerFactory.getLogger("cookies-repository-items");

    @Getter
    private final Identifier material;
    @Getter
    private final Category category;
    @Getter
    private final Tier tier;
    @Getter
    private final int npcSellPrice;
    @Getter
    private final Identifier skyblockId;
    @Getter
    private final String realSkyblockId;
    private final Map<Stats, Double> stats;
    private final List<SalvageUpgrade<?>> salvages;
    private final Color color;
    @Getter
    private final Soulbound soulbound;
    @Getter
    private final boolean glowing;
    @Getter
    private final boolean unstackable;
    private final List<Requirement> requirements;
    private final boolean museum;
    private final String generator;
    private final Integer generatorTier;
    private final String furniture;
    @Getter
    private final List<MutableText> description;
    @Getter
    private final int itemDurability;
    private final List<List<SalvageUpgrade<?>>> upgradeCosts;
    @Getter
    private final int gearScore;
    @Getter
    private final boolean dungeonItem;
    private final List<GemstoneSlot> gemstoneSlots;
    private final DungeonItemConversionCost dungeonItemConversionCost;
    private final List<CatacombsRequirement> catacombsRequirement;
    @Getter
    private final boolean hideFromViewrecipeCommand;
    private final SwordType swordType;
    @Getter
    private final double abilityDamageScaling;
    private final Map<String, Integer> enchantments;
    private final Origin origin;
    private final Map<Stats, int[]> tieredStats;
    @Getter
    private final int motesSellPrice;
    private final boolean canHaveAttributes;
    private final Crystal crystal;
    @Getter
    private final boolean salvageableFromRecipe;
    @Getter
    private final boolean riftTransferable;
    private final SalvageUpgrade<Identifier> salvage;
    private final PrivateIsland privateIsland;
    private final boolean canBeReforged;
    private final boolean loseMotesValueOnTransfer;
    private final Prestige prestige;
    @Getter
    private final ItemStack itemStack;
    @Getter
    private final int durability;
    private final String skin;
    @Getter
    private final MutableText name;

    /**
     * Creates an item from a {@linkplain com.google.gson.JsonObject}.
     *
     * @param jsonObject The json object.
     */
    public SkyblockItem(JsonObject jsonObject) {
        this.material = Optional
            .ofNullable(Identifier.of("minecraft", jsonObject.get("material").getAsString().toLowerCase()))
            .orElseThrow();
        this.skyblockId = ItemUtils.skyblockIdToIdentifier(jsonObject.get("id").getAsString()).orElseThrow();
        this.realSkyblockId = jsonObject.get("id").getAsString();

        this.durability = Optional
            .ofNullable(jsonObject.get("durability"))
            .map(jsonElement -> this.parseGenericInteger(jsonElement, "durability"))
            .orElse(-1);
        this.skin = Optional
            .ofNullable(jsonObject.get("skin"))
            .map(jsonElement -> this.parseGenericString(jsonElement, "skin"))
            .orElse(null);
        this.name = Optional
            .ofNullable(jsonObject.get("name"))
            .map(this::parseName)
            .orElse(Text.literal(material.toString()));
        this.category = Optional
            .ofNullable(jsonObject.get("category"))
            .map(jsonElement -> this.parseGenericString(jsonElement, "category"))
            .map(Category::byName)
            .orElse(Category.UNKNOWN);
        this.tier = Optional.ofNullable(jsonObject.get("tier"))
            .map(jsonElement -> this.parseGenericString(jsonElement, "tier"))
            .map(Tier::byName)
            .orElse(Tier.COMMON);
        this.npcSellPrice = Optional
            .ofNullable(jsonObject.get("npc_sell_price"))
            .map(jsonElement -> this.parseGenericInteger(jsonElement, "npc_sell_price"))
            .orElse(-1);

        this.stats = Optional.ofNullable(jsonObject.get("stats")).map(this::parseStats).orElse(null);
        this.salvages = Optional.ofNullable(jsonObject.get("salvages")).map(this::parseSalvages).orElse(null);
        this.color = Optional.ofNullable(jsonObject.get("color")).map(this::parseColor).orElse(null);
        this.soulbound = Optional
            .ofNullable(jsonObject.get("soulbound"))
            .map(jsonElement -> this.parseGenericString(jsonElement, "soulbound"))
            .map(Soulbound::byName)
            .orElse(Soulbound.NONE);
        this.glowing = Optional
            .ofNullable(jsonObject.get("glowing"))
            .map(jsonElement -> this.parseGenericBoolean(jsonElement, "glowing"))
            .orElse(false);
        this.unstackable = Optional
            .ofNullable(jsonObject.get("unstackable"))
            .map(jsonElement -> this.parseGenericBoolean(jsonElement, "unstackable"))
            .orElse(false);
        this.requirements = Optional
            .ofNullable(jsonObject.get("requirements"))
            .map(this::parseRequirements)
            .orElse(null);
        this.museum = Optional
            .ofNullable(jsonObject.get("museum"))
            .map(jsonElement -> this.parseGenericBoolean(jsonElement, "museum"))
            .orElse(false);
        this.generator = Optional
            .ofNullable(jsonObject.get("generator"))
            .map(jsonElement -> this.parseGenericString(jsonElement, "generator"))
            .orElse(null);
        this.generatorTier = Optional
            .ofNullable(jsonObject.get("generator_tier"))
            .map(jsonElement -> this.parseGenericInteger(jsonElement, "generator_tier"))
            .orElse(null);
        this.furniture = Optional
            .ofNullable(jsonObject.get("furniture"))
            .map(jsonElement -> this.parseGenericString(jsonElement, "furniture"))
            .orElse(null);
        this.description = Optional
            .ofNullable(jsonObject.get("description"))
            .map(this::parseDescription)
            .orElse(Collections.emptyList());
        this.itemDurability = Optional
            .ofNullable(jsonObject.get("item_durability"))
            .map(jsonElement -> this.parseGenericInteger(jsonElement, "item_durability"))
            .orElse(-1);
        this.upgradeCosts = Optional
            .ofNullable(jsonObject.get("upgrade_costs"))
            .map(this::parseUpgradeCosts)
            .orElse(null);
        this.gearScore = Optional
            .ofNullable(jsonObject.get("gear_score"))
            .map(jsonElement -> this.parseGenericInteger(jsonElement, "gear_score"))
            .orElse(-1);
        this.dungeonItem = Optional.ofNullable(jsonObject.get("dungeon_item"))
            .map(jsonElement -> this.parseGenericBoolean(jsonElement, "dungeon_item"))
            .orElse(false);
        this.gemstoneSlots = Optional
            .ofNullable(jsonObject.get("gemstone_slots"))
            .map(this::parseGemstoneSlots)
            .orElse(null);
        this.dungeonItemConversionCost = Optional
            .ofNullable(jsonObject.get("dungeon_item_conversion_cost"))
            .map(this::parseDungeonItemConversionCost)
            .orElse(null);
        this.catacombsRequirement = Optional
            .ofNullable(jsonObject.get("catacombs_requirements"))
            .map(this::parseCatacombsRequirements)
            .orElse(null);
        this.hideFromViewrecipeCommand = Optional.ofNullable(jsonObject.get("hide_from_viewrecipe_command"))
            .map(jsonElement -> this.parseGenericBoolean(jsonElement, "hide_from_viewrecipe_command"))
            .orElse(false);
        this.swordType = Optional.ofNullable(jsonObject.get("sword_type"))
            .map(jsonElement -> this.parseGenericString(jsonElement, "sword_type"))
            .map(SwordType::valueOf)
            .orElse(null);
        this.abilityDamageScaling = Optional.ofNullable(jsonObject.get("ability_damage_scaling"))
            .map(jsonElement -> this.parseGenericInteger(jsonElement, "ability_damage_scaling"))
            .orElse(-1);
        this.enchantments = Optional.ofNullable(jsonObject.get("enchantments"))
            .map(this::parseEnchantments)
            .orElse(null);
        this.origin = Optional.ofNullable(jsonObject.get("origin"))
            .map(jsonElement -> this.parseGenericString(jsonElement, "origin"))
            .map(Origin::valueOf)
            .orElse(null);
        this.tieredStats = Optional.ofNullable(jsonObject.get("tiered_stats"))
            .map(this::parseTieredStats)
            .orElse(null);
        this.motesSellPrice = Optional
            .ofNullable(jsonObject.get("motes_sell_price"))
            .map(jsonElement -> this.parseGenericInteger(jsonElement, "motes_sell_price"))
            .orElse(-1);
        this.canHaveAttributes = Optional
            .ofNullable(jsonObject.get("can_have_attributes"))
            .map(jsonElement -> this.parseGenericBoolean(jsonElement, "can_have_attributes"))
            .orElse(false);
        this.crystal = Optional
            .ofNullable(jsonObject.get("crystal"))
            .map(jsonElement -> this.parseGenericString(jsonElement, "crystal"))
            .map(Crystal::valueOf)
            .orElse(null);
        this.salvageableFromRecipe = Optional
            .ofNullable(jsonObject.get("salvageable_from_recipe"))
            .map(jsonElement -> this.parseGenericBoolean(jsonElement, "salvageable_from_recipe"))
            .orElse(false);
        this.riftTransferable = Optional
            .ofNullable(jsonObject.get("rift_transferable"))
            .map(jsonElement -> this.parseGenericBoolean(jsonElement, "rift_transferable"))
            .orElse(false);
        this.salvage = Optional.ofNullable(jsonObject.get("salvage")).map(this::parseSalvage).orElse(null);
        this.privateIsland = Optional
            .ofNullable(jsonObject.get("private_island"))
            .map(jsonElement -> this.parseGenericString(jsonElement, "private_island"))
            .map(PrivateIsland::valueOf)
            .orElse(null);
        this.canBeReforged = Optional
            .ofNullable(jsonObject.get("can_be_reforged"))
            .map(jsonElement -> this.parseGenericBoolean(jsonElement, "can_be_reforged"))
            .orElse(false);
        this.loseMotesValueOnTransfer = Optional
            .ofNullable(jsonObject.get("lose_motes_value_on_transfer"))
            .map(jsonElement -> this.parseGenericBoolean(jsonElement, "lose_motes_value_on_transfer"))
            .orElse(false);
        this.prestige = Optional.ofNullable(jsonObject.get("prestige")).map(this::parsePrestige).orElse(null);
        this.itemStack = this.constructItemStack();
    }

    /**
     * Returns whether the item can be reforged.
     */
    public boolean canBeReforged() {
        return this.canBeReforged;
    }

    /**
     * Returns whether the item can have attributes.
     */
    public boolean canHaveAttributes() {
        return this.canHaveAttributes;
    }

    /**
     * Gets the catacombs requirements.
     */
    public Optional<List<CatacombsRequirement>> getCatacombsRequirement() {
        return Optional.ofNullable(this.catacombsRequirement);
    }

    /**
     * Returns the color of the item.
     */
    public Optional<Color> getColor() {
        return Optional.ofNullable(this.color);
    }

    /**
     * Sets the color for the itemstack.
     *
     * @param itemStack The itemstack.
     */
    private void setColor(ItemStack itemStack) {
        NbtCompound display = new NbtCompound();
        display.putInt("color", this.color.getRGB());
        itemStack.setSubNbt("display", display);
    }

    /**
     * Gets the tooltip to be rendered.
     *
     * @param context The context.
     * @return The tooltip.
     */
    public List<Text> getTooltip(TooltipContext context) {
        List<MutableText> texts = new ArrayList<>(this.description);
        if (context == TooltipContext.ADVANCED) {
            texts.add(Text.empty());
            texts.add(Text.literal(this.material.toString()).formatted(Formatting.DARK_GRAY));
            texts.add(Text.literal(this.skyblockId.toString()).formatted(Formatting.DARK_GRAY));
            texts.add(Text.literal(this.realSkyblockId).formatted(Formatting.DARK_GRAY));
        }
        return texts.stream().map(Text.class::cast).collect(Collectors.toList());
    }

    /**
     * Gets the crystal type of the item.
     *
     * @return The crystal type.
     */
    public Optional<Crystal> getCrystal() {
        return Optional.ofNullable(this.crystal);
    }

    /**
     * Returns the dungeon item conversion cost of the item.
     */
    public Optional<DungeonItemConversionCost> getDungeonItemConversionCost() {
        return Optional.ofNullable(this.dungeonItemConversionCost);
    }

    /**
     * Returns the enchantments of the item.
     */
    public Optional<Map<String, Integer>> getEnchantments() {
        return Optional.ofNullable(this.enchantments);
    }

    /**
     * Returns the furniture string of the item.
     */
    public Optional<String> getFurniture() {
        return Optional.ofNullable(this.furniture);
    }

    /**
     * Returns the gemstone slots of the item.
     */
    public Optional<List<GemstoneSlot>> getGemstoneSlots() {
        return Optional.ofNullable(this.gemstoneSlots);
    }

    /**
     * Returns the generator type.
     */
    public Optional<String> getGenerator() {
        return Optional.ofNullable(this.generator);
    }

    /**
     * Returns the tier of the generator.
     */
    public Optional<Integer> getGeneratorTier() {
        return Optional.ofNullable(this.generatorTier);
    }

    /**
     * Returns whether the item loses its mote value if transferred.
     */
    public boolean doesLoseMotesValueOnTransfer() {
        return this.loseMotesValueOnTransfer;
    }

    /**
     * Returns whether the item can be in the museum.
     */
    public boolean canBeInMuseum() {
        return this.museum;
    }

    /**
     * Returns the origin of the item.
     */
    public Optional<Origin> getOrigin() {
        return Optional.ofNullable(this.origin);
    }

    /**
     * Returns the prestige item for the item.
     */
    public Optional<Prestige> getPrestige() {
        return Optional.ofNullable(this.prestige);
    }

    /**
     * Returns the private island type the item spawns.
     */
    public Optional<PrivateIsland> getPrivateIsland() {
        return Optional.ofNullable(this.privateIsland);
    }

    /**
     * Returns the requirements of the item.
     */
    public Optional<List<Requirement>> getRequirements() {
        return Optional.ofNullable(this.requirements);
    }

    /**
     * Returns the salvage of the item.
     */
    public Optional<SalvageUpgrade<Identifier>> getSalvage() {
        return Optional.ofNullable(this.salvage);
    }

    /**
     * Returns the salvage results of the item.
     */
    public Optional<List<SalvageUpgrade<?>>> getSalvages() {
        return Optional.ofNullable(this.salvages);
    }

    /**
     * Returns the skin of the item.
     */
    public Optional<String> getSkin() {
        return Optional.ofNullable(this.skin);
    }

    /**
     * Sets the skin for the itemstack.
     *
     * @param itemStack The itemstack.
     */
    private void setSkin(ItemStack itemStack) {
        NbtCompound skullOwner = new NbtCompound();
        skullOwner.putUuid("Id", UUID.randomUUID());
        NbtCompound properties = new NbtCompound();
        NbtList textures = new NbtList();
        NbtCompound texture = new NbtCompound();
        texture.putString("Value", this.skin);
        textures.add(texture);
        properties.put("textures", textures);
        skullOwner.put("Properties", properties);
        itemStack.setSubNbt("SkullOwner", skullOwner);
    }

    /**
     * Returns the stats of the item.
     */
    public Optional<Map<Stats, Double>> getStats() {
        return Optional.ofNullable(this.stats);
    }

    /**
     * Returns the sword type.
     */
    public Optional<SwordType> getSwordType() {
        return Optional.ofNullable(this.swordType);
    }

    /**
     * Returns the tiered stats for the item.
     */
    public Optional<Map<Stats, int[]>> getTieredStats() {
        return Optional.ofNullable(this.tieredStats);
    }

    /**
     * Returns the upgrade costs.
     */
    public Optional<List<List<SalvageUpgrade<?>>>> getUpgradeCosts() {
        return Optional.ofNullable(this.upgradeCosts);
    }

    /**
     * Gets the item name as an alphanumerical string. Except also including § (\<span>u00A7</span>).
     *
     * @return The item name.
     */
    public String getItemNameAlphanumerical() {
        return Normalizer
            .normalize(this.getName().getString(), Normalizer.Form.NFD)
            .replaceAll("[^A-Za-z0-9 _\\-]|§.", "")
            .trim();
    }

    /**
     * Gets the tooltip as one string.
     *
     * @param basic The tooltip context to use.
     * @return The tooltip.
     */
    public String getTooltipAsString(TooltipContext.Default basic) {
        return getTooltip(basic)
            .stream()
            .filter(Objects::nonNull)
            .map(Text::getString)
            .collect(Collectors.joining("\n"));
    }

    /**
     * Parses an integer from a {@linkplain com.google.gson.JsonElement} or print an error.
     *
     * @param jsonElement The json element.
     * @param string      The key it's parsed for.
     * @return The integer or null.
     */
    private Integer parseGenericInteger(JsonElement jsonElement, String string) {
        if (JsonHelper.isNumber(jsonElement)) {
            return jsonElement.getAsInt();
        }

        logger.warn(
            "Expected int got {} for item {} at key '{}'",
            JsonHelper.getType(jsonElement),
            this.skyblockId,
            string
        );
        return null;
    }

    /**
     * Parses a string from a {@linkplain com.google.gson.JsonElement} or print an error.
     *
     * @param jsonElement The json element.
     * @param string      The key it's parsed for.
     * @return The string or null.
     */
    private String parseGenericString(JsonElement jsonElement, String string) {
        if (JsonHelper.isString(jsonElement)) {
            return jsonElement.getAsString();
        }

        logger.warn(
            "Expected string got {} for item {} at key '{}'",
            JsonHelper.getType(jsonElement),
            this.skyblockId,
            string
        );
        return null;
    }

    /**
     * Parses a boolean from a {@linkplain com.google.gson.JsonElement} or print an error.
     *
     * @param jsonElement The json element.
     * @param string      The key it's parsed for.
     * @return The boolean or null.
     */
    private Boolean parseGenericBoolean(JsonElement jsonElement, String string) {
        if (JsonHelper.isBoolean(jsonElement)) {
            return jsonElement.getAsBoolean();
        }

        logger.warn(
            "Expected boolean got {} for item {} at key '{}'",
            JsonHelper.getType(jsonElement),
            this.skyblockId,
            string
        );
        return null;
    }

    /**
     * Parses the name from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The name or null.
     */
    private MutableText parseName(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            return Text.Serializer.fromJson(jsonElement);
        }
        if (JsonHelper.isString(jsonElement)) {
            return Text.literal(jsonElement.getAsString());
        }

        logger.warn(
            "Expected chat component or string got {} for item {} at key 'name'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parses stats from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The stats or null.
     */
    private Map<Stats, Double> parseStats(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            HashMap<Stats, Double> map = new HashMap<>();
            JsonObject stats = jsonElement.getAsJsonObject();
            for (String statKey : stats.keySet()) {
                Stats stat = Stats.valueOfIgnore(statKey);
                double value = stats.get(statKey).getAsDouble();
                map.put(stat, value);
            }
            return map;
        }

        logger.warn(
            "Expected object got {} for item {} at key 'stats'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parses the salvages from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The salvages or null.
     */
    private List<SalvageUpgrade<?>> parseSalvages(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            List<SalvageUpgrade<?>> list = new ArrayList<>();
            createSalvageUpdateList(jsonElement.getAsJsonArray(), list);
            return list;
        }

        logger.warn(
            "Expected array got {} for item {} at key 'salvages'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parses a color from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The color or null.
     */
    private Color parseColor(JsonElement jsonElement) {
        if (JsonHelper.isString(jsonElement)) {
            String[] split = jsonElement.getAsString().split(",", 3);
            int r = Integer.parseInt(split[0]);
            int g = Integer.parseInt(split[1]);
            int b = Integer.parseInt(split[2]);
            return new Color(r, g, b);
        }

        logger.warn(
            "Expected string got {} for item {} at key 'color'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parses the requirements from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The requirements or null.
     */
    private List<Requirement> parseRequirements(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            List<Requirement> requirements = new ArrayList<>();
            for (JsonElement requirement : jsonElement.getAsJsonArray()) {
                if (!requirement.isJsonObject()) {
                    continue;
                }
                requirements.add(Requirement.parseRequirement(requirement.getAsJsonObject()));
            }
            return requirements;
        }

        logger.warn(
            "Expected array got {} for item {} at key 'requirements'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parses the description from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The description or null.
     */
    private List<MutableText> parseDescription(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            List<MutableText> description = new ArrayList<>();
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                description.add(Text.Serializer.fromJson(element));
            }
            return description.stream().filter(Objects::nonNull).toList();
        }

        logger.warn(
            "Expected array got {} for item {} at key 'description'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parses the upgrade costs from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The upgrade costs or null.
     */
    private List<List<SalvageUpgrade<?>>> parseUpgradeCosts(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            List<List<SalvageUpgrade<?>>> lists = new ArrayList<>();
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                List<SalvageUpgrade<?>> list = new ArrayList<>();
                createSalvageUpdateList(element.getAsJsonArray(), list);
                lists.add(list);
            }
            return lists;
        }

        logger.warn(
            "Expected array got {} for item {} at key 'upgrade_costs'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parses the gemstone slots from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The gemstone slots or null.
     */
    private List<GemstoneSlot> parseGemstoneSlots(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            List<GemstoneSlot> gemstoneSlots = new ArrayList<>();
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                JsonObject jsonObject = element.getAsJsonObject();
                GemstoneSlotTypes slotsType = GemstoneSlotTypes.valueOf(jsonObject.get("slot_type").getAsString());
                ArrayList<GemstoneSlotRequirement<?>> slotRequirements = new ArrayList<>();
                if (jsonObject.has("costs")) {
                    for (JsonElement costElement : jsonObject.getAsJsonArray("costs")) {
                        JsonObject cost = costElement.getAsJsonObject();

                        String type = cost.get("type").getAsString();

                        slotRequirements.add(
                            switch (type) {
                                case "COINS" -> new GemstoneSlotRequirement<>(
                                    GemstoneSlotRequirementType.COINS,
                                    null,
                                    cost.get("coins").getAsInt()
                                );
                                case "ITEM" -> new GemstoneSlotRequirement<>(
                                    GemstoneSlotRequirementType.ITEM,
                                    ItemUtils.skyblockIdToIdentifier(cost.get("item_id").getAsString()).orElseThrow(),
                                    cost.get("amount").getAsInt()
                                );
                                default -> null;
                            }
                        );
                    }
                }

                gemstoneSlots.add(new GemstoneSlot(slotsType, slotRequirements));
            }
            return gemstoneSlots;
        }

        logger.warn(
            "Expected array got {} for item {} at key 'gemstone_slots'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parses the dungeon item conversion cost from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The dungeon item conversion cost or null.
     */
    private DungeonItemConversionCost parseDungeonItemConversionCost(JsonElement jsonElement) {
        if (jsonElement instanceof JsonObject jsonObject) {
            return new DungeonItemConversionCost(
                EssenceType.valueOf(jsonObject.get("essence_type").getAsString()),
                jsonObject.get("amount").getAsInt()
            );
        }

        logger.warn(
            "Expected object got {} for item {} at key 'dungeon_item_conversion_cost'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parses the catacombs requirements from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The catacombs requirements or null.
     */
    private List<CatacombsRequirement> parseCatacombsRequirements(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            List<CatacombsRequirement> list = new ArrayList<>();
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                JsonObject object = element.getAsJsonObject();
                CatacombsRequirementType type = CatacombsRequirementType.valueOf(object.get("type").getAsString());
                CatacombsRequirementDungeonType dungeonType = CatacombsRequirementDungeonType.valueOf(object
                    .get("dungeon_type")
                    .getAsString());
                int level = object.get("level").getAsInt();
                list.add(new CatacombsRequirement(type, dungeonType, level));
            }
            return list;
        }

        logger.warn(
            "Expected array got {} for item {} at key 'catacombs_requirements'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parses the enchantments from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The enchantments or null.
     */
    private Map<String, Integer> parseEnchantments(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Map<String, Integer> enchantments = new HashMap<>();

            for (String key : jsonObject.keySet()) {
                enchantments.put(key, jsonObject.get(key).getAsInt());
            }

            return enchantments;
        }

        logger.warn(
            "Expected object got {} for item {} at key 'enchantments'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parses the tiered stats from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The tiered stats or null.
     */
    private Map<Stats, int[]> parseTieredStats(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Map<Stats, int[]> stats = new HashMap<>();

            for (String key : jsonObject.keySet()) {
                Stats stat = Stats.valueOfIgnore(key);
                stats.put(
                    stat,
                    jsonObject
                        .get(key)
                        .getAsJsonArray()
                        .asList()
                        .stream()
                        .map(JsonElement::getAsInt)
                        .mapToInt(Integer::intValue)
                        .toArray()
                );
            }
            return stats;
        }

        logger.warn(
            "Expected object got {} for item {} at key 'tiered_stats'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parses the salvage from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The salvage or null.
     */
    private SalvageUpgrade<Identifier> parseSalvage(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return new SalvageUpgrade<>(
                SalvageType.ITEM,
                ItemUtils.skyblockIdToIdentifier(jsonObject.get("item_id").getAsString()).orElseThrow(),
                jsonObject.get("amount").getAsInt()
            );
        }

        logger.warn(
            "Expected object got {} for item {} at key 'salvage'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Parser the prestige from a {@linkplain com.google.gson.JsonElement}.
     *
     * @param jsonElement The json element.
     * @return The prestige or null.
     */
    private Prestige parsePrestige(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Identifier identifier = ItemUtils
                .skyblockIdToIdentifier(jsonObject.get("item_id").getAsString())
                .orElseThrow();
            JsonArray costs = jsonObject.get("costs").getAsJsonArray();
            List<SalvageUpgrade<?>> list = new ArrayList<>();
            createSalvageUpdateList(costs, list);
            return new Prestige(identifier, list);
        }

        logger.warn(
            "Expected object got {} for item {} at key 'prestige'",
            JsonHelper.getType(jsonElement),
            this.skyblockId
        );
        return null;
    }

    /**
     * Populates a list of {@linkplain dev.morazzer.cookiesmod.features.repository.items.item.attributes.SalvageUpgrade}
     * for a json array.
     *
     * @param jsonArray The json array.
     * @param list      The list to populate.
     */
    private void createSalvageUpdateList(JsonArray jsonArray, List<SalvageUpgrade<?>> list) {
        for (JsonElement element : jsonArray) {
            if (!(element instanceof JsonObject salvageResult)) {
                continue;
            }

            String type = salvageResult.get("type").getAsString();

            int amount = salvageResult.get("amount").getAsInt();

            list.add(
                switch (type) {
                    case "ESSENCE" -> new SalvageUpgrade<>(
                        SalvageType.ESSENCE,
                        EssenceType.valueOf(salvageResult.get("essence_type").getAsString()),
                        amount
                    );
                    case "ITEM" -> new SalvageUpgrade<>(
                        SalvageType.ITEM,
                        ItemUtils.skyblockIdToIdentifier(salvageResult.get("item_id").getAsString()).orElseThrow(),
                        amount
                    );
                    default -> null;
                }
            );
        }
    }

    /**
     * Constructs the itemstack for this item.
     *
     * @return The itemstack.
     */
    private ItemStack constructItemStack() {
        Item item = Registries.ITEM
            .getOrEmpty(this.material)
            .orElse(Registries.ITEM.get(Identifier.tryParse("minecraft:barrier")));
        ItemStack itemStack = new ItemStack(item);
        itemStack.setCustomName(this.name.setStyle(Style.EMPTY.withItalic(false).withColor(this.tier.getFormatting())));

        ((ItemStackTooltip) (Object) itemStack).cookies$setSkyblockItem(this);

        if (this.skin != null) {
            this.setSkin(itemStack);
        }

        if (this.color != null) {
            this.setColor(itemStack);
        }

        if (this.glowing) {
            this.setGlowing(itemStack);
        }

        return itemStack;
    }

    /**
     * Sets the itemstack glowing (have glint).
     *
     * @param itemStack The itemstack.
     */
    private void setGlowing(ItemStack itemStack) {
        NbtList nbtElements = new NbtList();
        nbtElements.add(new NbtCompound());
        itemStack.setSubNbt("Enchantments", nbtElements);
    }

}
