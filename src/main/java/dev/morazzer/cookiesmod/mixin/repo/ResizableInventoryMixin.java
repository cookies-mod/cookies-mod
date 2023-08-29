package dev.morazzer.cookiesmod.mixin.repo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ResizableInventoryMixin {
	@Shadow
	@Nullable
	protected MinecraftClient client;

	@Shadow
	public int width;

	@Shadow
	public int height;
	@Shadow
	protected TextRenderer textRenderer;

	@Inject(method = "resize", at = @At("INVOKE"))
	public void onResize(MinecraftClient client, int width, int height, CallbackInfo ci) {

	}
}
