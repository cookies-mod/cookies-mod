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
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class RealIdentifierArgument implements ArgumentType<Identifier> {

    private final Collection<Identifier> identifierCollection;
    private final DynamicCommandExceptionType IDENTIFIER_NOT_FOUND = new DynamicCommandExceptionType(id -> CookiesMod.createPrefix(ColorUtils.failColor).append("Identifier %s not found".formatted(id)));

    public RealIdentifierArgument(Collection<Identifier> identifierCollection) {
        this.identifierCollection = identifierCollection;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (builder.getRemaining().isEmpty()) {
            identifierCollection.forEach(identifier -> builder.suggest(identifier.toString()));
        } else {
            identifierCollection.stream().map(Identifier::toString)
                    .filter(s -> s.startsWith(builder.getRemaining()))
                    .forEach(builder::suggest);
        }

        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.asList("cookie:dev/debug_logging", "cookie:dev/");
    }

    @Override
    public Identifier parse(StringReader reader) throws CommandSyntaxException {
        Identifier identifier = Identifier.fromCommandInput(reader);
        if (!identifierCollection.contains(identifier)) {
            throw IDENTIFIER_NOT_FOUND.create(identifier);
        }

        return identifier;
    }
}
