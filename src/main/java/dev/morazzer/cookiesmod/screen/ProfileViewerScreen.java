package dev.morazzer.cookiesmod.screen;

import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

/**
 * Profile viewer to view other players.a
 */
public class ProfileViewerScreen extends Screen {
    private final long openedAt = System.currentTimeMillis();
    double scaleFactorX;
    double scaleFactorY;
    int positionX;
    int positionY;
    int width;
    int height;
    private UUID uuid;
    private String userName;
    private boolean finishedFetching = false;

    /**
     * Create a new profile viewer.
     *
     * @param future If the profile has finished loading.
     */
    public ProfileViewerScreen(CompletableFuture<Void> future) {
        super(Text.of("Profile of "/* + PlayerManager.getUserName(ProfileViewerManager.getLastSearch()) */));
        future.whenComplete((unused, throwable) -> {
            if (throwable != null) {
                ExceptionHandler.handleException(throwable);
                return;
            }

            if (userName == null) {
                return;
            }

            //this.profile = ProfileManager.getProfile(userName);

            this.finishedFetching = true;
        });
    }

    @Override
    protected void init() {
        super.init();
        resize(MinecraftClient.getInstance(), super.width, super.height);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.positionX = (int) ((width / 4f) * ((5 - client.getWindow().getScaleFactor()) / 4));
        this.positionY = (int) ((height / 4f) * ((5 - client.getWindow().getScaleFactor()) / 4));
        this.scaleFactorX = ((client.getWindow().getScaleFactor() - 1) / 4);
        this.scaleFactorY = ((client.getWindow().getScaleFactor() - 1) / 4);
        this.width = width / 2;
        this.height = height / 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Creating a matrix to not use screen coordinates in the profile viewer
        // this allows for scalability with the ui scale without any bugs
        context.getMatrices().push();
        context.getMatrices().translate(this.positionX, this.positionY, 1);
        context.getMatrices().scale((float) (1 + this.scaleFactorX), (float) (1 + this.scaleFactorY), 1);

        this.renderBackground(context, mouseX, mouseY, delta);
        this.renderMainMenu(context);

        context.getMatrices().pop();
    }

    private void renderMainMenu(DrawContext context) {
        if (!this.finishedFetching) {
            long timeOpen = System.currentTimeMillis() - this.openedAt;

            int dots = (int) ((timeOpen / 300) % 4);
            int testLength = super.textRenderer.getWidth("Loading profiles");
            Text dottedText = Text.literal("Loading profiles").append(StringUtils.repeat(".", dots))
                .styled(style -> style.withColor(0xFFFF00FF));

            context.drawTextWithShadow(super.textRenderer, dottedText, this.width / 2 - testLength / 2, this.height / 2,
                0xFF << 24);
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, this.finishedFetching ? 0xFFD4D4D4 : ~0 ^ 0x5f5f5f);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
