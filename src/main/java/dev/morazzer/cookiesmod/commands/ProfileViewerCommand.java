package dev.morazzer.cookiesmod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.morazzer.cookiesmod.commands.arguments.PlayerNameArgument;
import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import dev.morazzer.cookiesmod.commands.helpers.LoadCommand;
import dev.morazzer.cookiesmod.features.PlayerManager;
import dev.morazzer.cookiesmod.features.ProfileViewerManager;
import dev.morazzer.cookiesmod.screen.ProfileViewerScreen;
import dev.morazzer.cookiesmod.utils.ConcurrentUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@LoadCommand
public class ProfileViewerCommand extends ClientCommand {
	@Override
	public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
		return literal("pv")
				.executes(this::openScreen)
				.then(argument("name", PlayerNameArgument.player())
						.executes(this::openScreen)
				);
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("profileviewer", "profile", "view");
	}

	private int openScreen(CommandContext<FabricClientCommandSource> context) {
		final String userName = getName(context);
		if (MinecraftClient.getInstance().player != null) {
			CompletableFuture<Void> future = new CompletableFuture<>();

			ConcurrentUtils.execute(() -> {
				ProfileViewerManager.setLastSearch(PlayerManager.getUUID(userName));
				MinecraftClient.getInstance().player.sendMessage(Text.literal(userName));
				future.complete(null);
			});

			MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(new ProfileViewerScreen(future)));
		}

		return Command.SINGLE_SUCCESS;
	}

	private String getName(CommandContext<FabricClientCommandSource> context) {
		try {
			return context.getArgument("name", String.class);
		} catch (Exception e) {
			return Optional.ofNullable(MinecraftClient.getInstance().player).map(Entity::getEntityName).orElse("");
		}
	}
}
