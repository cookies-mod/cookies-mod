package dev.morazzer.cookiesmod.commands.dev.subcommands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.commands.arguments.RealIdentifierArgument;
import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItem;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItemManager;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.ConcurrentUtils;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.TextUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.util.Identifier;

import java.util.Base64;
import java.util.Objects;

@DevSubcommand
public class RepoSubcommand extends ClientCommand {
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("repo").then(literal("reload").executes(context -> {
            long start = System.currentTimeMillis();
            context.getSource()
                    .sendFeedback(CookiesMod.createPrefix(ColorUtils.successColor).append("Reloading repository"));
            RepositoryManager.reload();
            context.getSource().sendFeedback(CookiesMod.createPrefix(ColorUtils.successColor)
                    .append("Reloaded repository in %sms".formatted(System.currentTimeMillis() - start)));

            return 1;
        })).then(literal("items").then(literal("create_from_api").executes(context -> {

            ConcurrentUtils.execute(() -> {
                boolean succeeded = RepositoryItemManager.loadOfficialItemList();
                if (!succeeded) {
                    context.getSource().sendError(CookiesMod.createPrefix(ColorUtils.failColor)
                            .append("Failed to export default items"));
                    return;
                }

                context.getSource().sendFeedback(CookiesMod.createPrefix(ColorUtils.successColor)
                        .append("Successfully exported default items"));

            });

            return Command.SINGLE_SUCCESS;
        })).then(literal("give").then(argument("item",
                new RealIdentifierArgument(RepositoryItemManager.getAllItems())
        ).executes(context -> {
            if (context.getSource().getEntity() instanceof PlayerEntity player && !player.isCreative()) {
                context.getSource().sendError(CookiesMod.createPrefix(ColorUtils.failColor)
                        .append("You have to be in creative to use this command"));
                return 0;
            }

            RepositoryItem item = RepositoryItemManager.getItem(context.getArgument("item", Identifier.class));

            ItemStack itemStack = item.getItemStack().copy();

            context.getSource().sendFeedback(CookiesMod.createPrefix(ColorUtils.successColor).append("Gave 1 [")
                    .append(itemStack.getName().copy()
                            .styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM,
                                    new HoverEvent.ItemStackContent(itemStack)
                            )))).append("] to %s".formatted(context.getSource().getEntity().getEntityName())));

            Objects.requireNonNull(MinecraftClient.getInstance().player).giveItemStack(itemStack);


            return Command.SINGLE_SUCCESS;
        }))).then(literal("get_skull_texture").then(argument("item",
                new RealIdentifierArgument(RepositoryItemManager.getAllItems())
        ).executes(context -> {
            if (context.getSource().getEntity() instanceof PlayerEntity player && !player.isCreative()) {
                context.getSource().sendError(CookiesMod.createPrefix(ColorUtils.failColor)
                        .append("You have to be in creative to use this command"));
                return 0;
            }

            RepositoryItem item = RepositoryItemManager.getItem(context.getArgument("item", Identifier.class));
            if (item.getSkin().isPresent()) {
                String s = new String(Base64.getDecoder().decode(item.getSkin().get()));
                JsonObject jsonObject = GsonUtils.gsonClean.fromJson(s, JsonObject.class);
                context.getSource().sendFeedback(TextUtils.prettyPrintJson(jsonObject));
            } else {
                context.getSource().sendFeedback(CookiesMod.createPrefix(ColorUtils.failColor).append("Item does not have a skin"));
            }

            return Command.SINGLE_SUCCESS;
        })))).requires(context -> ConfigManager.getConfig().devCategory.displayRepoOption.getValue());
    }
}
