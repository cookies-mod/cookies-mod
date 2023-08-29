package dev.morazzer.cookiesmod.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class RenderUtils {

	private static Vec3d transformWorldToRelative(Vec3d in) {
		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		Vec3d camPos = camera.getPos();
		return in.subtract(camPos);
	}

	public static void drawLineWorldCoordinates(BufferBuilder bufferBuilder, Vec3d start, Vec3d end, Color color) {
		drawLine(bufferBuilder, transformWorldToRelative(start), transformWorldToRelative(end), color);
	}

	public static void drawLine(BufferBuilder bufferBuilder, Vec3d relativeStart, Vec3d relativeEnd, Color color) {
		bufferBuilder.vertex(relativeStart.x, relativeStart.y, relativeStart.z).color(color.getRGB() | color.getAlpha() << 24).next();
		bufferBuilder.vertex(relativeEnd.x, relativeEnd.y, relativeEnd.z).color(color.getRGB() | color.getAlpha() << 24).next();
	}

	public static BufferBuilder createBuffer(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat) {
		Tessellator tessellator = new Tessellator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(drawMode,vertexFormat);
		return bufferBuilder;
	}

	public static void render(BufferBuilder bufferBuilder, boolean throughWalls) {
		startDrawing(throughWalls);
		draw(bufferBuilder);
		stopDrawing();
	}

	public static void startDrawing(boolean throughWalls) {
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.enableDepthTest();
		RenderSystem.depthFunc(throughWalls ? GL11.GL_ALWAYS : GL11.GL_LEQUAL);
	}

	public static void stopDrawing() {
		RenderSystem.enableCull();
		RenderSystem.disableBlend();
		RenderSystem.depthFunc(GL11.GL_ALWAYS);
	}

	public static void draw(BufferBuilder bufferBuilder) {
		BufferRenderer.draw(bufferBuilder.end());
	}
}
