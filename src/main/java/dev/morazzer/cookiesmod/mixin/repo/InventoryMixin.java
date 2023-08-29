package dev.morazzer.cookiesmod.mixin.repo;

import dev.morazzer.cookiesmod.screen.widgets.ImageButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(HandledScreen.class)
public abstract class InventoryMixin extends Screen {

	@Shadow
	protected int x;

	@Shadow
	protected int y;
	@Shadow
	protected int backgroundWidth;

	protected InventoryMixin(Text title) {
		super(title);
	}

	@Shadow
	protected abstract void drawItem(DrawContext context, ItemStack stack, int x, int y, String amountText);

	@Shadow
	protected abstract List<Text> getTooltipFromItem(ItemStack stack);

	@Unique
	private List<Identifier> cookies$items;
	@Unique
	private int cookies$page = 0;

	@Unique
	private ImageButtonWidget cookies$right;

	@Unique
	private ImageButtonWidget cookies$left;


	@Inject(method = "init", at = @At("RETURN"))
	public void init(CallbackInfo ci) {

	}

	@Unique
	int cookies$itemListStartX = 0;
	@Unique
	int cookies$itemListStartY = 30;
	@Unique
	int cookies$itemListWidth = 1;
	@Unique
	int cookies$itemListHeight = 1;

	@Unique
	int cookies$itemsPerRow = 1;
	@Unique
	int cookies$itemsPerColumn = 1;
	@Unique
	int cookies$itemsOnPage = 1;

	@Unique
	final int cookies$itemSize = 18;

	@Inject(method = "render", at = @At("RETURN"))
	public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {

	}

	public void onResize(MinecraftClient client, int width, int height, CallbackInfo ci) {

	}

	@Inject(method = "mouseClicked", at = @At("RETURN"))
	public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
	}
}
