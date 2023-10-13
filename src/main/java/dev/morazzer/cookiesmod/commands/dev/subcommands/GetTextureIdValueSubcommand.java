package dev.morazzer.cookiesmod.commands.dev.subcommands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.general.ItemUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;

import java.util.Optional;

@DevSubcommand
public class GetTextureIdValueSubcommand extends ClientCommand {
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("get_item_id_as_float").executes(context -> {
            Optional<String> s = ItemUtils.getMainHand().flatMap(ItemUtils::getSkyblockId);
            if (s.isEmpty()) {
                context.getSource().sendFeedback(CookiesMod.createPrefix(ColorUtils.failColor)
                        .append("Item does not have any skyblock id"));
                return -1;
            }
            context.getSource().sendFeedback(CookiesMod.createPrefix(ColorUtils.successColor)
                    .append(Text.literal("Item Id: ").append(String.valueOf(s.get().hashCode() / 1000000f))
                            .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,
                                    String.valueOf(s.get().hashCode() / 1000000f)
                            )))));
            return 1;
        }).then(argument("id", StringArgumentType.word()).executes(context -> {
            String id = context.getArgument("id", String.class);
            context.getSource().sendFeedback(CookiesMod.createPrefix(ColorUtils.successColor)
                    .append(Text.literal("Item Id: ").append(String.valueOf(id.hashCode() / 1000000f))
                            .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,
                                    String.valueOf(id.hashCode() / 1000000f)
                            )))));
            return 1;
        }));
    }
}
