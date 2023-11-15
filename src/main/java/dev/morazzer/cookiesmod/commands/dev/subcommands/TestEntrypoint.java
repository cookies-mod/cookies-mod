package dev.morazzer.cookiesmod.commands.dev.subcommands;

import dev.morazzer.cookiesmod.utils.GenerateLoader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adding this annotation to a public static method with zero parameters will add it to the {@code /dev test} command as
 * a literal argument.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@GenerateLoader
public @interface TestEntrypoint {

    /**
     * The name for the test will be added as literal argument.
     *
     * @return The name that will be used in the command tree.
     */
    String value();

}
