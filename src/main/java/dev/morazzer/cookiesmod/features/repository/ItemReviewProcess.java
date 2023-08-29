package dev.morazzer.cookiesmod.features.repository;

import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.features.repository.items.RepositoryItem;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Getter
public class ItemReviewProcess implements Runnable {
	@Getter
	private static ItemReviewProcess itemReviewProcess;
	private int done;
	private WatchService watchService;
	private WatchKey watchKey;
	private final Iterator<Identifier> items;
	private final Thread monitorThread;
	private Identifier currentItem;

	public ItemReviewProcess() {
		this.items = RepositoryManager.getAllItems().iterator();
		this.monitorThread = new Thread(this);
		this.monitorThread.start();
		itemReviewProcess = this;
	}

	private void register(Path path) throws Exception {
		this.watchService = FileSystems.getDefault().newWatchService();

		path.register(this.watchService, StandardWatchEventKinds.ENTRY_MODIFY);

		this.watchKey = this.watchService.take();
		this.startWatcher(path);
	}

	public boolean skip(int i) {
		for (int j = 0; j < i - 1; j++) {
			if (!this.getItems().hasNext()) return true;
			done++;
			this.getItems().next();
		}
		return next();
	}

	@SuppressWarnings("unchecked")
	private void startWatcher(Path path) {
		do {
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				if (StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind())) {
					WatchEvent<Path> modifyEvent = (WatchEvent<Path>) event;
					Path file = modifyEvent.context();

					Optional<ClientPlayerEntity> player = Optional.ofNullable(MinecraftClient.getInstance().player);

					player.ifPresent(p -> MinecraftClient.getInstance().send(() -> p.sendMessage(CookiesMod.createPrefix().append("Detected file change %s, reloading item".formatted(file.getFileName().toString())))));
					log.info("Detected file change {}", file.toString());

					MinecraftClient.getInstance().executeSync(() -> {
						boolean b = RepositoryManager.loadItem(path.resolve(file.getFileName()), identifier -> {
							if (identifier.equals(this.currentItem)) {
								giveItem(RepositoryManager.getItem(identifier));
							}
						});
						String message;
						if (b) {
							message = "Successfully reloaded item %s";
						} else {
							message = "Failed reloading item %s";
						}

						message = message.formatted(file.getFileName().toString());

						log.info(message);
						String finalMessage = message;
						player.ifPresent(p -> p.sendMessage(CookiesMod.createPrefix().append(finalMessage)));
					});
				}
			}
		} while (items.hasNext());
	}

	public boolean next() {
		done++;
		if (!items.hasNext()) {
			return true;
		}


		Identifier next = items.next();

		if (next.getPath().toLowerCase().contains("_generator")) {
			Objects.requireNonNull(MinecraftClient.getInstance().player)
					.sendMessage(CookiesMod
							.createPrefix(ColorUtils.successColor)
							.append(Text.literal("Skipping minion!")));
			return next();
		}

		RepositoryItem item = RepositoryManager.getItem(next);
		this.currentItem = item.getSkyblockId();

		giveItem(item);

		Objects.requireNonNull(MinecraftClient.getInstance().player)
				.sendMessage(CookiesMod
						.createPrefix(ColorUtils.successColor)
						.append(Text.literal("Click here to open the file (%s/%s)".formatted(done, RepositoryManager.getAllItems().size()))
								.styled(style -> style.withClickEvent(
												new ClickEvent(
														ClickEvent.Action.SUGGEST_COMMAND,
														RepositoryManager.getRepoRoot().resolve(item.getSkyblockId().getPath().substring(5) + ".json").toAbsolutePath().toString()
												)
										)
								)
						)
				);


		return false;
	}

	private void giveItem(RepositoryItem item) {
		ItemStack value = item.getItemStack().getValue();

		Objects.requireNonNull(MinecraftClient.getInstance().player).getInventory().clear();

		Objects.requireNonNull(MinecraftClient.getInstance().player).giveItemStack(value);

		Objects.requireNonNull(MinecraftClient.getInstance().player)
				.sendMessage(
						CookiesMod
								.createPrefix(ColorUtils.successColor)
								.append("Gave 1 [")
								.append(
										value.getName()
												.copy()
												.styled(style -> style
														.withHoverEvent(
																new HoverEvent(
																		HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(value))))
								).append("] to %s".formatted(MinecraftClient.getInstance().player.getEntityName())));
	}

	@Override
	public void run() {
		Path items = RepositoryManager.getRepoRoot().resolve("items");
		try {
			register(items);
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
		}
	}

}
