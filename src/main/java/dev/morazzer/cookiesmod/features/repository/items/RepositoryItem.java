package dev.morazzer.cookiesmod.features.repository.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.data.DungeonType;
import dev.morazzer.cookiesmod.data.EssenceType;
import dev.morazzer.cookiesmod.data.Factions;
import dev.morazzer.cookiesmod.data.GemstoneSlotTypes;
import dev.morazzer.cookiesmod.data.Origin;
import dev.morazzer.cookiesmod.data.Skill;
import dev.morazzer.cookiesmod.data.SlayerBoss;
import dev.morazzer.cookiesmod.data.Stats;
import dev.morazzer.cookiesmod.data.TrophyFishingReward;
import dev.morazzer.cookiesmod.mixin.ItemStackTooltip;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.RomanNumerals;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("OverlyComplexClass")
@Getter
public class RepositoryItem {
	private final static Identifier NAMESPACE = new Identifier("skyblock", "item/");
	private final Identifier material;
	private final int durability;
	private final Optional<String> skin;
	private final MutableText name;
	private final Category category;
	private final Tier tier;
	private final int npcSellPrice;
	private final Identifier skyblockId;
	private final Optional<HashMap<Stats, Double>> stats;
	private final Optional<List<SalvageUpgrade<?>>> salvages;
	private final Optional<Color> color;
	private final Soulbound soulbound;
	private final boolean glowing;
	private final boolean unstackable;
	private final Optional<List<Requirement>> requirements;
	private final boolean museum;
	private final Optional<String> generator;
	private final Optional<Integer> generatorTier;
	private final Optional<String> furniture;
	private final List<MutableText> description;
	private final int itemDurability;
	private final Optional<List<List<SalvageUpgrade<?>>>> upgradeCosts;
	private final int gearScore;
	private final boolean dungeonItem;
	private final Optional<List<GemstoneSlot>> gemstoneSlots;
	private final Optional<DungeonItemConversionCost> dungeonItemConversionCost;
	private final Optional<List<CatacombsRequirement>> catacombsRequirement;
	private final boolean hideFromViewrecipeCommand;
	private final Optional<SwordType> swordType;
	private final double abilityDamageScaling;
	private final Optional<HashMap<String, Integer>> enchantments;
	private final Optional<Origin> origin;
	private final Optional<HashMap<Stats, int[]>> tiredStats;
	private final int motesSellPrice;
	private final boolean canHaveAttributes;
	private final Optional<Crystal> crystal;
	private final boolean salvageableFromRecipe;
	private final boolean riftTransferrable;
	private final Optional<SalvageUpgrade<Identifier>> salvage;
	private final Optional<PrivateIsland> privateIsland;
	private final boolean canBeReforged;
	private final boolean loseMotesValueOnTransfer;
	private final Optional<Prestige> prestige;
	private final ItemStack itemStack;

