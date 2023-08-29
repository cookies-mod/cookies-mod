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
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.util.InvalidIdentifierException;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class PlayerNameArgument implements ArgumentType<String> {
    private static final DynamicCommandExceptionType COMMAND_EXCEPTION = new DynamicCommandExceptionType(o -> CookiesMod.createPrefix(ColorUtils.failColor)
            .append("Player name \"")
            .append(Objects.toString(o))
            .append("\" is not a valid username"));

    public static PlayerNameArgument player() {
        return new PlayerNameArgument();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        while(reader.canRead() && isCharValid(reader.peek())) {
            reader.skip();
        }

        String playerName = reader.getString().substring(i, reader.getCursor());

        try {
            return playerName;
        } catch (InvalidIdentifierException var4) {
            reader.setCursor(i);
            throw COMMAND_EXCEPTION.createWithContext(reader, playerName);
        }
    }

    private boolean isCharValid(char peek) {
        return peek >= 'a' && peek <= 'z' || peek >= 'A' && peek <= 'Z' || peek >= '0' && peek <= '9' || peek == '_';
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof FabricClientCommandSource commandSource) {
            Collection<String> playerNames = commandSource.getPlayerNames();

            if (builder.getRemaining().isEmpty()) {
                playerNames.forEach(builder::suggest);
            } else {
                playerNames.stream()
                        .filter(s -> s.startsWith(builder.getRemaining()))
                        .forEach(builder::suggest);
            }
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return ArgumentType.super.getExamples();
    }
}
