package it.alessandrozap.interfaces;

import org.bukkit.command.CommandSender;

public interface MessageProvider {
    void sendNoPermission(CommandSender sender);
    void sendSubCommandNotFound(CommandSender sender, String input);
    void sendPlayerOnly(CommandSender sender);
    void sendUsage(CommandSender sender, String usage);
    void sendExecutionError(CommandSender sender, Exception e);
}
