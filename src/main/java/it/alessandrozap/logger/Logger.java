package it.alessandrozap.logger;

import it.alessandrozap.UtilsAPI;
import it.alessandrozap.managers.messages.Locale;
import org.bukkit.Bukkit;

public class Logger {
    private static final String CONSOLE_PREFIX = Locale.translate("", UtilsAPI.getInstance().getPrefix());

    public static void log(String message, LogType type) {
        Bukkit.getConsoleSender().sendMessage(CONSOLE_PREFIX + type.getColor() + message);
    }
}