	public RepositoryItem(JsonObject jsonObject) {
		this.material = Identifier.of("minecraft", jsonObject.get("material").getAsString().toLowerCase());

		if (jsonObject.has("durability")) {
			this.durability = jsonObject.get("durability").getAsInt();
		} else {
			this.durability = -1;
		}

		if (jsonObject.has("skin")) {
			this.skin = Optional.of(jsonObject.get("skin").getAsString());
		} else {
			this.skin = Optional.empty();
		}

		if (jsonObject.get("name").isJsonObject()) {
			this.name = Text.Serializer.fromJson(jsonObject.get("name").getAsJsonObject());
		} else {
			this.name = Text.literal(jsonObject.get("name").getAsString());
		}

		if (jsonObject.has("category")) {
			this.category = Category.byName(jsonObject.get("category").getAsString());
		} else {
			this.category = Category.UNKNOWN;
		}

		if (jsonObject.has("tier")) {
			this.tier = Tier.byName(jsonObject.get("tier").getAsString());
		} else {
			this.tier = Tier.COMMON;
		}

		if (jsonObject.has("npc_sell_price")) {
			this.npcSellPrice = jsonObject.get("npc_sell_price").getAsInt();
		} else {
			this.npcSellPrice = -1;
		}

		this.skyblockId = NAMESPACE.withSuffixedPath(jsonObject.get("id").getAsString().toLowerCase().replace(":", "_"));

		if (jsonObject.has("stats")) {
			HashMap<Stats, Double> weakHashMap = new HashMap<>();
			JsonObject stats = jsonObject.getAsJsonObject("stats");
			for (String s : stats.keySet()) {
				Stats stat = Stats.valueOfIgnore(s);
				double value = stats.get(s).getAsDouble();
				weakHashMap.put(stat, value);
			}
			this.stats = Optional.of(weakHashMap);
		} else {
			this.stats = Optional.empty();
		}

		if (jsonObject.has("salvages")) {
			JsonArray salvagesObject = jsonObject.getAsJsonArray("salvages");
			ArrayList<SalvageUpgrade<?>> salvageList = new ArrayList<>();
			createSalvageUpdateList(salvagesObject, salvageList);
			this.salvages = Optional.of(salvageList);
		} else {
			this.salvages = Optional.empty();
		}

		if (jsonObject.has("color")) {
			String colorString = jsonObject.get("color").getAsString();
			String[] split = colorString.split(",", 3);
			int r = Integer.parseInt(split[0]);
			int g = Integer.parseInt(split[1]);
			int b = Integer.parseInt(split[2]);
			this.color = Optional.of(new Color(r, g, b));
		} else {
			this.color = Optional.empty();
		}

		if (jsonObject.has("soulbound")) {
			this.soulbound = Soulbound.byName(jsonObject.get("soulbound").getAsString());
		} else {
			this.soulbound = Soulbound.NONE;
		}

		this.glowing = jsonObject.has("glowing") && jsonObject.get("glowing").getAsBoolean();
		this.unstackable = jsonObject.has("unstackable") && jsonObject.get("unstackable").getAsBoolean();

		if (jsonObject.has("requirements")) {
			JsonArray requirementsArray = jsonObject.getAsJsonArray("requirements");
			ArrayList<Requirement> requirements = new ArrayList<>();
			for (JsonElement requirement : requirementsArray) {
				requirements.add(Requirement.parseRequirement(requirement.getAsJsonObject()));
			}
			this.requirements = Optional.of(requirements);
		} else {
			this.requirements = Optional.empty();
		}

		this.museum = jsonObject.has("museum") && jsonObject.get("museum").getAsBoolean();

		if (jsonObject.has("generator")) {
			this.generator = Optional.of(jsonObject.get("generator").getAsString());
		} else {
			this.generator = Optional.empty();
		}

		if (jsonObject.has("generator_tier")) {
			this.generatorTier = Optional.of(jsonObject.get("generator_tier").getAsInt());
		} else {
			this.generatorTier = Optional.empty();
		}

		if (jsonObject.has("furniture")) {
			this.furniture = Optional.of(jsonObject.get("furniture").getAsString());
		} else {
			this.furniture = Optional.empty();
		}

		// TODO: item specific attributes

		if (jsonObject.has("description") && jsonObject.get("description").isJsonArray()) {
			ArrayList<MutableText> description = new ArrayList<>();
			JsonArray asJsonArray = jsonObject.getAsJsonArray("description");
			for (JsonElement jsonElement : asJsonArray) {
				description.add(Text.Serializer.fromJson(jsonElement));
			}
			this.description = description;
		} else {
			this.description = Collections.emptyList();
		}

		if (jsonObject.has("item_durability")) {
			this.itemDurability = jsonObject.get("item_durability").getAsInt();
		} else {
			this.itemDurability = -1;
		}

		if (jsonObject.has("upgrade_costs")) {
			ArrayList<List<SalvageUpgrade<?>>> lists = new ArrayList<>();
			JsonArray upgradeRoot = jsonObject.getAsJsonArray("upgrade_costs");
			for (JsonElement jsonElement : upgradeRoot) {
				JsonArray upgradeChild = jsonElement.getAsJsonArray();

				ArrayList<SalvageUpgrade<?>> list = new ArrayList<>();
				createSalvageUpdateList(upgradeChild, list);
				lists.add(list);
			}
			this.upgradeCosts = Optional.of(lists);
		} else {
			this.upgradeCosts = Optional.empty();
		}

		if (jsonObject.has("gear_score")) {
			this.gearScore = jsonObject.get("gear_score").getAsInt();
		} else {
			this.gearScore = -1;
		}

		this.dungeonItem = jsonObject.has("dungeon_item") && jsonObject.get("dungeon_item").getAsBoolean();

		if (jsonObject.has("gemstone_slots")) {
			JsonArray gemstoneSlots = jsonObject.getAsJsonArray("gemstone_slots");
			ArrayList<GemstoneSlot> gemstoneSlotList = new ArrayList<>();

			for (JsonElement gemstoneSlotElement : gemstoneSlots) {
				JsonObject gemstoneSlot = gemstoneSlotElement.getAsJsonObject();
				GemstoneSlotTypes slotType = GemstoneSlotTypes.valueOf(gemstoneSlot.get("slot_type").getAsString());
				ArrayList<GemstoneSlotRequirement<?>> slotRequirements = new ArrayList<>();
				if (gemstoneSlot.has("costs")) {
					for (JsonElement costsElement : gemstoneSlot.getAsJsonArray("costs")) {
						JsonObject costs = costsElement.getAsJsonObject();

						String type = costs.get("type").getAsString();

						if (type.equalsIgnoreCase("COINS")) {
							slotRequirements.add(new GemstoneSlotRequirement<>(GemstoneSlotRequirementType.COINS, null, costs.get("coins").getAsInt()));
						} else if (type.equalsIgnoreCase("ITEM")) {
							slotRequirements.add(new GemstoneSlotRequirement<>(GemstoneSlotRequirementType.ITEM, NAMESPACE.withSuffixedPath(costs.get("item_id").getAsString().toLowerCase()), costs.get("amount").getAsInt()));
						}

					}
				}

				gemstoneSlotList.add(new GemstoneSlot(slotType, slotRequirements));
			}

			this.gemstoneSlots = Optional.of(gemstoneSlotList);
		} else {
			this.gemstoneSlots = Optional.empty();
		}

		if (jsonObject.has("dungeon_item_conversion_cost")) {
			JsonObject dungeonItemConversionCost = jsonObject.getAsJsonObject("dungeon_item_conversion_cost");
			this.dungeonItemConversionCost = Optional.of(new DungeonItemConversionCost(EssenceType.valueOf(dungeonItemConversionCost.get("essence_type").getAsString()), dungeonItemConversionCost.get("amount").getAsInt()));
		} else {
			this.dungeonItemConversionCost = Optional.empty();
		}

		if (jsonObject.has("catacombs_requirements")) {
			JsonArray catacombsRequirements = jsonObject.getAsJsonArray("catacombs_requirements");
			ArrayList<CatacombsRequirement> list = new ArrayList<>();
			for (JsonElement requirement : catacombsRequirements) {
				JsonObject catacombsRequirement = requirement.getAsJsonObject();
				CatacombsRequirementType type = CatacombsRequirementType.valueOf(catacombsRequirement.get("type").getAsString());
				CatacombsRequirementDungeonType dungeonType = CatacombsRequirementDungeonType.valueOf(catacombsRequirement.get("dungeon_type").getAsString());
				int level = catacombsRequirement.get("level").getAsInt();
				list.add(new CatacombsRequirement(type, dungeonType, level));
			}
			this.catacombsRequirement = Optional.of(list);
		} else {
			this.catacombsRequirement = Optional.empty();
		}

		this.hideFromViewrecipeCommand = jsonObject.has("dungeon_item") && jsonObject.get("dungeon_item").getAsBoolean();

		if (jsonObject.has("sword_type")) {
			this.swordType = Optional.of(SwordType.valueOf(jsonObject.get("sword_type").getAsString()));
		} else {
			this.swordType = Optional.empty();
		}

		if (jsonObject.has("ability_damage_scaling")) {
			this.abilityDamageScaling = jsonObject.get("ability_damage_scaling").getAsDouble();
		} else {
			this.abilityDamageScaling = -1;
		}

		if (jsonObject.has("enchantments")) {
			JsonObject enchantmentsArray = jsonObject.getAsJsonObject("enchantments");

			HashMap<String, Integer> enchantments = new HashMap<>();

			for (String s : enchantmentsArray.keySet()) {
				enchantments.put(s, enchantmentsArray.get(s).getAsInt());
			}

			this.enchantments = Optional.of(enchantments);
		} else {
			this.enchantments = Optional.empty();
		}

		if (jsonObject.has("origin")) {
			this.origin = Optional.of(Origin.valueOf(jsonObject.get("origin").getAsString()));
		} else {
			this.origin = Optional.empty();
		}

		if (jsonObject.has("tiered_stats")) {
			JsonObject tieredStats = jsonObject.getAsJsonObject("tiered_stats");
			HashMap<Stats, int[]> stats = new HashMap<>();

			for (String s : tieredStats.keySet()) {
				Stats stat = Stats.valueOfIgnore(s);
				stats.put(
						stat,
						tieredStats
								.get(s)
								.getAsJsonArray()
								.asList()
								.stream()
								.map(JsonElement::getAsInt)
								.mapToInt(p -> p)
								.toArray()
				);
			}

			this.tiredStats = Optional.of(stats);
		} else {
			this.tiredStats = Optional.empty();
		}

		if (jsonObject.has("motes_sell_price")) {
			this.motesSellPrice = jsonObject.get("motes_sell_price").getAsInt();
		} else {
			this.motesSellPrice = -1;
		}

		this.canHaveAttributes = jsonObject.has("can_have_attributes") && jsonObject.get("can_have_attributes").getAsBoolean();

		if (jsonObject.has("crystal")) {
			this.crystal = Optional.of(Crystal.valueOf(jsonObject.get("crystal").getAsString()));
		} else {
			this.crystal = Optional.empty();
		}

		this.salvageableFromRecipe = jsonObject.has("salvageable_from_recipe") && jsonObject.get("salvageable_from_recipe").getAsBoolean();
		this.riftTransferrable = jsonObject.has("rift_transferrable") && jsonObject.get("rift_transferrable").getAsBoolean();

		if (jsonObject.has("salvage")) {
			JsonObject salvage = jsonObject.getAsJsonObject("salvage");
			this.salvage = Optional.of(new SalvageUpgrade<>(SalvageType.ITEM, NAMESPACE.withSuffixedPath(salvage.get("item_id").getAsString().toLowerCase()), salvage.get("amount").getAsInt()));
		} else {
			this.salvage = Optional.empty();
		}

		if (jsonObject.has("private_island")) {
			this.privateIsland = Optional.of(PrivateIsland.valueOf(jsonObject.get("private_island").getAsString()));
		} else {
			this.privateIsland = Optional.empty();
		}

		this.canBeReforged = jsonObject.has("can_be_reforged") && jsonObject.get("can_be_reforged").getAsBoolean();
		this.loseMotesValueOnTransfer = jsonObject.has("lose_motes_value_on_transfer") && jsonObject.get("lose_motes_value_on_transfer").getAsBoolean();

		if (jsonObject.has("prestige")) {
			JsonObject prestigeObject = jsonObject.getAsJsonObject("prestige").getAsJsonObject();
			Identifier itemId = NAMESPACE.withSuffixedPath(prestigeObject.get("item_id").getAsString().toLowerCase());
			JsonArray costs = prestigeObject.getAsJsonArray("costs");
			ArrayList<SalvageUpgrade<?>> list = new ArrayList<>();
			createSalvageUpdateList(costs, list);
			this.prestige = Optional.of(new Prestige(itemId, list));
		} else {
			this.prestige = Optional.empty();
		}

		this.itemStack = constructItemStack();
	}


