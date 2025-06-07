package it.alessandrozap.managers.messages;

import it.alessandrozap.logger.LogType;
import it.alessandrozap.logger.Logger;
import it.alessandrozap.managers.file.FileManager;
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
            //localeMap.get(identifier).setMessage(translate("", cfg.getString("messages." + identifier, "")));
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

    public static void setup(FileManager messagesFile, boolean initPrefix, String prefixPath, boolean outputTime) {
        Locale.messagesFile = messagesFile;
        if(initPrefix) {
            PREFIX = new Locale(prefixPath);
            String rawMessage = Locale.messagesFile.getConfig().getString(BASE_PATH + prefixPath, "");
            localeMap.get(prefixPath).setMessage(translate(rawMessage));
            Logger.setConsolePrefix(PREFIX.getMessage(false));
        }
    }

    public static void setup(FileManager messagesFile,  boolean initPrefix, String prefixPath) {
        setup(messagesFile, initPrefix, prefixPath, false);
    }
    public static void setup(FileManager messagesFile, String prefixPath) {
        setup(messagesFile, true, prefixPath, false);
    }

    public static void setup(FileManager messagesFile) {
        setup(messagesFile, "prefix");
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
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

    public void send(CommandSender sender, boolean prefix, Object... objects) {
        String message = getMessage(prefix, objects);
        if (message != null && sender != null)
            sender.sendMessage(message);
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


}
