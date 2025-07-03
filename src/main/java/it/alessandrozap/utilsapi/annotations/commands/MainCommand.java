package it.alessandrozap.utilsapi.annotations.commands;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MainCommand {
    String description() default "";
    boolean allowConsole() default false;
    boolean register() default true;
}
