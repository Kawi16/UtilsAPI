package it.alessandrozap.managers.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class CommandImpl extends Command {

    private final BiFunction<CommandSender, String[], Boolean> executor;

    public CommandImpl(String name, BiFunction<CommandSender, String[], Boolean> executor) {
        super(name);
        this.executor = executor;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        return executor.apply(sender, args);
    }
}
