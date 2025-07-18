package it.alessandrozap.utilsapi.interfaces;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface IMessageProvider {
    List<String> formatMainCommandHelp(CommandSender sender, String name, String description, String permission, String[] aliases);
    List<String> formatSubCommandHelp(CommandSender sender, String mainCommandName, String subCommandName, String usage, String description, String permission, String[] aliases);
    void sendNoPermission(CommandSender sender);
    void sendSubCommandNotFound(CommandSender sender, String input);
    void sendPlayerOnly(CommandSender sender);
    void sendUsage(CommandSender sender, String usage);
    void sendExecutionError(CommandSender sender, Exception e);
}