	public String getItemNameAlphaNumerical() {
		return Normalizer.normalize(this.getName().getString(), Normalizer.Form.NFD).replaceAll("[^A-Za-z0-9 _\\-]|§.", "").trim();
	}

	private ItemStack constructItemStack() {
		Item item = Registries.ITEM.getOrEmpty(this.material).orElse(Registries.ITEM.get(Identifier.tryParse("minecraft:barrier")));
		ItemStack itemStack = new ItemStack(item);
		itemStack.setCustomName(this.name.styled(style -> style.withItalic(false).withColor(this.tier.formatting)));

		((ItemStackTooltip) (Object) itemStack).cookies$setSkyblockItem(this);

		this.skin.ifPresent(skullValue -> {
			NbtCompound skullOwner = new NbtCompound();
			skullOwner.putUuid("Id", UUID.randomUUID());
			NbtCompound properties = new NbtCompound();
			NbtList textures = new NbtList();
			NbtCompound texture = new NbtCompound();
			texture.putString("Value", skullValue);
			textures.add(texture);
			properties.put("textures", textures);
			skullOwner.put("Properties", properties);
			itemStack.setSubNbt("SkullOwner", skullOwner);
		});

		this.color.ifPresent(color -> {
			NbtCompound display = new NbtCompound();
			display.putInt("color", color.getRGB());
			itemStack.setSubNbt("display", display);
		});

		if (this.glowing) {
			NbtList nbtElements = new NbtList();
			nbtElements.add(new NbtCompound());
			itemStack.setSubNbt("Enchantments", nbtElements);
		}

		return itemStack;
	}

