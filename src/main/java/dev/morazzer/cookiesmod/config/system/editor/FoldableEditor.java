package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.options.FoldableOption;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

/**
 * Editor that describes a foldable.
 */
@Getter
public class FoldableEditor extends ConfigOptionEditor<Object, FoldableOption> {

    public int foldableId;
    boolean active = true;

    public FoldableEditor(FoldableOption option, int id) {
        super(option);
        this.foldableId = id;
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public void init() {
        this.active = false;
    }

    @Override
    public void render(@NotNull DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
        RenderUtils.renderRectangle(drawContext, 0, 0, optionWidth - 2, getHeight() - 2, true);
        Matrix4f matrix4f = drawContext.getMatrices().peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
        if (this.active) {
            bufferBuilder.vertex(matrix4f, 6, 6, 0).color(-1).next();
            bufferBuilder.vertex(matrix4f, 9.75f, 13.5f, 0).color(-1).next();
            bufferBuilder.vertex(matrix4f, 13.5f, 6, 0).color(-1).next();
        } else {
            bufferBuilder.vertex(matrix4f, 6, 13.5f, 0).color(-1).next();
            bufferBuilder.vertex(matrix4f, 13.5f, 9.75f, 0).color(-1).next();
            bufferBuilder.vertex(matrix4f, 6, 6, 0).color(-1).next();
        }
        tessellator.draw();
        RenderUtils.renderTextWithMaxWidth(
            drawContext,
            this.option.getName(),
            optionWidth - 10,
            18,
            6,
            0xffd0d0d0,
            true
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int optionWidth) {
        if (mouseX >= 0 && mouseX < optionWidth
            && mouseY >= 0 && mouseY < getHeight()) {
            this.active = !this.active;
        }
        return super.mouseClicked(mouseX, mouseY, button, optionWidth);
    }

}
