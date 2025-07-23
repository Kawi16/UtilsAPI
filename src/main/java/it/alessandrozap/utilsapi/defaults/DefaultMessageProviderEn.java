package it.alessandrozap.utilsapi.defaults;

import it.alessandrozap.utilsapi.interfaces.IMessageProvider;
import it.alessandrozap.utilsapi.managers.messages.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DefaultMessageProviderEn implements IMessageProvider {

    @Override
    public List<String> formatSubCommandHelp(CommandSender sender, String mainCommandName, String subCommandName, String usage, String description, String permission, String[] aliases) {
        List<String> list = new ArrayList<>();
        list.add(String.format("&#929B92• &f/%s &#929B92- %s", usage != null && !usage.isEmpty() ? usage : (mainCommandName + " " + subCommandName), description));
        if(permission != null && !permission.isEmpty()) list.add("  &#929B92" + ((sender instanceof Player) ? "ᴘᴇʀᴍɪꜱꜱɪᴏɴ: " : "Permission: ") + permission);
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
        if(permission != null && !permission.isEmpty()) list.add("  &#929B92" + ((sender instanceof Player) ? "ᴘᴇʀᴍɪꜱꜱɪᴏɴ: " : "Permission: ") + permission);
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
        sender.sendMessage(getMessage("&#FF2222ᴍ&#FE2323ɪ&#FC2424ꜱ&#FB2525ꜱ&#FA2626ɪ&#F92727ɴ&#F72828ɢ &#F52A2Aᴘ&#F32C2Cᴇ&#F22D2Dʀ&#F12E2Eᴍ&#EF2F2Fɪ&#EE3030ꜱ&#ED3131ꜱ&#EC3232ɪ&#EA3333ᴏ&#E93434ɴ", prefix));
    }

    @Override
    public void sendSubCommandNotFound(CommandSender sender, boolean prefix, String input) {
        sender.sendMessage(getMessage("&#FF2222ꜱ&#FE2323ᴜ&#FD2424ʙ&#FC2525ᴄ&#FA2626ᴏ&#F92727ᴍ&#F82828ᴍ&#F72929ᴀ&#F62A2Aɴ&#F52B2Bᴅ &#F22C2Cɴ&#F12D2Dᴏ&#F02E2Eᴛ &#EE3030ꜰ&#EC3131ᴏ&#EB3232ᴜ&#EA3333ɴ&#E93434ᴅ", prefix));
    }

    @Override
    public void sendPlayerOnly(CommandSender sender, boolean prefix) {
        sender.sendMessage(getMessage("&#FF2222ᴛ&#FE2222ʜ&#FE2323ɪ&#FD2323ꜱ &#FC2424ᴄ&#FC2525ᴏ&#FB2525ᴍ&#FA2626ᴍ&#FA2626ᴀ&#F92727ɴ&#F92727ᴅ &#F82828ᴄ&#F72828ᴀ&#F72929ɴ &#F52A2Aᴏ&#F52A2Aɴ&#F42B2Bʟ&#F42B2Bʏ &#F32C2Cʙ&#F22D2Dᴇ &#F12E2Eʀ&#F02E2Eᴜ&#F02E2Eɴ &#EF2F2Fʙ&#EE3030ʏ &#ED3131ᴀ &#EC3232ᴘ&#EB3232ʟ&#EB3333ᴀ&#EA3333ʏ&#EA3434ᴇ&#E93434ʀ", prefix));
    }

    @Override
    public void sendUsage(CommandSender sender, boolean prefix, String usage) {
        sender.sendMessage(getMessage("&#FF2222ᴡ&#FE2323ʀ&#FD2424ᴏ&#FC2525ɴ&#FA2626ɢ &#F82828ᴜ&#F72929ꜱ&#F62A2Aᴀ&#F52B2Bɢ&#F32B2Bᴇ&#F22C2C: %usage%".replaceAll("%usage", usage), prefix));
    }

    @Override
    public void sendExecutionError(CommandSender sender, boolean prefix, Exception e) {
        sender.sendMessage(getMessage("&#FF2222ᴇ&#FD2424ʀ&#FB2525ʀ&#F92727ᴏ&#F72929ʀ&#F52A2A! &#F12D2Dʀ&#EF2F2Fᴇ&#ED3131ᴛ&#EB3232ʀ&#E93434ʏ", prefix));
        e.printStackTrace();
    }

    private String getMessage(String message, boolean prefix) {
        return ((prefix ? Locale.PREFIX.getMessage(false) : "") + Locale.translate(message));
    }
}