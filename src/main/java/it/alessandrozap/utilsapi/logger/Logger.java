package it.alessandrozap.utilsapi.logger;

import it.alessandrozap.utilsapi.managers.messages.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

public class Logger {
    private static Component CONSOLE_PREFIX_COMPONENT = Component.empty();
    private static String CONSOLE_PREFIX_STRING = "";

    public static void log(String message, LogType type) {
        if (Locale.isUseMiniMessage()) {
            Component messageComponent = Component.text(message, convertToTextColor(type));
            Component finalComponent = CONSOLE_PREFIX_COMPONENT.append(messageComponent);
            String legacyMessage = LegacyComponentSerializer.legacySection().serialize(finalComponent);
            Bukkit.getConsoleSender().sendMessage(legacyMessage);
        } else Bukkit.getConsoleSender().sendMessage(CONSOLE_PREFIX_STRING + type.getColor() + message);
    }

    public static void setConsolePrefix(String prefix) {
        if (prefix == null) prefix = "";
        if (Locale.isUseMiniMessage()) {
            CONSOLE_PREFIX_COMPONENT = MiniMessage.miniMessage().deserialize(prefix);
            CONSOLE_PREFIX_STRING = LegacyComponentSerializer.legacySection().serialize(CONSOLE_PREFIX_COMPONENT);
        } else CONSOLE_PREFIX_STRING = Locale.translate(prefix);
    }

    private static TextColor convertToTextColor(LogType type) {
        return switch (type) {
            case ERROR -> NamedTextColor.RED;
            case WARN -> NamedTextColor.YELLOW;
            case INFO -> NamedTextColor.WHITE;
            case START -> NamedTextColor.DARK_RED;
        };
    }
}