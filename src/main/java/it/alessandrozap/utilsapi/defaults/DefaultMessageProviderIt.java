package it.alessandrozap.utilsapi.defaults;

import it.alessandrozap.utilsapi.interfaces.IMessageProvider;
import it.alessandrozap.utilsapi.managers.messages.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DefaultMessageProviderIt implements IMessageProvider {

    @Override
    public List<String> formatSubCommandHelp(CommandSender sender, String mainCommandName, String subCommandName, String usage, String description, String permission, String[] aliases) {
        List<String> list = new ArrayList<>();
        list.add(String.format("&#929B92• &f/%s &#929B92- %s", usage != null && !usage.isEmpty() ? usage : (mainCommandName + " " + subCommandName), description));
        if(permission != null && !permission.isEmpty()) list.add("  &#929B92" + ((sender instanceof Player) ? "ᴘᴇʀᴍᴇꜱꜱᴏ: " : "Permesso: ") + permission);
        if(aliases != null && aliases.length > 0) {
            list.add("  &#929B92" + ((sender instanceof Player) ? "ᴀʟɪᴀꜱᴇꜱ: " : "Aliases: "));
            for(String alias : aliases) {
                list.add(String.format("    &#929B92• &f/%s %s", mainCommandName, alias));
            }
        }
        return Locale.translate(list);
    }

    @Override
    public List<String> formatMainCommandHelp(CommandSender sender, String name, String description, String permission, String[] aliases) {
        List<String> list = new ArrayList<>();
        list.add(String.format("&#929B92• &f/%s &#929B92- %s", name, description));
        if(permission != null && !permission.isEmpty()) list.add("  &#929B92" + ((sender instanceof Player) ? "ᴘᴇʀᴍᴇꜱꜱᴏ: " : "Permesso: ") + permission);
        if(aliases != null && aliases.length > 0) {
            list.add("  &#929B92" + ((sender instanceof Player) ? "ᴀʟɪᴀꜱᴇꜱ: " : "Aliases: "));
            for(String alias : aliases) {
                list.add(String.format("    &#929B92• &f/%s", alias));
            }
        }
        return Locale.translate(list);
    }

    @Override
    public void sendNoPermission(CommandSender sender, boolean prefix) {
        sender.sendMessage(getMessage("&#FF0C0Cᴘ&#FF0B0Bᴇ&#FF0A0Aʀ&#FF0909ᴍ&#FF0808ᴇ&#FF0707ꜱ&#FF0606ꜱ&#FF0505ᴏ &#FF0404ᴍ&#FF0404ᴀ&#FF0404ɴ&#FF0404ᴄ&#FF0404ᴀ&#FF0404ɴ&#FF0404ᴛ&#FF0404ᴇ", prefix));
    }

    @Override
    public void sendSubCommandNotFound(CommandSender sender, boolean prefix, String input) {
        sender.sendMessage(getMessage("&#EE3131ꜱ&#EF3131ᴏ&#EF3232ᴛ&#F03232ᴛ&#F13232ᴏ&#F23333ᴄ&#F23333ᴏ&#F33333ᴍ&#F43434ᴀ&#F53434ɴ&#F53434ᴅ&#F63535ᴏ &#F83636ɴ&#F83636ᴏ&#F93636ɴ &#FB3737ᴛ&#FB3737ʀ&#FC3838ᴏ&#FD3838ᴠ&#FE3838ᴀ&#FE3939ᴛ&#FF3939ᴏ: " + input, prefix));
    }

    @Override
    public void sendPlayerOnly(CommandSender sender, boolean prefix) {
        sender.sendMessage(getMessage("&#FF0C0Cᴄ&#FF0C0Cᴏ&#FF0B0Bᴍ&#FF0B0Bᴀ&#FF0A0Aɴ&#FF0A0Aᴅ&#FF0909ᴏ &#FF0909ᴜ&#FF0808ᴛ&#FF0808ɪ&#FF0707ʟ&#FF0707ɪ&#FF0707ᴢ&#FF0606ᴢ&#FF0606ᴀ&#FF0505ʙ&#FF0505ɪ&#FF0404ʟ&#FF0404ᴇ &#FF0404ꜱ&#FF0404ᴏ&#FF0404ʟ&#FF0404ᴏ &#FF0404ᴅ&#FF0404ᴀ &#FF0404ᴜ&#FF0404ɴ &#FF0404ᴘ&#FF0404ʟ&#FF0404ᴀ&#FF0404ʏ&#FF0404ᴇ&#FF0404ʀ", prefix));
    }

    @Override
    public void sendUsage(CommandSender sender, boolean prefix, String usage) {
        sender.sendMessage(getMessage("&#EE3131ꜱ&#EF3232ɪ&#F03232ɴ&#F23333ᴛ&#F33333ᴀ&#F43434ꜱ&#F53434ꜱ&#F73535ɪ &#F93636ᴇ&#FA3737ʀ&#FB3737ʀ&#FD3838ᴀ&#FE3838ᴛ&#FF3939ᴀ: %usage%".replaceAll("%usage", usage), prefix));
    }

    @Override
    public void sendExecutionError(CommandSender sender, boolean prefix, Exception e) {
        sender.sendMessage(getMessage("&#EE3131ᴇ&#EF3232ʀ&#F03232ʀ&#F23333ᴏ&#F33333ʀ&#F43434ᴇ&#F53434! &#F83636ʀ&#F93636ɪ&#FA3737ᴘ&#FB3737ʀ&#FD3838ᴏ&#FE3838ᴠ&#FF3939ᴀ", prefix));
        e.printStackTrace();
    }

    private String getMessage(String message, boolean prefix) {
        return ((prefix ? Locale.PREFIX.getMessage(false) : "") + Locale.translate(message));
    }

}
