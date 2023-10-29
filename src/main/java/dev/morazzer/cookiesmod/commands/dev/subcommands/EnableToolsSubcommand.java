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
import org.jetbrains.annotations.NotNull;

/**
 * Used to enable disabled "devtools" in the {@linkplain dev.morazzer.cookiesmod.utils.DevUtils} class.
 */
@DevSubcommand
public class EnableToolsSubcommand extends ClientCommand {

    @Override
    @NotNull
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("enable").then(argument(
                "tool",
                new RealIdentifierArgument(DevUtils.getDisabledTools(), "cookiesmod", "dev/")
        ).executes(context -> {
            Identifier identifier = context.getArgument("tool", Identifier.class);
            boolean enable = DevUtils.enable(identifier);
            if (!enable) {
                context.getSource()
                        .sendError(CookiesMod.createPrefix(ColorUtils.failColor)
                                .append("No devtool found with name ")
                                .append(identifier.toString()));
                return 0;
            }

            context.getSource().sendFeedback(CookiesMod.createPrefix(ColorUtils.successColor)
                    .append("Enabled devtool ")
                    .append(identifier.toString()));
            return Command.SINGLE_SUCCESS;
        }));
    }

}
