package dev.morazzer.morassbmod.commands.dev;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.morazzer.morassbmod.MorasSbMod;
import dev.morazzer.morassbmod.commands.arguments.RealIdentifierArgument;
import dev.morazzer.morassbmod.commands.helpers.ClientCommand;
import dev.morazzer.morassbmod.utils.DevUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static dev.morazzer.morassbmod.commands.helpers.Helper.argument;
import static dev.morazzer.morassbmod.commands.helpers.Helper.literal;

public class DevCommand extends ClientCommand {


    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("dev")
                .then(
                        literal("enable")
                                .then(
                                        argument("tool", new RealIdentifierArgument(DevUtils.getDisabledTools()))
                                                .executes(context -> {
                                                    Identifier tool = context.getArgument("tool", Identifier.class);

                                                    boolean enable = DevUtils.enable(tool);

                                                    if (!enable) {
                                                        context.getSource().sendError(Text.of("No devtool found with name " + tool));
                                                        return 0;
                                                    }

                                                    context.getSource().sendFeedback(
                                                            MorasSbMod.createPrefix()
                                                                    .append(Text.literal("Enabled devtool ").styled(style -> style.withColor(0x77DD77)))
                                                                    .append(Text.literal(tool.toString()).styled(style -> style.withColor(0x77DD77)))
                                                    );

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                )
                )
                .then(
                        literal("disable")
                                .then(
                                        argument("tool", new RealIdentifierArgument(DevUtils.getEnabledTools()))
                                                .executes(context -> {
                                                    Identifier tool = context.getArgument("tool", Identifier.class);

                                                    boolean disable = DevUtils.disable(tool);

                                                    if (!disable) {
                                                        context.getSource().sendError(Text.of("No devtool found with name " + tool));
                                                        return 0;
                                                    }

                                                    context.getSource().sendFeedback(
                                                            MorasSbMod.createPrefix()
                                                                    .append(Text.literal("Disabled devtool ").styled(style -> style.withColor(0xFF6961)))
                                                                    .append(Text.literal(tool.toString()).styled(style -> style.withColor(0xFF6961)))
                                                    );

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                )
                );
    }

}
