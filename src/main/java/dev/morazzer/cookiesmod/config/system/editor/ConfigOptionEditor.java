package dev.morazzer.cookiesmod.config.system.editor;

import dev.morazzer.cookiesmod.config.system.Option;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

@SuppressWarnings("unused")
public abstract class ConfigOptionEditor<T, O extends Option<T, O>> {

	protected final O option;
	private boolean isDragging;

	public ConfigOptionEditor(O option) {
		this.option = option;
	}

	public int getHeight(int optionWidth) {
		return getHeight();
	}

	public int getHeight() {
		return 45;
	}

	public void init() {

	}

	public void render(DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
		RenderUtils.renderRectangle(drawContext, 0, 0, optionWidth - 2, getHeight(optionWidth) - 2, true);

		RenderUtils.renderCenteredTextWithMaxWidth(
				drawContext,
				this.option.getName(),
				optionWidth / 3 - 10,
				optionWidth / 6 + 2,
				13,
				-1,
				true
		);

		int lineCount = this.getTextRenderer().wrapLines(this.option.getDescription(), optionWidth * 2 / 3 - 10).size();
		if (lineCount == 0) {
			return;
		}


		drawContext.drawTextWrapped(
				this.getTextRenderer(),
				this.option.getDescription(),
				5 + optionWidth / 3,
				getHeight() / 2 - (lineCount * 9) / 2,
				(optionWidth * 2 / 3 - 10),
				~0
		);
	}

	public boolean doesMatchSearch(String search) {
		return this.option.getName().getString().contains(search)
				|| this.option.getDescription().getString().contains(search)
				|| this.option.getHiddenKeys().stream().anyMatch(key -> key.contains(search));
	}

	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return false;
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button, int optionWidth) {
		return false;
	}

	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return false;
	}

	public boolean mouseDragged(
			double doubleX, double mouseY, int button, double deltaX, double deltaY, int optionWidth) {
		return false;
	}

	public boolean isDragging() {
		return isDragging;
	}

	public void setDragging(boolean dragging) {
		isDragging = dragging;
	}

	public void mouseScrolled(double mouseX, double mouseY, double amount) {}

	public void keyReleased(int keyCode, int scanCode, int modifiers) {}


	public void charTyped(char chr, int modifiers) {}

	public void renderOverlay(DrawContext context, int mouseX, int mouseY, float delta, int optionWidth) {

	}

	protected TextRenderer getTextRenderer() {
		return MinecraftClient.getInstance().textRenderer;
	}
}
