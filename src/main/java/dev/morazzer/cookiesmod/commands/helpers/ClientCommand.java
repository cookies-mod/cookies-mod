package dev.morazzer.cookiesmod.commands.helpers;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.SingleRedirectModifier;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.morazzer.cookiesmod.generated.LoadCommandLoader;
import dev.morazzer.cookiesmod.utils.DevUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A client command is a command that will always only be available to the client itself. This class helps with creating
 * those, and therefore every client command has to extend it.
 */
@Slf4j
public abstract class ClientCommand implements Helper {

    private static final Identifier identifier = new Identifier("cookie", "commands");
    private String originalCommandName;

    /**
     * Loads all classes that extend {@linkplain dev.morazzer.cookiesmod.commands.helpers.ClientCommand} and are
     * annotated with {@linkplain dev.morazzer.cookiesmod.commands.helpers.LoadCommand} into the command registry.
     *
     * @param dispatcher  The command dispatcher that is provided by the
     *                    {@linkplain net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback}.
     */
    public static void loadCommands(@NotNull CommandDispatcher<FabricClientCommandSource> dispatcher) {
        Arrays.stream(LoadCommandLoader.getClasses()).forEach(clazz -> {
            log.debug("Found class annotated with @LoadCommand");
            if (!ClientCommand.class.isAssignableFrom(clazz)) {
                log.warn("{} does not extend ClientCommand but is annotated with @LoadCommand", clazz);
                return;
            }

            try {
                //noinspection unchecked
                Constructor<? extends ClientCommand> constructor =
                    (Constructor<? extends ClientCommand>) clazz.getConstructor();
                ClientCommand clientCommand = constructor.newInstance();
                clientCommand.register(dispatcher);
            } catch (NoSuchMethodException e) {
                log.warn("No empty constructor found for class {}", clazz);
            } catch (InvocationTargetException e) {
                log.error("Error while invoking constructor {}", clazz, e);
            } catch (InstantiationException e) {
                log.error("Module {} is an abstract class", clazz);
            } catch (IllegalAccessException e) {
                log.error("Constructor not accessible {}", clazz);
            }
        });
    }

    @NotNull
    public abstract LiteralArgumentBuilder<FabricClientCommandSource> getCommand();

    /**
     * Registers an instance of a command with all the respective checks and aliases/namespaces.
     *
     * @param dispatcher The command dispatcher.
     */
    public void register(@NotNull CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> command = this.getCommand();
        if (!this.isAvailableOnServers()) {
            Predicate<FabricClientCommandSource> requirement = command.getRequirement();
            command.requires(fabricClientCommandSource -> {
                ClientWorld world = MinecraftClient.getInstance().world;
                return (this.isAvailableOnServers()
                    || world != null
                    && world.isClient())
                    && requirement.test(fabricClientCommandSource);
            });
        }

        LiteralCommandNode<FabricClientCommandSource> register = dispatcher.register(command);
        this.originalCommandName = register.getName();
        for (String alias : getAliases()) {
            dispatcher.register(literal(alias)
                .executes(command.getCommand())
                .requires(command.getRequirement())
                .redirect(register, getRedirectModifier(alias)));

            if (alias.startsWith("cookie")) {
                alias = alias.substring(6);
            }

            String namespace = String.format("%s:%s", identifier.getNamespace(), alias);
            dispatcher.register(literal(namespace)
                .executes(command.getCommand())
                .requires(command.getRequirement())
                .redirect(register, getRedirectModifier(namespace)));
        }

        String name = this.originalCommandName;
        if (name.startsWith("cookie")) {
            name = name.substring(6);
        }
        String namespace = String.format("%s:%s", identifier.getNamespace(), name);

        dispatcher.register(literal(namespace)
            .executes(command.getCommand())
            .requires(command.getRequirement())
            .redirect(register, getRedirectModifier(namespace)));
    }

    /**
     * Helper method to create a redirect modifier for the aliases.
     *
     * @param commandName The name of the alias (only used for debug).
     * @return A new redirect modifier.
     */
    @NotNull
    public SingleRedirectModifier<FabricClientCommandSource> getRedirectModifier(@NotNull String commandName) {
        return context -> {
            DevUtils.log("command-redirected", "Redirected from command {} to {}", commandName, originalCommandName);
            return context.getSource();
        };
    }

    /**
     * Gets all the aliases of the current command instance.
     *
     * @return All aliases.
     */
    @NotNull
    @Contract(pure = true)
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    /**
     * Checks whether the current command is available on servers.
     *
     * @return Whether the command is available on servers.
     */
    public boolean isAvailableOnServers() {
        return true;
    }

}
