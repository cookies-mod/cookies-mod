package dev.morazzer.cookiesmod.commands.dev;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.morazzer.cookiesmod.commands.dev.subcommands.DevSubcommand;
import dev.morazzer.cookiesmod.commands.helpers.ClientCommand;
import dev.morazzer.cookiesmod.commands.helpers.LoadCommand;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@LoadCommand
@Slf4j
public class DevCommand extends ClientCommand {
    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        LiteralArgumentBuilder<FabricClientCommandSource> command = literal("dev");

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage(DevSubcommand.class.getPackageName())
                .setScanners(Scanners.TypesAnnotated));

        reflections.getTypesAnnotatedWith(DevSubcommand.class).forEach(devSubCommand -> {
	        log.debug("Found class annotated with @DevSubcommand");
	        if (!ClientCommand.class.isAssignableFrom(devSubCommand)) {
		        log.warn("{} does not extend ClientCommand but is annotated with @DevSubcommand", devSubCommand);
		        return;
	        }

	        try {
		        //noinspection unchecked
		        Constructor<? extends ClientCommand> constructor = (Constructor<? extends ClientCommand>) devSubCommand.getConstructor();
		        ClientCommand clientCommand = constructor.newInstance();
		        command.then(clientCommand.getCommand());
	        } catch (NoSuchMethodException e) {
		        log.warn("No empty constructor found for class {}", devSubCommand);
	        } catch (InvocationTargetException e) {
		        log.error("Error while invoking constructor {}", devSubCommand, e);
	        } catch (InstantiationException e) {
		        log.error("Module {} is an abstract class", devSubCommand);
	        } catch (IllegalAccessException e) {
		        log.error("Constructor not accessible {}", devSubCommand);
	        }
        });

        return command;
    }
}
