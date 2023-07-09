package dev.morazzer.morassbmod.commands.helpers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface Helper {

    static LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    static <T> RequiredArgumentBuilder<FabricClientCommandSource, T> argument(String name, ArgumentType<T> argumentType) {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }

    @SuppressWarnings("unused")
    static <T> CompletableFuture<Suggestions> suggest(Collection<T> list, CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        return suggest(list, context, builder, null);
    }

    @SuppressWarnings("unused")
    static <T> CompletableFuture<Suggestions> suggest(Collection<T> list, CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder, SimpleCommandExceptionType exception) throws CommandSyntaxException {
        if (builder.getRemaining().isEmpty()) {
            list.forEach(object -> builder.suggest(object.toString()));
        } else {
            list.stream().map(Object::toString).filter(s -> s.startsWith(builder.getRemaining())).forEach(builder::suggest);
        }

        if (exception != null && builder.build().isEmpty()) {
            System.out.println("a");
            StringReader stringReader = new StringReader(builder.getInput());
            stringReader.setCursor(builder.getStart());
            throw exception.createWithContext(stringReader);
        }

        return builder.buildFuture();
    }
}
