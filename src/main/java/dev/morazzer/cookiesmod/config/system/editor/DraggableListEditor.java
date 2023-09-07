package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.options.DraggableListOption;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class DraggableListEditor extends ConfigOptionEditor<List<String>, DraggableListOption> {
	public DraggableListEditor(DraggableListOption option) {
		super(option);
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
		drawContext.fill(0,0,optionWidth,this.getHeight(),0xFFFFFF00);
	}
}
