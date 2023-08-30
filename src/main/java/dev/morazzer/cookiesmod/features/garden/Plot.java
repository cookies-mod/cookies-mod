package dev.morazzer.cookiesmod.features.garden;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public enum Plot {

	BARN,
	INNER_EDGE_PLOT,
	INNER_CORNER_PLOT,
	OUTER_EDGE_PLOT,
	OUTER_CORNER_PLOT,
	NONE;

	public static Plot getCurrentPlot() {
		return Optional.ofNullable(MinecraftClient.getInstance().player).map(Entity::getPos).map(Plot::getPlotFromRealCoordinate).orElse(Plot.NONE);
	}

	public static Plot getPlotFromRealCoordinate(Vec3d position) {
		return getPlotFromPlotCoordinate(
				(int) (changeToPlotCenter(Math.abs(position.x)) + 48) / 96,
				(int) (changeToPlotCenter(Math.abs(position.z)) + 48) / 96
		);
	}

	public static Plot getPlotFromPlotCoordinate(int x, int z) {
		return getPlotFromAbsolutePlotCoordinate(Math.abs(x), Math.abs(z));
	}

	private static Plot getPlotFromAbsolutePlotCoordinate(int absoluteX, int absoluteY) {
		if (absoluteX == 2 && absoluteY == 2) {
			return OUTER_CORNER_PLOT;
		} else if (absoluteX == 1 && absoluteY == 1) {
			return INNER_CORNER_PLOT;
		} else if (Math.min(absoluteX, absoluteY) == 0 && Math.max(absoluteX, absoluteY) == 1) {
			return INNER_EDGE_PLOT;
		} else if (absoluteX + absoluteY == 0) {
			return BARN;
		} else if (absoluteX <= 2 && absoluteY <= 2) {
			return OUTER_EDGE_PLOT;
		} else {
			return NONE;
		}
	}

	private static double changeToPlotCenter(double coordinate) {
		return coordinate - coordinate % 48;
	}

	private static double changeToPlotCorner(double coordinate) {
		return coordinate - Math.abs(coordinate % 96);
	}


	public Vec3d getPlotCenter(Vec3d position) {
		return new Vec3d(changeToPlotCorner(position.x + 240) - 192, 0, changeToPlotCorner(position.z + 240) - 192);
	}

	public boolean isValidPlot() {
		return ordinal() != 5;
	}

	public boolean isInnerCircle() {
		return ordinal() == 1 || ordinal() == 2;
	}

	public boolean isOuterCircle() {
		return ordinal() == 3 || ordinal() == 4;
	}

	public boolean isBarn() {
		return ordinal() == 0;
	}

	public boolean isEdge() {
		return ordinal() == 1 || ordinal() == 3;
	}

	public boolean isCorner() {
		return ordinal() == 2 || ordinal() == 4;
	}
}