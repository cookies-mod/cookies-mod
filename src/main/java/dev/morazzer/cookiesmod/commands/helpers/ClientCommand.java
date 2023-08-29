package dev.morazzer.cookiesmod.commands.helpers;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.SingleRedirectModifier;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.morazzer.cookiesmod.utils.DevUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public abstract class ClientCommand {
    private static Identifier identifier = new Identifier("cookie", "commands");

    public abstract LiteralArgumentBuilder<FabricClientCommandSource> getCommand();

    private String originalCommandName;

    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> command = this.getCommand();
        if (!this.isAvailableOnServers()) {
            Predicate<FabricClientCommandSource> requirement = command.getRequirement();
            command.requires(fabricClientCommandSource -> {
                ClientWorld world = MinecraftClient.getInstance().world;
                return (this.isAvailableOnServers() || world != null && world.isClient()) && requirement.test(fabricClientCommandSource);
            });
        }

        LiteralCommandNode<FabricClientCommandSource> register = dispatcher.register(command);
        this.originalCommandName = register.getName();
        for (String alias : getAliases()) {
            dispatcher.register(Helper
                    .literal(alias)
                    .executes(command.getCommand())
                    .requires(command.getRequirement())
                    .redirect(register, getRedirectModifier(alias)));

            if (alias.startsWith("cookie")) alias = alias.substring(6);

            String namespace = String.format("%s:%s", identifier.getNamespace(), alias);
            dispatcher.register(Helper
                    .literal(namespace)
                    .executes(command.getCommand())
                    .requires(command.getRequirement())
                    .redirect(register, getRedirectModifier(namespace)));
        }

        String name = this.originalCommandName;
        if (name.startsWith("cookie")) name = name.substring(6);
        String namespace = String.format("%s:%s", identifier.getNamespace(), name);

        dispatcher.register(Helper
                .literal(namespace)
                .executes(command.getCommand())
                .requires(command.getRequirement())
                .redirect(register, getRedirectModifier(namespace)));
    }

    public SingleRedirectModifier<FabricClientCommandSource> getRedirectModifier(String commandName) {
        return context -> {
            DevUtils.log("command-redirected", "Redirected from command {} to {}", commandName, originalCommandName);
            return context.getSource();
        };
    }

    public List<String> getAliases() {
        return Collections.emptyList();
    }

    public boolean isAvailableOnServers() {
        return true;
    }

}
