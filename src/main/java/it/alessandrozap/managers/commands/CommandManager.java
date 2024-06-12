package it.alessandrozap.managers.commands;

import it.alessandrozap.managers.commands.subcommands.SubCommand;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.reflections.Reflections;

import java.util.*;
import java.util.stream.Collectors;

import static it.alessandrozap.utils.Utils.translate;


@Getter
public class CommandManager implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;
    private final HashMap <String, SubCommand> subCommands = new HashMap<>();
    private final String command, prefix, nomeHelp;
    private final ChatColor helpColor;
    private final boolean italian;

    public CommandManager(JavaPlugin plugin, String command, String prefix, String nomeHelp, ChatColor helpColor, boolean italian) {
        this.plugin = plugin;
        this.command = command;
        this.prefix = prefix;
        this.nomeHelp = nomeHelp;
        this.helpColor = helpColor;
        this.italian = italian;

        init();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            String[] strings = new String[subCommands.size() + 8];
            strings[0] = "";
            strings[1] = translate("&7&m&l---------------------------------------------");
            strings[2] = helpColor + translate(nomeHelp + " Help");
            strings[3] = "";
            if(!italian) strings[4] = translate("&r<> &7= &rRequired &7| &r[] &7= &rOptional");
            else strings[4] = translate("&r<> &7= &rRichiesto &7| &r[] &7= &rOpzionale");
            strings[5] = "";

            List<Map.Entry<String, SubCommand>> entryList = new ArrayList<>(subCommands.entrySet());
            for(int i = 0; i < entryList.size(); i++) {
                Map.Entry<String, SubCommand> entry = entryList.get(i);

                strings[i + 6] = translate("&7* ") + helpColor + translate("/" + this.command + " " + entry.getValue().getUsage() + "&7 - " + helpColor + entry.getValue().getDescription());
            }

            strings[subCommands.size() + 6] = "";
            strings[subCommands.size() + 7] = translate("&7&m&l---------------------------------------------");

            sender.sendMessage(strings);

            return false;
        }
        if(getSubCommands().containsKey(args[0])) { // O(1)
            SubCommand subCommand = getSubCommands().get(args[0]);// O(1) Non è necessario questo coso si può smeplicemente fare getsubcommands.getargs ma anche così va bene
            if(!subCommand.isAllowFromConsole() && !(sender instanceof Player)){// O(1)
                if(italian) sender.sendMessage(translate(getPrefix() + "&cIl comando può esser eseguito solo da un player."));// O(1)
                else sender.sendMessage(translate(getPrefix() + "&cThe command is executable only from a player."));
                return true;// O(1)
            }
            if(!sender.hasPermission(subCommand.getPermission())){// O(1)
               if(italian) sender.sendMessage(translate(getPrefix() + "&cPermesso mancante."));// O(1)
                else sender.sendMessage(translate(getPrefix() + "&cInsufficient permission."));
               return true;// O(1)
            }
            subCommand.execute(sender, args, label);// O(1)
            return false;
        }

        if(italian) sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', getPrefix() + "&cComando inesistente."
                )
        );
        else sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&', getPrefix() + "&cUnknown command."
                )
        );
        //Complessità O(1)

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        if(args.length == 0) {
            return result;
        }

        if(args.length == 1) {
            List<String> matchingCommands = subCommands.entrySet().stream()
                    .filter(entry -> sender.hasPermission(entry.getValue().getPermission()))
                    .map(Map.Entry::getKey)
                    .filter(commandName -> commandName.startsWith(args[0]))
                    .collect(Collectors.toList());

            StringUtil.copyPartialMatches(args[0], matchingCommands, result);
            return result;
        }

        SubCommand subCommand = subCommands.get(args[0]);

        if(subCommand != null) {
            if(!sender.hasPermission(subCommand.getPermission())) return Collections.emptyList();
            String[] usage = subCommand.getUsage().split(" ");
            if(usage.length >= args.length) {
                if(usage[args.length - 1].contains("<player>")) return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(name -> name.toLowerCase().startsWith(args[args.length-1].toLowerCase())).collect(Collectors.toList());
                String par = usage[args.length - 1]
                        .replaceAll("<", "")
                        .replaceAll(">", "");
                if(par.contains("/")) result.addAll(Arrays.asList(par.split("/")));
                else result.add(par);
            }
        }

        return result;

    }

    private void init() {
        PluginCommand cmd = plugin.getCommand(command);
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);

        Reflections ref = new Reflections("it.alessandrozap");
        Reflections reflections = new Reflections("com.pinco");

        Set<Class<? extends SubCommand>> subCommandClasses = ref.getSubTypesOf(SubCommand.class);
        Set<Class<? extends SubCommand>> subCommandClassesPinco = reflections.getSubTypesOf(SubCommand.class);

        if(!subCommandClassesPinco.isEmpty()){
            for(Class<? extends SubCommand> subCommandClass : subCommandClassesPinco) {
                try {
                    SubCommand subCommand = subCommandClass.getDeclaredConstructor(CommandManager.class).newInstance(this);
                    subCommands.put(subCommand.getName(), subCommand);
                } catch (Exception ignored) {}
            }
        } else {
            for (Class<? extends SubCommand> subCommandClass : subCommandClasses) {
                try {
                    SubCommand subCommand = subCommandClass.getDeclaredConstructor(CommandManager.class).newInstance(this);
                    subCommands.put(subCommand.getName(), subCommand);
                } catch (Exception ignored) {}
            }
        }

    }




}