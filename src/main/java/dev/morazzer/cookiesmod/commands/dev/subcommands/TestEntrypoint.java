package dev.morazzer.cookiesmod.commands.dev.subcommands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adding this annotation to a public static method with 0 parameters will add it to the <u>/dev test</u> command as a literal argument
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestEntrypoint {
    /**
     * The name for the test, will be added as literal argument
     *
     * @return The name of the test
     */
    String value();
}
