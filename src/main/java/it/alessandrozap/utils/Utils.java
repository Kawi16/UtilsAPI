package it.alessandrozap.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String getMessage(YamlConfiguration file, String path) {
        return file.getString(path);
    }

    /** @deprecated
     *
     * @param s String to be colored
     * @return Translated colored string with normal color, not supports HEX
     */
    public static String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
        Get a component with colored text, supports HEX (&#)
        @return The Component with the colored text
     */
    public static Component translateComponent(String message) {
        String converted = message.replaceAll("&#([A-Fa-f0-9]{6})", "<#$1>");
        Pattern legacyPattern = Pattern.compile("&([a-f0-9k-orA-FK-OR])");
        Matcher matcher = legacyPattern.matcher(converted);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String group = matcher.group(1).toLowerCase();
            String replacement = getMiniMessageTag(group.charAt(0));
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);

        return MiniMessage.miniMessage().deserialize(buffer.toString());
    }

    private static String getMiniMessageTag(char code) {
        return switch (code) {
            case '0' -> "<black>";
            case '1' -> "<dark_blue>";
            case '2' -> "<dark_green>";
            case '3' -> "<dark_aqua>";
            case '4' -> "<dark_red>";
            case '5' -> "<dark_purple>";
            case '6' -> "<gold>";
            case '7' -> "<gray>";
            case '8' -> "<dark_gray>";
            case '9' -> "<blue>";
            case 'a' -> "<green>";
            case 'b' -> "<aqua>";
            case 'c' -> "<red>";
            case 'd' -> "<light_purple>";
            case 'e' -> "<yellow>";
            case 'f' -> "<white>";
            case 'k' -> "<obfuscated>";
            case 'l' -> "<bold>";
            case 'm' -> "<strikethrough>";
            case 'n' -> "<underline>";
            case 'o' -> "<italic>";
            case 'r' -> "<reset>";
            default -> "";
        };
    }
}
