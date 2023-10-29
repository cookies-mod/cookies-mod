package dev.morazzer.cookiesmod.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Command to open the profile viewer (currently not implemented)
 */
public class ProfileViewerCommand extends ClientCommand {

    @Override
    @NotNull
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("pv");
    }

    @Override
    @NotNull
    public List<String> getAliases() {
        return Arrays.asList("profileviewer", "profile", "view");
    }

}
