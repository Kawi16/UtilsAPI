package it.alessandrozap.utilsapi.managers.messages;

import it.alessandrozap.utilsapi.logger.LogType;
import it.alessandrozap.utilsapi.logger.Logger;
import it.alessandrozap.utilsapi.managers.file.FileManager;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
public final class Locale {
    private static final Map<String, Locale> localeMap = new HashMap<>();
    private static FileManager messagesFile = null;
    private static boolean errorSent = false;
    private static final String BASE_PATH = "messages.";
    @Getter @Setter
    private static boolean useMiniMessage = false;

    public static Locale PREFIX;

    private Object message;

    public Locale(String identifier) {
        if (messagesFile == null) {
            if(!errorSent) {
                Logger.log("[API] The messages file wasn't configured in the Locale class - you need to call the setup method first.", LogType.ERROR);
                errorSent = true;
                throw new IllegalStateException("Messages file not initialized. Please call setup() before using this class.");
            }
        } else { localeMap.put(identifier, this); }
    }

    public static long reload(boolean silent, boolean outputTime) {
        if(!silent) Logger.log("Loading messages...", LogType.INFO);
        long startTime = System.currentTimeMillis();

        int messagesAmount = 0;
        File file = messagesFile.getFile();

        YamlConfiguration cfg = messagesFile.getConfig();

        if (!file.exists())
            messagesFile.setup();

        for (String identifier : localeMap.keySet()) {
            List<String> listMessage = cfg.getStringList(BASE_PATH + identifier);
            if (!listMessage.isEmpty()) localeMap.get(identifier).setMessage(translate(listMessage));
            else {
                String rawMessage = cfg.getString(BASE_PATH + identifier, "");
                localeMap.get(identifier).setMessage(translate(rawMessage));
            }
            messagesAmount++;
        }
        long duration = System.currentTimeMillis() - startTime;
        if(!silent) {
            if(outputTime) Logger.log("Loaded " + messagesAmount + " messages. Duration: " + duration + " ms.", LogType.INFO);
            else Logger.log("Loaded " + messagesAmount + " messages.", LogType.INFO);
        }
        return duration;
    }

    public static long reload(boolean silent) { return reload(silent, false); }
    public static long reload() { return reload(false, false); }

    public static void setup(FileManager messagesFile, boolean initPrefix, String prefixPath, boolean useMiniMessage, boolean outputTime) {
        Locale.messagesFile = messagesFile;
        Locale.useMiniMessage = useMiniMessage;
        if (initPrefix) {
            PREFIX = new Locale(prefixPath);
            String rawMessage = Locale.messagesFile.getConfig().getString(BASE_PATH + prefixPath, "");
            localeMap.get(prefixPath).setMessage(translate(rawMessage));
            Logger.setConsolePrefix(PREFIX.getMessage(false));
        }
    }

    public static void setup(FileManager messagesFile, boolean initPrefix, String prefixPath, boolean useMiniMessage) {
        setup(messagesFile, initPrefix, prefixPath, useMiniMessage, false);
    }

    public static void setup(FileManager messagesFile, boolean initPrefix, String prefixPath) {
        setup(messagesFile, initPrefix, prefixPath, false, false);
    }

    public static void setup(FileManager messagesFile, String prefixPath) {
        setup(messagesFile, true, prefixPath, false, false);
    }

    public static void setup(FileManager messagesFile) {
        setup(messagesFile, "prefix");
    }

    public static void sendMessage(CommandSender sender, String message) {
        if (useMiniMessage) {
            Component component = translateToComponent(message);
            sender.sendMessage(component);
        } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private String getString(Object message, boolean prefix, Object[] objects) {
        if (message != null) {
            if (message instanceof String) {
                String msg = "";
                if(prefix) msg = (String) Locale.PREFIX.message;
                msg += (String) message;
                for (int i = 0; i < objects.length; i++) {
                    msg = msg.replace("{" + i + "}", objects[i].toString());
                }
                return msg;
            } else if (message instanceof List) {
                return getListToString(message, prefix, objects);
            }
        }
        return null;
    }

    public static String getListToString(Object message, boolean prefix, Object... objects) {
        List<String> messages = (List<String>) message;
        List<String> processedMessages = new ArrayList<>();
        for (String msg : messages) {
            if(prefix) msg = Locale.PREFIX.message + msg;
            for (int i = 0; i < objects.length; i++) {
                msg = msg.replace("{" + i + "}", objects[i].toString());
            }
            processedMessages.add(msg);
        }
        return String.join("\n", processedMessages);
    }

    public String getMessage(boolean prefix, Object... objects) {
        return getString(message, prefix, objects);
    }

    public Component getMessageComponent(boolean prefix, Object... objects) {
        String msg = getMessage(prefix, objects);
        return msg != null ? translateToComponent(msg) : Component.empty();
    }

    public void send(CommandSender sender, boolean prefix, Object... objects) {
        String message = getMessage(prefix, objects);
        if (message != null && sender != null) {
            if (useMiniMessage) sender.sendMessage(translateToComponent(message));
            else sender.sendMessage(message);
        }
    }

    public void sendComponent(CommandSender sender, boolean prefix, Object... objects) {
        Component component = getMessageComponent(prefix, objects);
        if (sender != null) sender.sendMessage(component);
    }

    public Object get() {
        return message;
    }

    private void setMessage(Object message) {
        this.message = message;
    }

    public static List<String> translate(List<String> list) {
        List<String> l = new ArrayList<>();
        for(String s : list) {
            l.add(translate(s));
        }
        return l;
    }

    public static String translate(String message) {
        if (useMiniMessage) {
            Component component = translateToComponent(message);
            return LegacyComponentSerializer.legacySection().serialize(component);
        } else return translateLegacy(message);
    }

    public static Component translateToComponent(String message) {
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

    private static String translateLegacy(String message) {
        final char COLOR_CHAR = ChatColor.COLOR_CHAR;
        final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder replacement = new StringBuilder(COLOR_CHAR + "x");
            for (char c : hex.toCharArray()) {
                replacement.append(COLOR_CHAR).append(c);
            }
            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);

        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
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