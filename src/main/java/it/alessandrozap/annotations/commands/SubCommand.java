package it.alessandrozap.annotations.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {
    String name();
    String[] aliases() default {};
    String description() default "";
    String permission() default "";
    boolean allowConsole() default false;
    boolean register() default true;
}
