package dev.morazzer.cookiesmod.features.hud;

import dev.morazzer.cookiesmod.utils.render.Position;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

@Getter
public abstract class HudElement {
	private static final Identifier HUD_NAMESPACE = new Identifier("cookiesmod", "hud/");

	public HudElement(Position position) {
		this.position = position;
	}

	public void init() {}

	private Position position;
	private boolean enabled;

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract String getIdentifierPath();

	public abstract boolean shouldRender();

	public final int getX() {
		return this.position.getFixedX(this.getWidth());
	}

	public final int getY() {
		return this.position.getFixedY(this.getHeight());
	}

	public Identifier getIdentifier() {
		return HUD_NAMESPACE.withSuffixedPath(getIdentifierPath());
	}

	public void renderWithTests(DrawContext drawContext, float delta) {
		if (!(this.enabled && shouldRender())) {
			return;
		}
		render(drawContext, delta);
	}


	public void render(DrawContext drawContext, float delta) {
		drawContext.getMatrices().push();
		drawContext.getMatrices().translate(position.getFixedX(getWidth()), position.getFixedY(getHeight()), 1);
		renderOverlay(drawContext, delta);
		drawContext.getMatrices().pop();
	}

	protected abstract void renderOverlay(DrawContext drawContext, float delta);

	public void toggle(boolean value) {
		this.enabled = value;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}
