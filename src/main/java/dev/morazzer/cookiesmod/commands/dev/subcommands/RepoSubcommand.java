package dev.morazzer.cookiesmod.commands.dev.subcommands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.commands.arguments.RealIdentifierArgument;
import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItemManager;
import dev.morazzer.cookiesmod.features.repository.items.item.SkyblockItem;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.TextUtils;
import dev.morazzer.cookiesmod.utils.json.JsonUtils;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Subcommand to execute various operations related to the
 * {@linkplain dev.morazzer.cookiesmod.features.repository.RepositoryManager}. <br> These include the following:
 * {@code reload}, {@code give item}, {@code get texture}.
 */
@DevSubcommand
public class RepoSubcommand extends ClientCommand {

    @Override
    @NotNull
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("repo").then(literal("reload").executes(context -> {
            long start = System.currentTimeMillis();
            context
                .getSource()
                .sendFeedback(CookiesMod.createPrefix(ColorUtils.successColor).append("Reloading repository"));
            RepositoryManager.reload();
            context
                .getSource()
                .sendFeedback(CookiesMod
                    .createPrefix(ColorUtils.successColor)
                    .append("Reloaded repository in %sms".formatted(System.currentTimeMillis() - start)));

            return 1;
        })).then(literal("items")
            .then(literal("give").then(argument(
                "item",
                new RealIdentifierArgument(RepositoryItemManager.getAllItems(), "skyblock", "items/")
            ).executes(context -> {
                if (context.getSource().getEntity() instanceof PlayerEntity player && !player.isCreative()) {
                    context
                        .getSource()
                        .sendError(CookiesMod
                            .createPrefix(ColorUtils.failColor)
                            .append("You have to be in creative to use this command"));
                    return 0;
                }

                Optional<SkyblockItem> item =
                    RepositoryItemManager.getItem(context.getArgument("item", Identifier.class));
                if (item.isEmpty()) {
                    context
                        .getSource()
                        .sendError(CookiesMod
                            .createPrefix(ColorUtils.failColor)
                            .append("Could not find item"));
                    return 0;
                }

                ItemStack itemStack = item.get().getItemStack().copy();

                context
                    .getSource()
                    .sendFeedback(CookiesMod
                        .createPrefix(ColorUtils.successColor)
                        .append("Gave 1 [")
                        .append(itemStack
                            .getName()
                            .copy()
                            .styled(style -> style.withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_ITEM,
                                new HoverEvent.ItemStackContent(itemStack)
                            ))))
                        .append("] to %s".formatted(context.getSource().getEntity().getEntityName())));

                Objects.requireNonNull(MinecraftClient.getInstance().player).giveItemStack(itemStack);


                return Command.SINGLE_SUCCESS;
            })))
            .then(literal("get_skull_texture").then(argument(
                "item",
                new RealIdentifierArgument(RepositoryItemManager.getAllItems(), "skyblock", "items/")
            ).executes(context -> {
                if (context.getSource().getEntity() instanceof PlayerEntity player && !player.isCreative()) {
                    context
                        .getSource()
                        .sendError(CookiesMod
                            .createPrefix(ColorUtils.failColor)
                            .append("You have to be in creative to use this command"));
                    return 0;
                }

                var item = RepositoryItemManager.getItem(context.getArgument("item", Identifier.class));
                if (item.isEmpty()) {
                    context
                        .getSource()
                        .sendError(CookiesMod
                            .createPrefix(ColorUtils.failColor)
                            .append("Could not find item"));
                    return 0;
                }
                if (item.get().getSkin().isPresent()) {
                    String s = new String(Base64.getDecoder().decode(item.get().getSkin().get()));
                    JsonObject jsonObject = JsonUtils.CLEAN_GSON.fromJson(s, JsonObject.class);
                    context.getSource().sendFeedback(TextUtils.prettyPrintJson(jsonObject));
                } else {
                    context
                        .getSource()
                        .sendFeedback(CookiesMod
                            .createPrefix(ColorUtils.failColor)
                            .append("Item does not have a skin"));
                }

                return Command.SINGLE_SUCCESS;
            })))).requires(context -> ConfigManager.getConfig().devCategory.displayRepoOption.getValue());
    }

}
