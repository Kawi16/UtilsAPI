package it.alessandrozap.utilsapi.logger;

import it.alessandrozap.utilsapi.UtilsAPI;
import it.alessandrozap.utilsapi.managers.messages.Locale;
import org.bukkit.Bukkit;

public class Logger {
    private static String CONSOLE_PREFIX = UtilsAPI.getInstance() != null ?
            Locale.translate(UtilsAPI.getInstance().getPrefix()) :
            Locale.PREFIX.getMessage(false);

    public static void log(String message, LogType type) {
        Bukkit.getConsoleSender().sendMessage(CONSOLE_PREFIX + type.getColor() + message);
    }

    public static void setConsolePrefix(String prefix) { CONSOLE_PREFIX = prefix; }
}
