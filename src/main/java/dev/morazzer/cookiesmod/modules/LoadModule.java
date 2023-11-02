package dev.morazzer.cookiesmod.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to find all modules that should be loaded.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface LoadModule {

    /**
     * The identifier of the method to filter if it should be loaded or not.
     *
     * @return The identifier.
     */
    String value();

}