	public List<Text> getTooltip(TooltipContext context) {
		List<Text> tooltip = new ArrayList<>();

		tooltip.add(Text.empty().append(this.getName()).formatted(this.getTier().formatting));

		if (this.category == Category.REFORGE_STONE) {
			tooltip.add(Text.literal("Reforge Stone").formatted(Formatting.DARK_GRAY));
			tooltip.add(Text.literal("Combinable in Reforge Anvil").formatted(Formatting.DARK_GRAY));
		} else if (this.category == Category.BAIT) {
			tooltip.add(Text.literal("Fishing Bait").formatted(Formatting.DARK_GRAY));
			tooltip.add(Text.literal("Consumed on Cast").formatted(Formatting.DARK_GRAY));
		} else if (this.category == Category.POWER_STONE) {
			tooltip.add(Text.literal("Power Stone").formatted(Formatting.DARK_GRAY));
			tooltip.add(Text.empty());
			tooltip.add(Text.literal("Combine ").append(Text.literal("9x").formatted(Formatting.GREEN)).append(" of this stone at the").formatted(Formatting.GRAY));
			tooltip.add(Text.literal("Thaumaturgist ").formatted(Formatting.GOLD).append(Text.literal("to permanently").formatted(Formatting.GRAY)));
		}

		if (this.gearScore != -1) {
			tooltip.add(Text.literal("Gear Score: ").formatted(Formatting.GRAY).append(Text.literal(String.valueOf(this.gearScore)).formatted(Formatting.LIGHT_PURPLE)));
		}

		this.furniture.ifPresent(furniture -> tooltip.add(Text.literal("Furniture").formatted(Formatting.DARK_GRAY)));

		this.stats.ifPresent(stats -> {
			if (stats.containsKey(Stats.BREAKING_POWER)) {
				tooltip.add(Text.empty().append("Breaking Power ").append(String.valueOf((int) (double) stats.get(Stats.BREAKING_POWER)).formatted(Stats.BREAKING_POWER.getFormatting())).formatted(Stats.BREAKING_POWER.getColor()));
				tooltip.add(Text.empty());
			}

			for (Map.Entry<Stats, Double> statsIntegerEntry : stats.entrySet()) {
				if (statsIntegerEntry.getKey() == Stats.BREAKING_POWER) continue;

				Stats stat = statsIntegerEntry.getKey();
				double value = statsIntegerEntry.getValue();

				MutableText statEntry = ExceptionHandler.removeThrows(
						() -> {
							DecimalFormat decimalFormat = new DecimalFormat(stat.getFormatting());

							return Text
									.literal(stat.getDisplayName())
									.append(": ")
									.formatted(Formatting.GRAY)
									.append(
											Text
													.empty()
													.append(decimalFormat.format(value))
													.formatted(stat.getColor())
									);
						});

				if (statEntry == null) {
					statEntry = Text.literal("An internal exception occurred").formatted(Formatting.DARK_RED);
				}

				if (stat.isHidden()) {
					statEntry = Text.literal("*hidden* ").append(statEntry).append(" *hidden*").formatted(Formatting.DARK_PURPLE);
				}

				tooltip.add(statEntry);
			}
		});

		this.tiredStats.ifPresent(stats -> {
			for (Map.Entry<Stats, int[]> statsIntegerEntry : stats.entrySet()) {
				if (statsIntegerEntry.getKey() == Stats.BREAKING_POWER) continue;

				Stats stat = statsIntegerEntry.getKey();
				int[] value = statsIntegerEntry.getValue();

				MutableText statEntry = ExceptionHandler.removeThrows(
						() -> {
							DecimalFormat decimalFormat = new DecimalFormat(stat.getFormatting());

							return Text
									.literal(stat.getDisplayName())
									.append(": ")
									.formatted(Formatting.GRAY)
									.append(
											Text
													.empty()
													.append(decimalFormat.format(value[Math.min(value.length - 1, this.tier.ordinal())]))
													.formatted(stat.getColor())
									);
						});

				if (statEntry == null) {
					statEntry = Text.literal("An internal exception occurred").formatted(Formatting.DARK_RED);
				}

				if (stat.isHidden()) {
					statEntry = Text.literal("*hidden* ").append(statEntry).append(" *hidden*").formatted(Formatting.DARK_PURPLE);
				}

				tooltip.add(statEntry);
			}
		});

		this.gemstoneSlots.ifPresent(gemstoneSlots -> {
			MutableText mutableText = Text.empty();
			for (GemstoneSlot gemstoneSlot : gemstoneSlots) {
				mutableText.append("[").append(Text.literal(gemstoneSlot.slotType.getIcon()).styled(style -> style.withColor(!gemstoneSlot.slots.isEmpty() ? Formatting.DARK_GRAY : Formatting.GRAY))).append("] ").formatted(Formatting.DARK_GRAY);
			}
			tooltip.add(mutableText);
		});

		tooltip.addAll(this.description.stream().filter(Predicate.not(Objects::isNull)).toList());

		if (description.isEmpty() && tooltip.size() > 1) {
			tooltip.add(Text.empty());
		}

		if (this.canBeReforged) {
			tooltip.add(Text.literal("This item can be reforged!").formatted(Formatting.DARK_GRAY));
		}

		this.requirements.ifPresent(requirements -> requirements.forEach(requirement -> tooltip.add(requirement.getRequirementString())));

		if (this.riftTransferrable) {
			tooltip.add(Text.literal("X Rift-Transferable X").formatted(Formatting.DARK_PURPLE));

			if (this.loseMotesValueOnTransfer) {
				tooltip.add(Text.literal("(Looses motes value on transfer)").formatted(Formatting.DARK_PURPLE));
			}
		}

		if (this.soulbound != Soulbound.NONE) {
			if (soulbound == Soulbound.SOLO) {
				tooltip.add(Text.literal("* Soulbound *").formatted(Formatting.DARK_GRAY));
			} else {
				tooltip.add(Text.literal("* Co-op Soulbound *").formatted(Formatting.DARK_GRAY));
			}
		}

		if (this.category == Category.ARROW) {
			tooltip.add(Text.literal("Stats added when shot!").formatted(Formatting.DARK_GRAY));
		}

		MutableText itemTierLine = Text.empty();
		itemTierLine.formatted(this.tier.formatting, Formatting.BOLD);
		itemTierLine.append(this.tier.name()).append(" ");
		if (this.dungeonItem) {
			itemTierLine.append("DUNGEON ");
		}
		if (this.category != Category.UNKNOWN && this.category != Category.NONE) {
			itemTierLine.append(this.category.name().replace("_", " "));
		}


		tooltip.add(itemTierLine);

		if (context.isAdvanced()) {
			tooltip.add(Text.empty());
			tooltip.add(Text.literal("Skyblock ID: ").formatted(Formatting.AQUA, Formatting.BOLD).append(Text.literal(this.skyblockId.toString()).styled(style -> style.withColor(Formatting.GRAY).withBold(false))));
			tooltip.add(Text.literal("Minecraft ID: ").formatted(Formatting.AQUA, Formatting.BOLD).append(Text.literal(this.material.toString()).styled(style -> style.withColor(Formatting.GRAY).withBold(false))));
		}

		return tooltip;
	}

