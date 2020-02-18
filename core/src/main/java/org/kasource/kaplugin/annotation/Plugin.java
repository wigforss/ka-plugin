package org.kasource.kaplugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate classes that implement an @ExtensionPoint interface with this annotation.
 *
 * @author rikardwigforss
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Plugin {
    String name();
    String version();
    String vendor();
    int priority () default 0;
}
