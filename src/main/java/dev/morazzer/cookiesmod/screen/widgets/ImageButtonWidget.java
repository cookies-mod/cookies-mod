package dev.morazzer.cookiesmod.screen.widgets;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

@Getter
@Setter
public class ImageButtonWidget {
	private int height;
	private Text message;
	private Consumer<ImageButtonWidget> onPress;
	private Identifier background;
	private int x;
	private int y;
	private int width;

	public ImageButtonWidget(int x, int y, int width, int height, Text message, Consumer<ImageButtonWidget> onPress, Identifier background) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.message = message;
		this.onPress = onPress;
		this.background = background;
	}


	protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
		boolean fileFound = true;
		try {
			MinecraftClient.getInstance().getTextureManager().getTexture(this.background);
		} catch (Exception e) {
			fileFound = false;
		}

		if (fileFound) {
			context.drawTexture(background, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
		} else {
			context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, ~0);
		}
	}

	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderButton(context, mouseX, mouseY, delta);
	}

	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (mouseX >= this.x && mouseX <= this.x + this.width
				&& mouseY >= this.y && mouseY <= this.y + this.height) {
			if (button != 0) return;
			onPress.accept(this);
		}
	}

}