	private void createSalvageUpdateList(JsonArray jsonArray, List<SalvageUpgrade<?>> list) {
		for (JsonElement jsonElement : jsonArray) {
			if (!(jsonElement instanceof JsonObject)) {
				continue;
			}

			JsonObject salvageResult = jsonElement.getAsJsonObject();
			String type = salvageResult.get("type").getAsString();

			if (type.equalsIgnoreCase("ESSENCE")) {
				list.add(
						new SalvageUpgrade<>(
								SalvageType.ESSENCE,
								EssenceType.valueOf(salvageResult.get("essence_type").getAsString()),
								salvageResult.get("amount").getAsInt()
						)
				);
			} else if (type.equalsIgnoreCase("ITEM")) {
				list.add(
						new SalvageUpgrade<>(
								SalvageType.ITEM,
								NAMESPACE.withSuffixedPath(salvageResult.get("item_id").getAsString().toLowerCase()),
								salvageResult.get("amount").getAsInt()
						)
				);
			}
		}
	}

	public String getTooltipAsString(TooltipContext.Default basic) {
		return getTooltip(basic).stream().map(MutableText.class::cast).map(MutableText::getString).collect(Collectors.joining("\n"));
	}

	enum SwordType {
		AXE,
		DAGGER,
		KATANA,
		SCYTHE,
		KARAMBIT
	}

