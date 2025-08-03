package it.alessandrozap.utilsapi.interfaces;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface IMessageProvider {
    List<String> formatMainCommandHelp(CommandSender sender, String name, String description, String permission, String[] aliases);
    List<String> formatSubCommandHelp(CommandSender sender, String mainCommandName, String subCommandName, String usage, String description, String permission, String[] aliases);
    void sendNoPermission(CommandSender sender, boolean prefix);
    void sendSubCommandNotFound(CommandSender sender, boolean prefix, String input);
    void sendPlayerOnly(CommandSender sender, boolean prefix);
    void sendUsage(CommandSender sender, boolean prefix, String usage);
    void sendExecutionError(CommandSender sender, boolean prefix, Exception e);
}
