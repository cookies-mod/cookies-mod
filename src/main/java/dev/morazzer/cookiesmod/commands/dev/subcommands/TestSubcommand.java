package dev.morazzer.cookiesmod.commands.dev.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

@DevSubcommand
@Slf4j
public class TestSubcommand extends ClientCommand {
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        LiteralArgumentBuilder<FabricClientCommandSource> subcommand = literal("test");

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage(CookiesMod.class.getPackageName())
                .setScanners(Scanners.MethodsAnnotated));

        reflections.getMethodsAnnotatedWith(TestEntrypoint.class).forEach(entrypoint -> {
            int modifiers = entrypoint.getModifiers();
            if (!Modifier.isStatic(modifiers)
                    || !Modifier.isPublic(modifiers)) {
                log.warn("Found @TestEntrypoint annotation on a non public/static method, skipping");
                return;
            }

            if (entrypoint.getParameterCount() != 0) {
                log.warn("Found @TestEntrypoint annotation on a method with parameters, skipping");
                return;
            }

            TestEntrypoint annotation = entrypoint.getAnnotation(TestEntrypoint.class);
            String name = annotation.value();
            subcommand.then(literal(name).executes(context -> {
                try {
                    entrypoint.invoke(null);
                } catch (IllegalAccessException e) {
                    context.getSource().sendFeedback(CookiesMod.createPrefix(ColorUtils.failColor).append("Failed to access the test method!"));
                } catch (InvocationTargetException e) {
                    context.getSource().sendFeedback(CookiesMod.createPrefix(ColorUtils.failColor).append("Failed to invoke the target method!"));
                } catch (Exception e) {
                    ExceptionHandler.handleException(e);
                }
                return Command.SINGLE_SUCCESS;
            }));
        });

        return subcommand;
    }
}