	record Prestige(Identifier itemId, List<SalvageUpgrade<?>> costs) {
	}


	enum PrivateIsland {
		NETHER,
		MINING_FOREST,
		WINTER,
		DESERT,
		FARMING,
		POND,
		NETHER_WART,
		BARN,
		MINING
	}

	enum Crystal {
		FOREST_ISLAND,
		FISHING,
		RESOURCE_REGENERATOR,
		WINTER_ISLAND,
		FARM,
		WHEAT_ISLAND,
		WOODCUTTING,
		DESERT_ISLAND,
		MITHRIL,
		NETHER_WART_ISLAND
	}

	record CatacombsRequirement(CatacombsRequirementType type, CatacombsRequirementDungeonType dungeonType, int level) {
	}

	enum CatacombsRequirementDungeonType {
		CATACOMBS
	}

	enum CatacombsRequirementType {
		DUNGEON_SKILL
	}

	record DungeonItemConversionCost(EssenceType essenceType, int amount) {
	}

	record GemstoneSlot(GemstoneSlotTypes slotType, List<GemstoneSlotRequirement<?>> slots) {
	}

	// value WILL be null for T = Integer
	record GemstoneSlotRequirement<T>(GemstoneSlotRequirementType<T> requirementType, T value, int amount) {
	}

	@SuppressWarnings("unused")
	static class GemstoneSlotRequirementType<T> {
		public static GemstoneSlotRequirementType<Integer> COINS = new GemstoneSlotRequirementType<>();
		public static GemstoneSlotRequirementType<Identifier> ITEM = new GemstoneSlotRequirementType<>();
	}

	static abstract class Requirement {
		public RequirementTypes requirementType;

		public static Requirement parseRequirement(JsonObject jsonObject) {
			RequirementTypes type = RequirementTypes.valueOf(jsonObject.get("type").getAsString());

			switch (type) {
				case SKILL -> {
					return new SkillRequirement(Skill.valueOf(jsonObject.get("skill").getAsString()), jsonObject.get("level").getAsInt());
				}
				case TROPHY_FISHING -> {
					return new TrophyFishingRequirement(TrophyFishingReward.valueOf(jsonObject.get("reward").getAsString()));
				}
				case CRIMSON_ISLE_REPUTATION -> {
					return new CrimsonIsleReputationRequirement(Factions.valueOf(jsonObject.get("faction").getAsString()), jsonObject.get("reputation").getAsInt());
				}
				case SLAYER -> {
					return new SlayerRequirement(SlayerBoss.valueOfIgnore(jsonObject.get("slayer_boss_type").getAsString()), jsonObject.get("level").getAsInt());
				}
				case DUNGEON_TIER -> {
					return new DungeonTierRequirement(DungeonType.valueOf(jsonObject.get("dungeon_type").getAsString()), jsonObject.get("tier").getAsInt());
				}
				case DUNGEON_SKILL -> {
					return new DungeonSkillRequirement(DungeonType.valueOf(jsonObject.get("dungeon_type").getAsString()), jsonObject.get("level").getAsInt());
				}
				case COLLECTION -> {
					return new CollectionRequirement(jsonObject.get("tier").getAsInt(), jsonObject.get("collection").getAsString());
				}
				case TARGET_PRACTICE -> {
					return new TargetPracticeRequirement(jsonObject.get("mode").getAsString());
				}
				case HEART_OF_THE_MOUNTAIN -> {
					return new HeartOfTheMountainRequirement(jsonObject.get("tier").getAsInt());
				}
				case MELODY_HAIR -> {
					return new MelodyHairRequirement();
				}
				case GARDEN_LEVEL -> {
					return new GardenLevelRequirement(jsonObject.get("level").getAsInt());
				}
				case PROFILE_AGE -> {
					return new ProfileAgeRequirement(jsonObject.get("minimum_age").getAsInt(), ChronoUnit.valueOf(jsonObject.get("minimum_age_unit").getAsString()));
				}
				default -> throw new UnsupportedOperationException();
			}
		}

