package it.alessandrozap.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    private static final Map<Character, Character> SMALL_CAPS_MAP = createSmallCapsMap();

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

    private static Map<Character, Character> createSmallCapsMap() {
        Map<Character, Character> map = new HashMap<>();
        map.put('a', 'ᴀ');
        map.put('b', 'ʙ');
        map.put('c', 'ᴄ');
        map.put('d', 'ᴅ');
        map.put('e', 'ᴇ');
        map.put('f', 'ꜰ');
        map.put('g', 'ɢ');
        map.put('h', 'ʜ');
        map.put('i', 'ɪ');
        map.put('j', 'ᴊ');
        map.put('k', 'ᴋ');
        map.put('l', 'ʟ');
        map.put('m', 'ᴍ');
        map.put('n', 'ɴ');
        map.put('o', 'ᴏ');
        map.put('p', 'ᴘ');
        map.put('q', 'ꞯ');
        map.put('r', 'ʀ');
        map.put('s', 'ꜱ');
        map.put('t', 'ᴛ');
        map.put('u', 'ᴜ');
        map.put('v', 'ᴠ');
        map.put('w', 'ᴡ');
        map.put('x', 'x');
        map.put('y', 'ʏ');
        map.put('z', 'ᴢ');
        map.put('A', 'ᴀ');
        map.put('B', 'ʙ');
        map.put('C', 'ᴄ');
        map.put('D', 'ᴅ');
        map.put('E', 'ᴇ');
        map.put('F', 'ꜰ');
        map.put('G', 'ɢ');
        map.put('H', 'ʜ');
        map.put('I', 'ɪ');
        map.put('J', 'ᴊ');
        map.put('K', 'ᴋ');
        map.put('L', 'ʟ');
        map.put('M', 'ᴍ');
        map.put('N', 'ɴ');
        map.put('O', 'ᴏ');
        map.put('P', 'ᴘ');
        map.put('Q', 'ꞯ');
        map.put('R', 'ʀ');
        map.put('S', 'ꜱ');
        map.put('T', 'ᴛ');
        map.put('U', 'ᴜ');
        map.put('V', 'ᴠ');
        map.put('W', 'ᴡ');
        map.put('X', 'x');
        map.put('Y', 'ʏ');
        map.put('Z', 'ᴢ');
        return map;
    }

    /**
        Convert a string in small caps font
        @param s String to convert
        @return Converted string (not color translated)
     */
    public static String toSmallCaps(String s) {
        if (s == null) return null;

        StringBuilder result = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (SMALL_CAPS_MAP.containsKey(Character.toLowerCase(c))) {
                char smallCap = SMALL_CAPS_MAP.get(Character.toLowerCase(c));
                result.append(smallCap);
            } else result.append(c);
        }
        return result.toString();
    }

}
