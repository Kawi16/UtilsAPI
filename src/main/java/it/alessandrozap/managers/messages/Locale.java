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

    public static long reload(boolean outputTime) {
        Logger.log("Loading messages...", LogType.INFO);
        long startTime = System.currentTimeMillis();

        int messagesAmount = 0;
        File file = messagesFile.getFile();

        YamlConfiguration cfg = messagesFile.getConfig();

        if (!file.exists())
            messagesFile.setup();

        for (String identifier : localeMap.keySet()) {
            List<String> listMessage = cfg.getStringList("messages." + identifier);
            if (!listMessage.isEmpty()) localeMap.get(identifier).setMessage(translate("", listMessage));
            else {
                String rawMessage = cfg.getString("messages." + identifier, "");
                localeMap.get(identifier).setMessage(translate("", rawMessage));
            }
            //localeMap.get(identifier).setMessage(translate("", cfg.getString("messages." + identifier, "")));
            messagesAmount++;
        }
        long duration = System.currentTimeMillis() - startTime;
        if(outputTime) Logger.log("Loaded " + messagesAmount + " messages. Duration: " + duration + " ms.", LogType.INFO);
        else Logger.log("Loaded " + messagesAmount + " messages.", LogType.INFO);
        return duration;
    }

    public static void setup(FileManager messagesFile, String prefixPath, boolean outputTime) {
        Locale.messagesFile = messagesFile;
        PREFIX = new Locale(prefixPath);
        reload(outputTime);
    }

    public static void setup(FileManager messagesFile, String prefixPath) {
        setup(messagesFile, prefixPath, false);
    }

    public static void setup(FileManager messagesFile) {
        setup(messagesFile, "prefix", false);
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

    public static List<String> translate(String endTag, List<String> list) {
        List<String> l = new ArrayList<>();
        for(String s : list) {
            l.add(translate(endTag, s));
        }
        return l;
    }

    public static String translate(String endTag, String message) {
        final char COLOR_CHAR = ChatColor.COLOR_CHAR;
        String startTag;
        if (message.startsWith("&#")) startTag = "&#";
        else return ChatColor.translateAlternateColorCodes('&', message);

        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);

        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }


}
