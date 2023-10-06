package dev.morazzer.cookiesmod.utils.render;

import net.minecraft.client.MinecraftClient;

public record Position(double x, double y, boolean centeredX, boolean centeredY) {

    public Position(double x, double y) {
        this(x, y, false, false);
    }

    public int getFixedX(int objectWidth) {
        float width = MinecraftClient.getInstance().getWindow().getScaledWidth();
        if (this.centeredX) {
            return (int) Math.max(0, Math.min(width - objectWidth, width / 2 + x - (double) objectWidth / 2));
        }

        return (int) Math.max(0, Math.min(width - objectWidth, x));
    }

    public int getFixedY(int objectHeight) {
        float height = MinecraftClient.getInstance().getWindow().getScaledHeight();
        if (this.centeredY) {
            return (int) Math.max(0, Math.min(height - objectHeight, height / 2 + y - (double) objectHeight / 2));
        }
        return (int) Math.max(0, Math.min((height - objectHeight), y));
    }

    public Position add(double x, double y) {
        return new Position(this.x + x, this.y + y, this.centeredX, this.centeredY);
    }
}
