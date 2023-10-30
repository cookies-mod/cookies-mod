package dev.morazzer.cookiesmod.commands.helpers;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Helper methods to create client sided {@linkplain com.mojang.brigadier.builder.LiteralArgumentBuilder} and {@linkplain com.mojang.brigadier.builder.RequiredArgumentBuilder} arguments.
 */
public interface Helper {

    /**
     * Helper method to create a {@code literal} argument for a client-sided command.
     *
     * @param name The name of the argument.
     * @return The argument builder.
     */
    @NotNull
    @Contract(pure = true)
    default LiteralArgumentBuilder<FabricClientCommandSource> literal(@NotNull @NotBlank String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    /**
     * Helper method to create a {@code required} argument for a client-sided command,
     * despite it's name these arguments can also be optional.
     *
     * @param name         The name of the argument.
     * @param argumentType An argument type instance.
     * @param <T>          The type of the argument.
     * @return The argument builder.
     */
    @NotNull
    @Contract(pure = true)
    default <T> RequiredArgumentBuilder<FabricClientCommandSource, T> argument(
            @NotNull @NotBlank String name,
            @NotNull ArgumentType<T> argumentType
    ) {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }

}
