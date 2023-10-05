package dev.morazzer.cookiesmod.features.mining;

import dev.morazzer.cookiesmod.config.ConfigManager;
import dev.morazzer.cookiesmod.events.api.ServerSwapEvent;
import dev.morazzer.cookiesmod.modules.LoadModule;
import dev.morazzer.cookiesmod.modules.Module;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import lombok.RequiredArgsConstructor;
import me.x150.renderer.render.Renderer3d;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.math.Vec3d;

import java.awt.Color;

@LoadModule
public class PuzzlerSolver implements Module {

    private Vec3d position = null;
    private final Vec3d puzzlerPosition = new Vec3d(181, 196, 135);

    @Override
    public void load() {
        ServerSwapEvent.SERVER_SWAP.register(() -> position = null);
        ClientReceiveMessageEvents.GAME.register(ExceptionHandler.wrap((message, overlay) -> {
            if (!MiningUtils.isInDwarven()) return;
            if (!ConfigManager.getConfig().miningCategory.showPuzzlerSolution.getValue()) return;
            if (overlay) return;

            String literalContent = message.getString();
            if (!literalContent.startsWith("§e[NPC] §dPuzzler§f: ")) {
                return;
            }
            String stripedContent = literalContent.replaceAll("§[a-z0-f]", "");

            String directions = stripedContent.substring(15);
            Vec3d solution = puzzlerPosition.subtract(0, 0.99, 0);
            boolean skip = false;
            for (char c : directions.toCharArray()) {
                Directions direction = Directions.getByChar(c);
                if (direction == null) {
                    skip = true;
                    break;
                }

                solution = solution.add(direction.vector);
            }

            if (skip) {
                this.position = null;
                return;
            }
            this.position = solution;
        }));
        WorldRenderEvents.AFTER_TRANSLUCENT.register(ExceptionHandler.wrap(context -> {
            if (!MiningUtils.isInDwarven()) return;
            if (!ConfigManager.getConfig().miningCategory.showPuzzlerSolution.getValue()) return;
            if (position == null) {
                return;
            }
            Renderer3d.renderFilled(context.matrixStack(), new Color(255, 0, 0, 125), position, new Vec3d(1, 1, 1));
        }));
    }

    @Override
    public String getIdentifierPath() {
        return "mining/puzzle_solver";
    }

    @RequiredArgsConstructor
    enum Directions {
        LEFT('◀', new Vec3d(1, 0, 0)),
        RIGHT('▶', new Vec3d(-1, 0, 0)),
        UP('▲', new Vec3d(0, 0, 1)),
        DOWN('▼', new Vec3d(0, 0, -1));

        final char literal;
        final Vec3d vector;

        public static Directions getByChar(char character) {
            for (Directions value : values()) {
                if (value.literal == character) {
                    return value;
                }
            }
            return null;
        }
    }
}