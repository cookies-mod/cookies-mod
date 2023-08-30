package dev.morazzer.cookiesmod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import dev.morazzer.cookiesmod.commands.helpers.Helper;
import dev.morazzer.cookiesmod.commands.helpers.LoadCommand;
import dev.morazzer.cookiesmod.config.ConfigManager;
import io.github.moulberry.moulconfig.gui.GuiScreenElementWrapper;
import io.github.moulberry.moulconfig.gui.MoulConfigEditor;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import java.util.Arrays;
import java.util.List;

@LoadCommand
public class OpenConfigCommand extends ClientCommand {
	@Override
	public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
		return Helper.literal("cookiesmod")
				.executes(context -> {
					MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(
							new GuiScreenElementWrapper(
									new MoulConfigEditor<>(ConfigManager.getProcessedConfig())
							)
					));
					return Command.SINGLE_SUCCESS;
				});
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("cm", "cookie", "cookies");
	}
}
