package dev.morazzer.cookiesmod.commands.dev.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.commands.arguments.RealIdentifierArgument;
import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.DevUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.util.Identifier;

@DevSubcommand
public class DisableToolsSubcommand extends ClientCommand {
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("disable").then(argument(
                "tool",
                new RealIdentifierArgument(DevUtils.getEnabledTools())
        ).executes(context -> {
            Identifier identifier = context.getArgument("tool", Identifier.class);
            boolean disabled = DevUtils.disable(identifier);
            if (!disabled) {
                context.getSource()
                        .sendError(CookiesMod.createPrefix(ColorUtils.failColor).append("No devtool found with name")
                                .append(identifier.toString()));
                return 0;
            }

            context.getSource()
                    .sendFeedback(CookiesMod.createPrefix(ColorUtils.successColor).append("Disabled devtool ")
                            .append(identifier.toString()));
            return Command.SINGLE_SUCCESS;
        }));
    }
}
