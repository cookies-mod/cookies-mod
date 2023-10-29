package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.data.dungeons.DungeonType;
import dev.morazzer.cookiesmod.data.enums.Factions;
import dev.morazzer.cookiesmod.data.enums.Skill;
import dev.morazzer.cookiesmod.data.enums.SlayerBoss;
import dev.morazzer.cookiesmod.data.enums.TrophyFishingReward;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.time.temporal.ChronoUnit;

/**
 * A generic item requirement.
 */
public abstract class Requirement {

    public RequirementTypes requirementType;

    /**
     * Parse a json object into an instance of a requirement.
     *
     * @param jsonObject The json object.
     * @return The new requirement.
     */
    public static Requirement parseRequirement(JsonObject jsonObject) {
        RequirementTypes type = RequirementTypes.valueOf(jsonObject.get("type").getAsString());

        switch (type) {
            case SKILL -> {
                return new SkillRequirement(
                        Skill.valueOf(jsonObject.get("skill").getAsString()),
                        jsonObject.get("level").getAsInt()
                );
            }
            case TROPHY_FISHING -> {
                return new TrophyFishingRequirement(TrophyFishingReward.valueOf(jsonObject
                        .get("reward")
                        .getAsString()));
            }
            case CRIMSON_ISLE_REPUTATION -> {
                return new CrimsonIsleReputationRequirement(
                        Factions.valueOf(jsonObject.get("faction").getAsString()),
                        jsonObject.get("reputation").getAsInt()
                );
            }
            case SLAYER -> {
                return new SlayerRequirement(
                        SlayerBoss.valueOfIgnore(jsonObject.get("slayer_boss_type").getAsString()),
                        jsonObject.get("level").getAsInt()
                );
            }
            case DUNGEON_TIER -> {
                return new DungeonTierRequirement(
                        DungeonType.valueOf(jsonObject.get("dungeon_type").getAsString()),
                        jsonObject.get("tier").getAsInt()
                );
            }
            case DUNGEON_SKILL -> {
                return new DungeonSkillRequirement(
                        DungeonType.valueOf(jsonObject.get("dungeon_type").getAsString()),
                        jsonObject.get("level").getAsInt()
                );
            }
            case COLLECTION -> {
                return new CollectionRequirement(
                        jsonObject.get("tier").getAsInt(),
                        jsonObject.get("collection").getAsString()
                );
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
                return new ProfileAgeRequirement(
                        jsonObject.get("minimum_age").getAsInt(),
                        ChronoUnit.valueOf(jsonObject.get("minimum_age_unit").getAsString())
                );
            }
            default -> throw new UnsupportedOperationException();
        }
    }

    public MutableText getRequirementString() {
        return Text
                .empty()
                .append(Text
                        .literal("\u2763 ")
                        .formatted(Formatting.DARK_RED)
                        .append("Requires ")
                        .formatted(Formatting.RED));
    }

}
