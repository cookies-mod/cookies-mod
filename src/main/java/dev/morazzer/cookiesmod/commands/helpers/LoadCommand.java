package dev.morazzer.cookiesmod.commands.helpers;

import dev.morazzer.cookiesmod.utils.GenerateLoader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The purpose of this annotation is to load all annotated classes and extend
 * {@linkplain dev.morazzer.cookiesmod.commands.helpers.ClientCommand}.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@GenerateLoader
public @interface LoadCommand {
}
