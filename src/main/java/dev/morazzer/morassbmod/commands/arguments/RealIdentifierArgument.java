package dev.morazzer.morassbmod.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class RealIdentifierArgument implements ArgumentType<Identifier> {

    private final static DynamicCommandExceptionType IDENTIFIER_NOT_FOUND = new DynamicCommandExceptionType(id -> Text.literal("Identifier " + id + " not found"));

    private final Collection<Identifier> collection;

    public RealIdentifierArgument(Collection<Identifier> collection) {
        this.collection = collection;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (builder.getRemaining().isEmpty()) {
            collection.forEach(identifier -> builder.suggest(identifier.toString()));
        } else {
            collection.stream().map(Identifier::toString).filter(s -> s.startsWith(builder.getRemaining())).forEach(builder::suggest);
        }

        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.asList("morassbmod:dev/debug_logging", "morassbmod:dev/all", "morassbmod:d");
    }

    @Override
    public Identifier parse(StringReader reader) throws CommandSyntaxException {
        final Identifier identifier = Identifier.fromCommandInput(reader);

        if (!collection.contains(identifier)) {
            throw IDENTIFIER_NOT_FOUND.create(identifier);
        }

        return identifier;
    }
}
