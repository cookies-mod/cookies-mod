package dev.morazzer.cookiesmod.commands.dev.subcommands;

import dev.morazzer.cookiesmod.utils.GenerateLoader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to find all subcommands of the {@code /dev} command on runtime.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@GenerateLoader
public @interface DevSubcommand {
}
