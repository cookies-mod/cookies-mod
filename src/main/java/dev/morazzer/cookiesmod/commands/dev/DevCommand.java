package dev.morazzer.cookiesmod.commands.dev;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.commands.arguments.RealIdentifierArgument;
import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import dev.morazzer.cookiesmod.commands.helpers.Helper;
import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.features.garden.Garden;
import dev.morazzer.cookiesmod.features.repository.ItemReviewProcess;
import dev.morazzer.cookiesmod.features.repository.RepositoryManager;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItem;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.ConcurrentUtils;
import dev.morazzer.cookiesmod.utils.DevUtils;
import dev.morazzer.cookiesmod.utils.StringUtils;
import dev.morazzer.cookiesmod.utils.general.ScoreboardUtils;
import dev.morazzer.cookiesmod.utils.general.SkyblockUtils;
import me.x150.renderer.render.Renderer3d;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

public class DevCommand extends ClientCommand {
	@NotNull
	@Override
	public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
		return Helper.literal("dev")
				.then(
						Helper.literal("enable")
								.then(
										Helper.argument(
												"tool",
												new RealIdentifierArgument(DevUtils.getDisabledTools())
										)
								).executes(
										context -> {
											Identifier identifier = context.getArgument("tool", Identifier.class);
											boolean enable = DevUtils.enable(identifier);
											if (!enable) {
												context.getSource()
														.sendError(
																CookiesMod.createPrefix(ColorUtils.failColor)
																		.append("No devtool found with name ")
																		.append(identifier.toString())
														);
												return 0;
											}

											context.getSource().sendFeedback(
													CookiesMod.createPrefix(ColorUtils.successColor)
															.append("Enabled devtool ")
															.append(identifier.toString())
											);
											return Command.SINGLE_SUCCESS;
										}
								)
				).then(
						Helper.literal("disable")
								.then(
										Helper.argument(
												"tool",
												new RealIdentifierArgument(DevUtils.getDisabledTools())
										)
								).executes(
										context -> {
											Identifier identifier = context.getArgument("tool", Identifier.class);
											boolean disabled = DevUtils.disable(identifier);
											if (!disabled) {
												context.getSource()
														.sendError(
																CookiesMod.createPrefix(ColorUtils.failColor)
																		.append("No devtool found with name")
																		.append(identifier.toString())
														);
												return 0;
											}

											context.getSource().sendFeedback(
													CookiesMod.createPrefix(ColorUtils.successColor)
															.append("Disabled devtool ")
															.append(identifier.toString())
											);

											return Command.SINGLE_SUCCESS;
										}
								)
				).then(
                        /*

                         /dev repo
                          |- display
                          |  |- chests
                          |  \- screen
                          |- download
                          |  \-

                         */

						Helper.literal("repo")
								.then(Helper.literal("reload")
										.executes(context -> {
											long start = System.currentTimeMillis();
											context.getSource()
													.sendFeedback(CookiesMod.createPrefix(ColorUtils.successColor)
															.append("Reloading item repository"));
											RepositoryManager.reloadItems();
											context.getSource()
													.sendFeedback(CookiesMod.createPrefix(ColorUtils.successColor)
															.append("Reloaded item repository in %sms".formatted(System.currentTimeMillis() - start)));

											return 1;
										})
								).then(Helper.literal("display")
										.then(
												Helper.literal("screen")
														.executes(context -> Command.SINGLE_SUCCESS)
										)
								).then(Helper.literal("download")
								).then(Helper.literal("items")
										.then(Helper.literal("try_load_clipboard")
												.executes(context -> {
													System.out.println(String.join(", ", REPLACEMENT_CHARS));

													String clipboard = MinecraftClient.getInstance().keyboard.getClipboard();

													String text = getString(Pattern.compile("<span class=\"color-(black|dark_blue|dark_green|dark_aqua|dark_red|dark_purple|gold|gray|dark_gray|blue|green|aqua|red|light_purple|yellow|white)\">(.*?)</span>"), clipboard);

													System.out.println(text);
													MinecraftClient.getInstance().keyboard.setClipboard(text);

													return Command.SINGLE_SUCCESS;
												}))
										.then(Helper.literal("apply_large")
												.then(Helper.argument("file", StringArgumentType.string())
														.then(Helper.argument("regex", StringArgumentType.string())
																.executes(context -> Command.SINGLE_SUCCESS))))

										.then(Helper.literal("review")
												.executes(context -> {
													context.getSource().sendFeedback(CookiesMod.createPrefix().append("Starting review..."));
													new ItemReviewProcess();
													ItemReviewProcess.getItemReviewProcess().next();

													return Command.SINGLE_SUCCESS;
												})
												.then(Helper.literal("skip")
														.then(Helper.argument("skip", IntegerArgumentType.integer(1, RepositoryManager.getAllItems().size()))
																.executes(context -> {
																	if (ItemReviewProcess.getItemReviewProcess() == null) {
																		context.getSource().sendFeedback(
																				CookiesMod.createPrefix(ColorUtils.failColor)
																						.append("No review process is running")
																		);
																		return 0;
																	}

																	Integer skip = context.getArgument("skip", Integer.class);

																	boolean done = ItemReviewProcess.getItemReviewProcess().skip(skip);

																	if (done) {
																		context.getSource().sendFeedback(
																				CookiesMod.createPrefix().append("Finished item review!")
																		);
																	} else {
																		context.getSource().sendFeedback(
																				CookiesMod.createPrefix().append("Skipped %s".formatted(skip))
																		);
																	}

																	return Command.SINGLE_SUCCESS;
																})
														)
												).then(Helper.literal("next")
														.executes(context -> {
															if (ItemReviewProcess.getItemReviewProcess() == null) {
																context.getSource().sendFeedback(
																		CookiesMod.createPrefix(ColorUtils.failColor)
																				.append("No review process is running")
																);
																return 0;
															}

															boolean done = ItemReviewProcess.getItemReviewProcess().next();

															if (done) {
																context.getSource().sendFeedback(
																		CookiesMod.createPrefix().append("Finished item review!")
																);
																return 1;
															} else {
																context.getSource().sendFeedback(
																		CookiesMod.createPrefix().append("Proceeding with next item")
																);
															}

															return Command.SINGLE_SUCCESS;
														})
												)
										).then(Helper.literal("merge_all")
												.executes(context -> {
													ConcurrentUtils.execute(() -> {
														Path path = RepositoryManager.mergeAllItems();

														context.getSource().sendFeedback(Text.literal("Click here to open merged items").styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, path.toAbsolutePath().toString()))));
													});
													return Command.SINGLE_SUCCESS;
												})
										).then(Helper.literal("create_from_api")
												.executes(context -> {

													ConcurrentUtils.execute(() -> {
														boolean succeeded = RepositoryManager.loadOfficialItemList();
														if (!succeeded) {
															context.getSource().sendError(
																	CookiesMod.createPrefix(ColorUtils.failColor)
																			.append("Failed to export default items")
															);
															return;
														}

														context.getSource().sendFeedback(
																CookiesMod.createPrefix(ColorUtils.successColor)
																		.append("Successfully exported default items")
														);

													});

													return Command.SINGLE_SUCCESS;
												})
										).then(Helper.literal("give")
												.then(Helper.argument("item", new RealIdentifierArgument(RepositoryManager.getAllItems()))
														.executes(context -> {
															if (context.getSource().getEntity() instanceof PlayerEntity player && !player.isCreative()) {
																context.getSource()
																		.sendError(
																				CookiesMod.createPrefix(ColorUtils.failColor)
																						.append("You have to be in creative to use this command")
																		);
																return 0;
															}

															RepositoryItem item = RepositoryManager.getItem(context.getArgument("item", Identifier.class));

															ItemStack itemStack = item.getItemStack().getValue().copy();

															context.getSource()
																	.sendFeedback(
																			CookiesMod.createPrefix(ColorUtils.successColor)
																					.append("Gave 1 [").append(
																							itemStack.getName()
																									.copy().styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(itemStack))))
																					).append("] to %s".formatted(context.getSource().getEntity().getEntityName()))
																	);

															Objects.requireNonNull(MinecraftClient.getInstance().player).giveItemStack(itemStack);


															return Command.SINGLE_SUCCESS;
														})
												)
										)
								)
								.requires(context -> ConfigManager.getConfig().devCategory.displayRepoOption)
				).then(Helper.literal("test")
						.then(Helper.argument("test_description", StringArgumentType.string())
								.executes(context -> {
									String argument = context.getArgument("test_description", String.class);

									switch (argument) {
										case "debug_location_timings" -> {
											long[] timings = new long[10];
											for (int i = 0; i < 10; i++) {
												long time = System.nanoTime();
												ScoreboardUtils.getAllLines();
												timings[i] = System.nanoTime() - time;
											}

											ArrayList<Text> list = new ArrayList<>();
											list.add(CookiesMod.createColor().append("-------------------------------------"));
											list.add(CookiesMod.createColor().append("Timings of #getCurrentLocation"));
											list.add(CookiesMod.createColor().append("-------------------------------------"));


											for (int i = 0; i < timings.length; i++) {
												list.add(CookiesMod.createColor().append("" + i).append(" | ").append(Duration.ofNanos(timings[i]).toString()));
											}
											list.add(CookiesMod.createColor().append("-------------------------------------"));

											list.add(CookiesMod.createColor().append("Average timings: ")
													.append(Duration.ofNanos((long) LongStream.of(timings).average().orElse(0)).toString()));

											timings[0] = 0;

											list.add(CookiesMod.createColor().append("Average timings (skipping first): ")
													.append(Duration.ofNanos((long) LongStream.of(timings).average().orElse(0)).toString()));

											list.add(CookiesMod.createColor().append("-------------------------------------"));
											list.forEach(MinecraftClient.getInstance().player::sendMessage);
										}
										case "is_in_skyblock" ->
												MinecraftClient.getInstance().player.sendMessage(CookiesMod.createPrefix().append("" + SkyblockUtils.isCurrentlyInSkyblock()));
										case "enable_garden_features" -> Garden.loadGardenFeatures();
										case "is_on_garden" ->
												MinecraftClient.getInstance().player.sendMessage(CookiesMod.createPrefix().append("" + Garden.isOnGarden()));
									}

									return Command.SINGLE_SUCCESS;
								})
						)
						.then(Helper.literal("toggle_render_tests")
								.executes(context -> {

									WorldRenderEvents.BEFORE_DEBUG_RENDER.register(worldRenderContext -> {
										Renderer3d.renderThroughWalls();
										Renderer3d.renderFilled(worldRenderContext.matrixStack(), Color.GRAY, new Vec3d(0, 0, 0), new Vec3d(1, 1, 1));
										Renderer3d.stopRenderThroughWalls();
										Renderer3d.renderOutline(worldRenderContext.matrixStack(), Color.BLACK, new Vec3d(0, 0, 0), new Vec3d(1, 1, 1));
									});

									return Command.SINGLE_SUCCESS;
								}))
				);
	}

	static String[] REPLACEMENT_CHARS;

	static {
		REPLACEMENT_CHARS = new String[128];
		for (int i = 0; i <= 0x1f; i++) {
			REPLACEMENT_CHARS[i] = String.format("\\u%04x", i);
		}
		REPLACEMENT_CHARS['"'] = "\"";
		REPLACEMENT_CHARS['\\'] = "\\";
		REPLACEMENT_CHARS['\t'] = "\t";
		REPLACEMENT_CHARS['\b'] = "\b";
		REPLACEMENT_CHARS['\n'] = "\n";
		REPLACEMENT_CHARS['\r'] = "\r";
		REPLACEMENT_CHARS['\f'] = "\f";
	}

	@NotNull
	private static String getString(Pattern pattern, String clipboard) {
		Matcher matcher = pattern.matcher(clipboard);

		StringBuilder text = new StringBuilder();

		while (matcher.find()) {
			String color = matcher.group(1);
			String content = matcher.group(2);
			boolean italic = false, bold = false;

			if (content.contains("<i>")) {
				italic = true;
				content = content.replaceAll("</?i>", "");
			}
			if (content.contains("<b>")) {
				bold = true;
				content = content.replaceAll("</?b>", "");
			}


			String tempText = "{\"text\":\"%s\",\"color\":\"%s\",".formatted(content, color);

			if (italic) {
				tempText += "\"italic\":true,";
			}
			if (bold) {
				tempText += "\"bold\":true,";
			}
			tempText = tempText.substring(0, tempText.length() - 1);

			tempText += "},\n";
			text.append(StringUtils.escapeUnicode(tempText));
		}
		return text.toString();
	}
}
