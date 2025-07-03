package it.alessandrozap.utilsapi.managers.commands;

import it.alessandrozap.utilsapi.UtilsAPI;
import it.alessandrozap.utilsapi.annotations.commands.Command;
import it.alessandrozap.utilsapi.annotations.commands.MainCommand;
import it.alessandrozap.utilsapi.annotations.commands.SubCommand;
import it.alessandrozap.utilsapi.defaults.DefaultMessageProvider;
import it.alessandrozap.utilsapi.interfaces.IMessageProvider;
import it.alessandrozap.utilsapi.logger.LogType;
import it.alessandrozap.utilsapi.logger.Logger;
import it.alessandrozap.utilsapi.managers.messages.Locale;
import it.alessandrozap.utilsapi.utils.Utility;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.*;

public class CommandManager {
    private final Map<Object, Method> defaultCommandMethods = new HashMap<>();
    private final JavaPlugin plugin = UtilsAPI.getInstance().getPlugin();
    private final Map<String, RegisteredSubCommand> subCommands = new HashMap<>();
    @Getter @Setter
    private IMessageProvider messageProvider = new DefaultMessageProvider();

    public void register(Object commandInstance) {
        Class<?> clazz = commandInstance.getClass();

        if (!clazz.isAnnotationPresent(Command.class)) return;
        Command cmdInfo = clazz.getAnnotation(Command.class);
        if(!cmdInfo.register()) return;

        CommandImpl command = new CommandImpl(cmdInfo.name(), (sender, args) -> {
            if (!cmdInfo.permission().isEmpty() && !sender.hasPermission(cmdInfo.permission())) {
                messageProvider.sendNoPermission(sender);
                return true;
            }

            if (args.length == 0) {
                Method defaultMethod = defaultCommandMethods.get(commandInstance);
                if (defaultMethod != null) {
                    try {
                        if (defaultMethod.getParameterCount() != 1 ||
                                !CommandSender.class.isAssignableFrom(defaultMethod.getParameterTypes()[0])) {
                            throw new IllegalArgumentException("Default method must have signature: (CommandSender)");
                        }
                        MainCommand mainCommand = defaultMethod.getAnnotation(MainCommand.class);
                        if (mainCommand != null && !mainCommand.allowConsole() && !(sender instanceof Player)) {
                            messageProvider.sendPlayerOnly(sender);
                            return true;
                        }
                        defaultMethod.invoke(commandInstance, sender);
                    } catch (Exception e) {
                        messageProvider.sendUsage(sender, Locale.translate(Utility.toSmallCaps(cmdInfo.usage())));
                        e.printStackTrace();
                    }
                    return true;
                }
                return true;
            }

            RegisteredSubCommand sub = subCommands.get(args[0].toLowerCase());
            if (sub == null) {
                messageProvider.sendSubCommandNotFound(sender, args[0]);
                return true;
            }

            if (!sub.allowConsole && !(sender instanceof Player)) {
                messageProvider.sendPlayerOnly(sender);
                return true;
            }

            if (!sub.permission.isEmpty() && !sender.hasPermission(sub.permission)) {
                messageProvider.sendNoPermission(sender);
                return true;
            }

            try {
                if (sub.method.getParameterCount() != 2 ||
                        !CommandSender.class.isAssignableFrom(sub.method.getParameterTypes()[0]) ||
                        !sub.method.getParameterTypes()[1].isArray() ||
                        !sub.method.getParameterTypes()[1].getComponentType().equals(String.class)) {
                    throw new IllegalArgumentException("I metodi @SubCommand devono avere la firma: (CommandSender, String[]) - " + sub.getClass().getSimpleName());
                }
                sub.method.invoke(commandInstance, sender, Arrays.copyOfRange(args, 1, args.length));
            } catch (Exception e) {
                messageProvider.sendExecutionError(sender, e);
            }

            return true;
        });

        command.setAliases(Arrays.asList(cmdInfo.aliases()));
        command.setDescription(cmdInfo.description());
        command.setPermission(cmdInfo.permission());

        Method defaultMethod = null;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(SubCommand.class)) {
                SubCommand sub = method.getAnnotation(SubCommand.class);
                if (!sub.register()) continue;

                RegisteredSubCommand rsc = new RegisteredSubCommand(method, sub.permission(), sub.description(), sub.allowConsole());
                subCommands.put(sub.name().toLowerCase(), rsc);
                for (String alias : sub.aliases()) {
                    subCommands.put(alias.toLowerCase(), rsc);
                }
            } else if (method.isAnnotationPresent(MainCommand.class)) {
                defaultMethod = method;
            }
        }

        if (defaultMethod != null) {
            defaultCommandMethods.put(commandInstance, defaultMethod);
        }

        CommandRegister.registerCommand(plugin, command);
    }

    public long registerAll(boolean outputTime) {
        long startTime = System.currentTimeMillis();
        int count = 0;

        for (Class<?> clazz : UtilsAPI.getInstance().getPackageClassesList().stream().filter(c -> c.isAnnotationPresent(Command.class)).toList()) {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                register(instance);
                count++;
            } catch (Exception e) {
                Logger.log("Error during registration of " + clazz.getSimpleName(), LogType.ERROR);
                e.printStackTrace();
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        if(outputTime) Logger.log("Registered " + count + " commands, duration: " + duration + " ms.", LogType.INFO);
        else Logger.log("Registered " + count + " commands", LogType.INFO);
        return duration;
    }

    public void reset() {
        CommandRegister.unregisterAll();
        subCommands.clear();
        defaultCommandMethods.clear();
    }

    private static class RegisteredSubCommand {
        Method method;
        String permission;
        String description;
        boolean allowConsole;

        public RegisteredSubCommand(Method method, String permission, String description, boolean allowConsole) {
            this.method = method;
            this.permission = permission;
            this.description = description;
            this.allowConsole = allowConsole;
        }
    }
}

