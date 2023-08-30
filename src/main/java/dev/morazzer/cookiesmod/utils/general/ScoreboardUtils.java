package dev.morazzer.cookiesmod.utils.general;

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

	public static Text getTitle() {
		return getScoreboardObjective().map(ScoreboardObjective::getDisplayName).orElse(Text.empty());
	}

	public static List<String> getAllLines() {
		Collection<ScoreboardPlayerScore> scoreboardPlayerScores = getScoreboardObjective()
				.flatMap(objective -> Optional.ofNullable(MinecraftClient.getInstance().player)
						.map(PlayerEntity::getScoreboard)
						.map(scoreboard -> scoreboard.getAllPlayerScores(objective))
				).orElse(Collections.emptyList());

		if (scoreboardPlayerScores.isEmpty()) {
			return Collections.emptyList();
		}

		return scoreboardPlayerScores.stream()
				.map(score -> Optional.ofNullable(MinecraftClient.getInstance().player)
						.map(PlayerEntity::getScoreboard)
						.map(scoreboard -> scoreboard.getPlayerTeam(score.getPlayerName()))
						.map(team -> team.getPrefix().getString() + team.getSuffix().getString())
						.orElse("")
				).toList();
	}

	public static String getCurrentLocation() {
		return getAllLines().stream().map(String::trim).filter(line -> line.matches("\u23E3 .+")).findFirst().orElse("");
	}

	private static Optional<ScoreboardObjective> getScoreboardObjective() {
		return Optional.ofNullable(MinecraftClient.getInstance().player)
				.map(PlayerEntity::getScoreboard)
				.map(scoreboard -> scoreboard.getObjective(SKYBLOCK_SCOREBOARD_OBJECTIVE_NAME));
	}

}
