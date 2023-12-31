package dev.morazzer.cookiesmod.utils.general;

import dev.morazzer.cookiesmod.data.profile.GameMode;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.Text;

/**
 * Various methods related to scoreboards.
 */
public class ScoreboardUtils {

    private static final String SKYBLOCK_SCOREBOARD_OBJECTIVE_NAME = "SBScoreboard";

    /**
     * Gets the title of the scoreboard.
     *
     * @return The title.
     */
    public static Text getTitle() {
        return getScoreboardObjective().map(ScoreboardObjective::getDisplayName).orElse(Text.empty());
    }

    /**
     * Gets all lines on the scoreboard.
     *
     * @return The lines.
     */
    public static List<String> getAllLines() {
        Collection<ScoreboardPlayerScore> scoreboardPlayerScores = getScoreboardObjective()
            .flatMap(objective -> Optional
                .ofNullable(MinecraftClient.getInstance().player)
                .map(PlayerEntity::getScoreboard)
                .map(scoreboard -> scoreboard.getAllPlayerScores(objective)))
            .orElse(Collections.emptyList());

        if (scoreboardPlayerScores.isEmpty()) {
            return Collections.emptyList();
        }

        return scoreboardPlayerScores
            .stream()
            .map(score -> Optional
                .ofNullable(MinecraftClient.getInstance().player)
                .map(PlayerEntity::getScoreboard)
                .map(scoreboard -> scoreboard.getPlayerTeam(score.getPlayerName()))
                .map(team -> team.getPrefix().getString() + team.getSuffix().getString())
                .orElse(""))
            .toList();
    }

    /**
     * Gets the current location as String.
     *
     * @return The location.
     */
    public static String getCurrentLocation() {
        return getAllLines()
            .stream()
            .map(String::trim)
            .filter(line -> line.matches("[⏣ф] .+"))
            .findFirst()
            .orElse("");
    }

    /**
     * Gets the game mode of the current profile.
     *
     * @return The game mode.
     */
    public static GameMode getCurrentGameMode() {
        return getAllLines().stream()
            .map(String::trim)
            .filter(line -> line.matches("[^A-Za-z0-9⏣] .*"))
            .map(GameMode::getByString)
            .findFirst()
            .orElse(GameMode.CLASSIC);
    }

    /**
     * Gets the skyblock scoreboard objective.
     *
     * @return The scoreboard objective.
     */
    private static Optional<ScoreboardObjective> getScoreboardObjective() {
        return Optional.ofNullable(MinecraftClient.getInstance().player)
            .map(PlayerEntity::getScoreboard)
            .map(scoreboard -> scoreboard.getNullableObjective(SKYBLOCK_SCOREBOARD_OBJECTIVE_NAME));
    }

}
