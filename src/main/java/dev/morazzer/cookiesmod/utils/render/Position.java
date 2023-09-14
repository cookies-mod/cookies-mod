package dev.morazzer.cookiesmod.utils.render;

import net.minecraft.client.MinecraftClient;

public record Position(double x, double y) {
	public int getFixedX(int objectWidth) {
		float width = MinecraftClient.getInstance().getWindow().getScaledWidth();
		return (int) Math.max(0, Math.min(width - objectWidth, x));
	}

	public int getFixedY(int objectHeight) {
		float height = MinecraftClient.getInstance().getWindow().getScaledHeight();
		return (int) Math.max(0, Math.min((height - objectHeight), y));
	}

	public Position add(double x, double y) {
		return new Position(this.x + x, this.y + y);
	}
}
