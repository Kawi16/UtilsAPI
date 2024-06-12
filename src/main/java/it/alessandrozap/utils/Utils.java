package it.alessandrozap.utils;

import it.alessandrozap.managers.commands.subcommands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {

    public static String getMessage(YamlConfiguration file, String path) {
        return file.getString(path);
    }

    public static String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
