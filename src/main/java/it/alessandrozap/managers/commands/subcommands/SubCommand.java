package it.alessandrozap.managers.commands.subcommands;

import it.alessandrozap.managers.commands.CommandManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
@Getter
public abstract class SubCommand {
    protected final String name, permission, usage, description;
    protected final boolean allowFromConsole;
    protected final CommandManager commandManager;

    public abstract void execute(CommandSender sender, String[] args, String label);
}