package dev.morazzer.cookiesmod.commands.helpers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The purpose of this annotation is to load all annotated classes and extend
 * {@linkplain dev.morazzer.cookiesmod.commands.helpers.ClientCommand}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LoadCommand {
}
