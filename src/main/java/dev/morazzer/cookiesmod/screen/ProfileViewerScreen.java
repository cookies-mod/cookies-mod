package dev.morazzer.cookiesmod.screen;

import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.features.PlayerManager;
import dev.morazzer.cookiesmod.features.ProfileViewerManager;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ProfileViewerScreen extends Screen {
    private UUID uuid;
    private String userName;
    private JsonObject profile;
    private boolean finishedFetching = false;
    private final long openedAt = System.currentTimeMillis();

    public ProfileViewerScreen(CompletableFuture<Void> future) {
        super(Text.of("Profile of "/* + PlayerManager.getUserName(ProfileViewerManager.getLastSearch()) */));
        future.whenComplete((unused, throwable) -> {
            if (throwable != null) {
                ExceptionHandler.handleException(throwable);
                return;
            }

            this.uuid = ProfileViewerManager.getLastSearch();
            this.userName = PlayerManager.getUserName(uuid);

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

    double scaleFactorX;
    double scaleFactorY;
    int x;
    int y;
    int width;
    int height;

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.x = (int) ((width / 4f) * ((5 - client.getWindow().getScaleFactor()) / 4));
        this.y = (int) ((height / 4f) * ((5 - client.getWindow().getScaleFactor()) / 4));
        this.scaleFactorX = ((client.getWindow().getScaleFactor() - 1) / 4);
        this.scaleFactorY = ((client.getWindow().getScaleFactor() - 1) / 4);
        this.width = width / 2;
        this.height = height / 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Creating a matrix to not use screen coordinates in the pv, this allows for scalability with the ui scale without any bugs
        context.getMatrices().push();
        context.getMatrices().translate(this.x, this.y, 1);
        context.getMatrices().scale((float) (1 + this.scaleFactorX), (float) (1 + this.scaleFactorY), 1);

        this.renderBackground(context);
        this.renderMainMenu(context, mouseX, mouseY);

        context.getMatrices().pop();
    }

    private void renderMainMenu(DrawContext context, int mouseX, int mouseY) {
        if (!this.finishedFetching) {
            long timeOpen = System.currentTimeMillis() - this.openedAt;

            int dots = (int) ((timeOpen / 300) % 4);
            int testLength = super.textRenderer.getWidth("Loading profiles");
            Text dottedText = Text.literal("Loading profiles").append(StringUtils.repeat(".", dots)).styled(style -> style.withColor(0xFFFF00FF));

            context.drawTextWithShadow(super.textRenderer, dottedText, this.width / 2 - testLength / 2, this.height / 2, 0xFF << 24);

            return;
        }


    }

    @Override
    public void renderBackground(DrawContext context) {
        context.fill(0,0, this.width, this.height, this.finishedFetching ? 0xFFD4D4D4 :  ~0 ^ 0x5f5f5f);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
