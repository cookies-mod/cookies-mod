package dev.morazzer.cookiesmod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import dev.morazzer.cookiesmod.commands.helpers.LoadCommand;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.config.system.ConfigScreen;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Command to open the config screen.
 */
@LoadCommand
public class OpenConfigCommand extends ClientCommand {

    @Override
    @NotNull
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("cookiesmod")
                .executes(context -> {
                    MinecraftClient
                            .getInstance()
                            .send(() -> MinecraftClient
                                    .getInstance()
                                    .setScreen(new ConfigScreen(ConfigManager.getConfigReader())));
                    return Command.SINGLE_SUCCESS;
                });
    }

    @Override
    @NotNull
    public List<String> getAliases() {
        return Arrays.asList("cm", "cookie", "cookies");
    }

}
