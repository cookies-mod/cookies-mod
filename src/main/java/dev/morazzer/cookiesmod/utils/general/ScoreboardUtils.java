package dev.morazzer.cookiesmod.utils.general;

import dev.morazzer.cookiesmod.data.profile.GameMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ScoreboardUtils {

    private static final String SKYBLOCK_SCOREBOARD_OBJECTIVE_NAME = "SBScoreboard";

    /**
     * Get the title of the scoreboard.
     *
     * @return The title.
     */
    public static Text getTitle() {
        return getScoreboardObjective().map(ScoreboardObjective::getDisplayName).orElse(Text.empty());
    }

    /**
     * Get all lines on the scoreboard.
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
     * Get the current location as String.
     *
     * @return The location.
     */
    public static String getCurrentLocation() {
        return getAllLines()
                .stream()
                .map(String::trim)
                .filter(line -> line.matches("[\u23E3\u0444] .+"))
                .findFirst()
                .orElse("");
    }

    /**
     * Get the game mode of the current profile.
     *
     * @return The game mode.
     */
    public static GameMode getCurrentGameMode() {
        return getAllLines().stream()
                .map(String::trim)
                .filter(line -> line.matches("[^A-Za-z0-9\u23E3] .*"))
                .map(GameMode::getByString)
                .findFirst()
                .orElse(GameMode.CLASSIC);
    }

    /**
     * Get the skyblock scoreboard objective.
     *
     * @return The scoreboard objective.
     */
    private static Optional<ScoreboardObjective> getScoreboardObjective() {
        return Optional.ofNullable(MinecraftClient.getInstance().player)
                .map(PlayerEntity::getScoreboard)
                .map(scoreboard -> scoreboard.getNullableObjective(SKYBLOCK_SCOREBOARD_OBJECTIVE_NAME));
    }

}
