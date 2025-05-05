package it.alessandrozap.managers.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Map;

public class CommandRegister {

    private static CommandMap getCommandMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            throw new RuntimeException("Error getting CommandMap - if you see this, open a ticket on GitHub (https://github.com/Kawi16)", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Command> getKnownCommands() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());

            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);

            return (Map<String, Command>) knownCommandsField.get(commandMap);
        } catch (Exception e) {
            throw new RuntimeException("Error getting Known Commands - if you see this, open a ticket on GitHub (https://github.com/Kawi16)", e);
        }
    }

    public static void registerCommand(JavaPlugin plugin, Command command) {
        getCommandMap().register(plugin.getName(), command);
    }

    public static void unregisterCommand(String name) {
        getKnownCommands().remove(name);
    }

}
