package it.alessandrozap.annotations.commands;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    String name();
    String[] aliases() default {};
    String description() default "";
    String permission() default "";
    boolean register() default true;
}
