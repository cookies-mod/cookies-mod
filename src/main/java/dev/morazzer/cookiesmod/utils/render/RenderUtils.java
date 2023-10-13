package dev.morazzer.cookiesmod.utils.render;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.Optional;

@Slf4j
public class RenderUtils {

    private static final int ALPHA = 0xff000000;

    public static void renderRectangle(DrawContext drawContext, int x, int y, int width, int height, boolean shadow) {
        int main = ALPHA | 0x202026;

        int light = 0xff303036;
        int dark = 0xff101016;

        drawContext.fill(x, y, x + 1, y + height, light); //Left
        drawContext.fill(x + 1, y, x + width, y + 1, light); //Top
        drawContext.fill(x + width - 1, y + 1, x + width, y + height, dark); //Right
        drawContext.fill(x + 1, y + height - 1, x + width - 1, y + height, dark); //Bottom
        drawContext.fill(x + 1, y + 1, x + width - 1, y + height - 1, main); //Middle
        if (shadow) {
            drawContext.fill(x + width, y + 2, x + width + 2, y + height + 2, 0x70000000); //Right shadow
            drawContext.fill(x + 2, y + height, x + width, y + height + 2, 0x70000000); //Bottom shadow
        }
    }

    private static TextRenderer getTextRendererOrNull() {
        return Optional.of(MinecraftClient.getInstance()).map(client -> client.textRenderer).orElse(null);
    }

    public static void renderCenteredTextWithMaxWidth(
            @NotNull DrawContext drawContext, @NotNull Text text, int width, int centerX, int y, int color,
            boolean shadow) {
        TextRenderer textRenderer = getTextRendererOrNull();
        if (textRenderer == null) {
            return;
        }
        float scale = 1.0f;
        int textWidth = textRenderer.getWidth(text);
        if (textWidth > width) {
            scale = width / (float) textWidth;
        }
        int newLength = Math.min(textWidth, width);
        int fontHeight = (int) (textRenderer.fontHeight * scale);
        renderTextScaled(drawContext, text, scale, centerX - newLength / 2, y - fontHeight / 2, color, shadow);
    }

    public static void renderTextWithMaxWidth(
            @NotNull DrawContext drawContext, @NotNull Text text, int width, int x, int y, int color, boolean shadow) {
        TextRenderer textRenderer = getTextRendererOrNull();
        if (textRenderer == null) {
            return;
        }
        int textWidth = textRenderer.getWidth(text);
        float scale = 1.0f;
        if (textWidth > width) {
            scale = width / (float) textWidth;
        }
        renderTextScaled(drawContext, text, scale, x, y, color, shadow);
    }

    public static void renderTextScaled(
            @NotNull DrawContext drawContext, @NotNull Text text, float scaleFactor, int x, int y, int color,
            boolean shadow) {
        TextRenderer textRenderer = getTextRendererOrNull();
        if (textRenderer == null) {
            return;
        }
        drawContext.getMatrices().push();
        drawContext.getMatrices().scale(scaleFactor, scaleFactor, 1);
        drawContext.drawText(textRenderer, text, (int) (x / scaleFactor), (int) (y / scaleFactor), color, shadow);
        drawContext.getMatrices().pop();
    }

    public static void renderTextCenteredScaled(
            @NotNull DrawContext drawContext, @NotNull Text text, float scaleFactor, int x, int y, int color) {
        TextRenderer textRenderer = getTextRendererOrNull();
        if (textRenderer == null) {
            return;
        }
        drawContext.getMatrices().push();
        drawContext.getMatrices().scale(scaleFactor, scaleFactor, 1);
        drawContext.drawCenteredTextWithShadow(
                textRenderer,
                text,
                (int) (x / scaleFactor),
                (int) (y / scaleFactor),
                color
        );
        drawContext.getMatrices().pop();
    }

    public static void renderTextInWorld(
            MatrixStack matrixStack, Vec3d position, Text text, VertexConsumerProvider vertexConsumerProvider,
            float size, boolean center, boolean throughWalls, int color) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        Camera camera = minecraftClient.gameRenderer.getCamera();
        if (!camera.isReady() || minecraftClient.getEntityRenderDispatcher().gameOptions == null) {
            return;
        }
        TextRenderer textRenderer = minecraftClient.textRenderer;
        double d = camera.getPos().x;
        double e = camera.getPos().y;
        double f = camera.getPos().z;
        matrixStack.push();
        matrixStack.translate((float) (position.x - d), (float) (position.y - e) + 0.07f, (float) (position.z - f));
        matrixStack.multiplyPositionMatrix(new Matrix4f().rotation(camera.getRotation()));
        matrixStack.scale(-size, -size, size);
        float g = center ? (float) (-textRenderer.getWidth(text)) / 2.0f : 0.0f;


        float backgroundOpacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f);
        int background = (int)(backgroundOpacity * 255.0f) << 24;

        textRenderer.draw(
                text,
                g,
                0.0f,
                color,
                false,
                matrixStack.peek().getPositionMatrix(),
                vertexConsumerProvider,
                throughWalls ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL,
                background,
                0xF000F0
        );
        matrixStack.pop();
    }
}
