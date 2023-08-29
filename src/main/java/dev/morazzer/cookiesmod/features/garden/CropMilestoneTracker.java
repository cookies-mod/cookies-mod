package dev.morazzer.cookiesmod.features.garden;

import lombok.Getter;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CropMilestoneTracker {

	Set<Identifier> crops = Stream.of(
			Blocks.WHEAT,
			Blocks.CARROTS,
			Blocks.POTATOES,
			Blocks.NETHER_WART,
			Blocks.SUGAR_CANE,
			Blocks.COCOA,
			Blocks.CACTUS,
			Blocks.RED_MUSHROOM,
			Blocks.BROWN_MUSHROOM,
			Blocks.PUMPKIN,
			Blocks.MELON
	).map(Registries.BLOCK::getId).collect(Collectors.toUnmodifiableSet());


	@Getter
	private static CropMilestoneTracker cropMilestoneTracker;

	public static void initializeCropMilestoneTracker() {
		cropMilestoneTracker = new CropMilestoneTracker();
		PlayerBlockBreakEvents.AFTER.register(cropMilestoneTracker::blockBroken);
		PlayerBlockBreakEvents.CANCELED.register(cropMilestoneTracker::blockBroken);
	}

	private void blockBroken(World world,
	                         PlayerEntity playerEntity,
	                         BlockPos blockPos,
	                         BlockState blockState,
	                         BlockEntity blockEntity) {
		System.out.println("a");
		if (!Garden.isOnGarden()) return;
		Identifier blockId = Registries.BLOCK.getId(blockState.getBlock());

		if (!crops.contains(blockId)) return;

		MinecraftClient.getInstance().player.sendMessage(Text.of(blockId.toString()));
	}
}