		MutableText getRequirementString() {
			return Text.empty().append(Text.literal("\u2763 ").formatted(Formatting.DARK_RED).append("Requires ").formatted(Formatting.RED));
		}
	}

	@AllArgsConstructor
	static class SkillRequirement extends Requirement {
		{
			requirementType = RequirementTypes.SKILL;
		}

		public Skill skill;
		public int level;

		@Override
		MutableText getRequirementString() {
			return super.getRequirementString().append(Text.empty().append(StringUtils.capitalize(skill.name().toLowerCase())).append(" Skill ").append(level + ".")).formatted(Formatting.GREEN);
		}
	}

	@AllArgsConstructor
	static class TrophyFishingRequirement extends Requirement {
		{
			requirementType = RequirementTypes.TROPHY_FISHING;
		}

		public TrophyFishingReward reward;

		@Override
		MutableText getRequirementString() {
			return super.getRequirementString().append(reward.getName()).append(" Trophy Fisher.");
		}
	}

	@AllArgsConstructor
	static class CrimsonIsleReputationRequirement extends Requirement {
		{
			requirementType = RequirementTypes.CRIMSON_ISLE_REPUTATION;
		}

		public Factions faction;
		public int reputation;

		@Override
		MutableText getRequirementString() {
			return super.getRequirementString().append(String.valueOf(reputation)).append(" ").append(StringUtils.capitalize(faction.name().toLowerCase())).append(" Reputation.");
		}
	}

	@AllArgsConstructor
	static class SlayerRequirement extends Requirement {
		{
			requirementType = RequirementTypes.SLAYER;
		}

		public SlayerBoss slayerBossType;
		public int level;

		@Override
		MutableText getRequirementString() {
			return Text.literal("\u2620").formatted(Formatting.DARK_RED)
					.append(" Requires ").formatted(Formatting.RED)
					.append(Text.empty().append(StringUtils.capitalize(slayerBossType.name().toLowerCase())).append(" Slayer ").append(level + ".").formatted(Formatting.DARK_PURPLE));
		}
	}

	@AllArgsConstructor
	static class DungeonTierRequirement extends Requirement {
		{
			requirementType = RequirementTypes.DUNGEON_TIER;
		}

		public DungeonType dungeonType;
		public int tier;

		@Override
		MutableText getRequirementString() {
			return super.getRequirementString().append(Text.empty().append(StringUtils.capitalize(dungeonType.name().replace("_", " ").toLowerCase()))
					.append(" Floor ").append(RomanNumerals.toRoman(tier)).append(" Completion."));
		}
	}

	@AllArgsConstructor
	static class DungeonSkillRequirement extends Requirement {
		{
			requirementType = RequirementTypes.DUNGEON_SKILL;
		}

		public DungeonType dungeonType;
		public int level;

		@Override
		MutableText getRequirementString() {
			return super.getRequirementString().append(StringUtils.capitalize(dungeonType.name().replace("_", " ").toLowerCase())).append(" Skill ").append(level + ".");
		}
	}

	@AllArgsConstructor
	static class CollectionRequirement extends Requirement {
		{
			requirementType = RequirementTypes.COLLECTION;
		}

		public int tier;
		public String collection;

		@Override
		MutableText getRequirementString() {
			return super.getRequirementString()
					.append(Text.empty().append(StringUtils.capitalize(collection.replace("_", " ").toLowerCase()))
							.append(" Collection ").append(tier + "."));
		}
	}

	@AllArgsConstructor
	static class TargetPracticeRequirement extends Requirement {
		{
			requirementType = RequirementTypes.TARGET_PRACTICE;
		}

		public String mode;

		@Override
		MutableText getRequirementString() {
			return super.getRequirementString()
					.append(Text.empty().append(" Target Practice " + mode).formatted(Formatting.GREEN));
		}
	}

