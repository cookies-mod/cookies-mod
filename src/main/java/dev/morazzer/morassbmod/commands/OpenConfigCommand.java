package dev.morazzer.morassbmod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.morazzer.morassbmod.commands.helpers.ClientCommand;
import dev.morazzer.morassbmod.gui.screen.config.ConfigScreen;
import dev.morazzer.morassbmod.utils.DevUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import static dev.morazzer.morassbmod.commands.helpers.Helper.literal;


public class OpenConfigCommand extends ClientCommand {

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("msbmdc")
                .executes(context -> {
                    DevUtils.log("configdebug", "Opening debug config");
                    context.getSource().getClient().send(() ->
                            MinecraftClient.getInstance().setScreen(new ConfigScreen()));
                    return Command.SINGLE_SUCCESS;
                });
    }

    @Override
    protected String[] getAliases() {
        return new String[]{"msbmdebugconfig"};
    }
}
