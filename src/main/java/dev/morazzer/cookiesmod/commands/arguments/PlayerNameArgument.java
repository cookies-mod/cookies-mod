package dev.morazzer.cookiesmod.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Used to read a player's name as an argument for commands and disallow everything else. This includes all valid player
 * names matching the regex [a-zA-z0-9_]{1,16}.
 */
public class PlayerNameArgument implements ArgumentType<String> {

    private static final int PLAYER_NAME_LENGTH = 16;

    private static final DynamicCommandExceptionType COMMAND_EXCEPTION = new DynamicCommandExceptionType(o -> CookiesMod
        .createPrefix(ColorUtils.failColor)
        .append("Player name \"")
        .append(Objects.toString(o))
        .append("\" is not a valid username"));

    /**
     * Static constructor to fit the style of all {@link com.mojang.brigadier.arguments.ArgumentType} types.
     *
     * @return A newly created instance of {@link dev.morazzer.cookiesmod.commands.arguments.PlayerNameArgument}.
     */
    @Contract(pure = true)
    public static PlayerNameArgument player() {
        return new PlayerNameArgument();
    }

    /**
     * Parser to get a valid string username from the input.
     *
     * @param reader The string reader.
     * @return The parsed player name.
     * @throws CommandSyntaxException If the provided reader doesn't contain a player name.
     */
    @Override
    public String parse(@NotNull StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        while (reader.canRead() && isCharValid(reader.peek())) {
            reader.skip();
        }

        String playerName = reader.getString().substring(i, reader.getCursor());

        if (playerName.length() > PLAYER_NAME_LENGTH) {
            reader.setCursor(i);
            throw COMMAND_EXCEPTION.createWithContext(reader, playerName);
        }

        return playerName;
    }

    /**
     * Suggestions for the player names.
     *
     * @param context The {@linkplain com.mojang.brigadier.context.CommandContext} provided by the
     *                {@linkplain com.mojang.brigadier.builder.ArgumentBuilder#executes(com.mojang.brigadier.Command)}
     *                method.
     * @param builder A {@linkplain com.mojang.brigadier.suggestion.SuggestionsBuilder} which is also provided by the
     *                {@linkplain com.mojang.brigadier.builder.ArgumentBuilder#executes(com.mojang.brigadier.Command)}
     *                method.
     * @param <S>     The {@linkplain net.minecraft.command.CommandSource} type.
     * @return A future that will resolve to the suggestions.
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(
        @NotNull CommandContext<S> context,
        @NotNull SuggestionsBuilder builder
    ) {
        if (context.getSource() instanceof FabricClientCommandSource commandSource) {
            Collection<String> playerNames = commandSource.getPlayerNames();

            if (builder.getRemaining().isEmpty()) {
                playerNames.forEach(builder::suggest);
            } else {
                playerNames.stream().filter(s -> s.startsWith(builder.getRemaining())).forEach(builder::suggest);
            }
        }
        return builder.buildFuture();
    }

    /**
     * Checks if a character is a valid player name character.
     *
     * @param peek The character to check.
     * @return Rather or not, the character is valid.
     */
    @Contract(pure = true)
    private boolean isCharValid(char peek) {
        return peek >= 'a' && peek <= 'z' || peek >= 'A' && peek <= 'Z' || peek >= '0' && peek <= '9' || peek == '_';
    }

}