	@AllArgsConstructor
	static class HeartOfTheMountainRequirement extends Requirement {
		{
			requirementType = RequirementTypes.HEART_OF_THE_MOUNTAIN;
		}

		public int tier;

		@Override
		MutableText getRequirementString() {
			return super.getRequirementString()
					.append(Text.empty().append(" Heart Of The Mountain Tier ").append(tier + ".").formatted(Formatting.DARK_PURPLE));
		}
	}

	static class MelodyHairRequirement extends Requirement {
		{
			requirementType = RequirementTypes.MELODY_HAIR;
		}

		@Override
		MutableText getRequirementString() {
			return Text.literal("*hidden* ").append(super.getRequirementString()).append(Text.literal("Melodies Harp ").formatted(Formatting.DARK_PURPLE)).append(" *hidden*").formatted(Formatting.DARK_RED);
		}
	}

	@AllArgsConstructor
	static class GardenLevelRequirement extends Requirement {
		{
			requirementType = RequirementTypes.GARDEN_LEVEL;
		}

		public int level;

		@Override
		MutableText getRequirementString() {
			return super.getRequirementString()
					.append(Text.empty().append("Garden Level").append(" 6").formatted(Formatting.GREEN)).append(".");
		}
	}

	@AllArgsConstructor
	static class ProfileAgeRequirement extends Requirement {
		{
			requirementType = RequirementTypes.PROFILE_AGE;
		}

		public int minimumAge;
		public ChronoUnit minimumAgeUnit;

		@Override
		MutableText getRequirementString() {
			return Text.literal("*hidden* ")
					.append(super.getRequirementString())
					.append(Text.literal("Profile age of ")
							.append(String.valueOf(minimumAge))
							.append(StringUtils.capitalize(minimumAgeUnit.name().toLowerCase())).formatted(Formatting.DARK_PURPLE))
					.append(" *hidden*").formatted(Formatting.DARK_RED);
		}
	}

	enum RequirementTypes {
		SKILL,
		TROPHY_FISHING,
		CRIMSON_ISLE_REPUTATION,
		SLAYER,
		DUNGEON_TIER,
		DUNGEON_SKILL,
		COLLECTION,
		TARGET_PRACTICE,
		HEART_OF_THE_MOUNTAIN,
		MELODY_HAIR,
		GARDEN_LEVEL,
		PROFILE_AGE,
	}

	record SalvageUpgrade<T>(SalvageType<T> salvageType, T value, int amount) {
	}

	@SuppressWarnings("unused")
	static class SalvageType<T> {
		public final static SalvageType<EssenceType> ESSENCE = new SalvageType<>();
		public final static SalvageType<Identifier> ITEM = new SalvageType<>();
	}

	enum Soulbound {
		SOLO,
		COOP,
		NONE;

		public static Soulbound byName(String value) {
			for (Soulbound soulbound : Soulbound.values()) {
				if (soulbound.name().equalsIgnoreCase(value)) {
					return soulbound;
				}
			}
			return NONE;
		}
	}

	@Getter
	public enum Tier {
		COMMON(Formatting.WHITE),
		UNCOMMON(Formatting.GREEN),
		RARE(Formatting.BLUE),
		EPIC(Formatting.DARK_PURPLE),
		LEGENDARY(Formatting.GOLD),
		MYTHIC(Formatting.LIGHT_PURPLE),
		SPECIAL(Formatting.RED),
		VERY_SPECIAL(Formatting.RED),
		ADMIN(Formatting.DARK_RED),
		UNOBTAINABLE(Formatting.DARK_RED),
		;

		private final Formatting formatting;

		Tier(Formatting formatting) {
			this.formatting = formatting;
		}

		public static Tier byName(String name) {
			for (Tier value : values()) {
				if (value.name().equals(name)) {
					return value;
				}
			}
			return COMMON;
		}

	}

	public enum Category {
		ACCESSORY,
		ARROW,
		ARROW_POISON,
		AXE,
		BAIT,
		BELT,
		BOOTS,
		BOW,
		BRACELET,
		CHESTPLATE,
		CLOAK,
		COSMETIC,
		DEPLOYABLE,
		DRILL,
		DUNGEON_PASS,
		FISHING_ROD,
		FISHING_WEAPON,
		GAUNTLET,
		GLOVES,
		HELMET,
		HOE,
		LEGGINGS,
		LONGSWORD,
		MEMENTO,
		NECKLACE,
		NONE,
		PET_ITEM,
		PICKAXE,
		PORTAL,
		REFORGE_STONE,
		SHEARS,
		SPADE,
		SWORD,
		TRAVEL_SCROLL,
		WAND,
		POWER_STONE,
		UNKNOWN;

		public static Category byName(String name) {
			for (Category repositoryItem : values()) {
				if (repositoryItem.name().equals(name)) {
					return repositoryItem;
				}
			}
			return UNKNOWN;
		}

	}

}
