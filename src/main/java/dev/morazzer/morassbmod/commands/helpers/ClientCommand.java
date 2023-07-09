package dev.morazzer.morassbmod.commands.helpers;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.SingleRedirectModifier;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.morazzer.morassbmod.utils.DevUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

import static dev.morazzer.morassbmod.commands.helpers.Helper.literal;

public abstract class ClientCommand {

    private static final Identifier identifier = new Identifier("morassbmod", "commands");

    abstract public LiteralArgumentBuilder<FabricClientCommandSource> getCommand();

    String originalCommandName;

    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> command = getCommand();

        if (!isAvailableOnServers()) {
            command.requires(fabricClientCommandSource -> {
                ClientWorld world = MinecraftClient.getInstance().world;
                return isAvailableOnServers() || (world != null && world.isClient);
            });
        }

        LiteralCommandNode<FabricClientCommandSource> registeredCommand = dispatcher.register(command);

        originalCommandName = command.getLiteral();

        for (String alias : getAliases()) {
            dispatcher.register(literal(alias).executes(context -> command.getCommand().run(context)).redirect(registeredCommand, getRedirectModifier(alias)));

            if (alias.startsWith("msbm")) {
                alias = alias.substring(4);
            }

            String namespaceName = String.format("%s:%s", identifier.getNamespace(), alias);
            dispatcher.register(literal(namespaceName).executes(context -> command.getCommand().run(context)).redirect(registeredCommand, getRedirectModifier(namespaceName)));
        }

        String name = originalCommandName;
        if (name.startsWith("msbm")) {
            name = name.substring(4);
        }

        String namespaceName = String.format("%s:%s", identifier.getNamespace(), name);
        dispatcher.register(literal(namespaceName).executes(context -> command.getCommand().run(context)).redirect(registeredCommand, getRedirectModifier(namespaceName)));
    }

    private SingleRedirectModifier<FabricClientCommandSource> getRedirectModifier(String commandName) {
        return context -> {
            DevUtils.log("command-redirected", "Redirected from command {} to {}", commandName, originalCommandName);
            return context.getSource();
        };
    }

    protected String[] getAliases() {
        return new String[]{};
    }

    boolean isAvailableOnServers() {
        return true;
    }

}
