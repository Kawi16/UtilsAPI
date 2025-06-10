package it.alessandrozap.defaults;

import it.alessandrozap.interfaces.IMessageProvider;
import it.alessandrozap.managers.messages.Locale;
import org.bukkit.command.CommandSender;

public class DefaultMessageProvider implements IMessageProvider {

    @Override
    public void sendNoPermission(CommandSender sender) {
        sender.sendMessage(Locale.translate("&#FF0C0Cᴘ&#FF0B0Bᴇ&#FF0A0Aʀ&#FF0909ᴍ&#FF0808ᴇ&#FF0707ꜱ&#FF0606ꜱ&#FF0505ᴏ &#FF0404ᴍ&#FF0404ᴀ&#FF0404ɴ&#FF0404ᴄ&#FF0404ᴀ&#FF0404ɴ&#FF0404ᴛ&#FF0404ᴇ"));
    }

    @Override
    public void sendSubCommandNotFound(CommandSender sender, String input) {
        sender.sendMessage(Locale.translate("&#EE3131ꜱ&#EF3131ᴏ&#EF3232ᴛ&#F03232ᴛ&#F13232ᴏ&#F23333ᴄ&#F23333ᴏ&#F33333ᴍ&#F43434ᴀ&#F53434ɴ&#F53434ᴅ&#F63535ᴏ &#F83636ɴ&#F83636ᴏ&#F93636ɴ &#FB3737ᴛ&#FB3737ʀ&#FC3838ᴏ&#FD3838ᴠ&#FE3838ᴀ&#FE3939ᴛ&#FF3939ᴏ: " + input));
    }

    @Override
    public void sendPlayerOnly(CommandSender sender) {
        sender.sendMessage(Locale.translate("&#FF0C0Cᴄ&#FF0C0Cᴏ&#FF0B0Bᴍ&#FF0B0Bᴀ&#FF0A0Aɴ&#FF0A0Aᴅ&#FF0909ᴏ &#FF0909ᴜ&#FF0808ᴛ&#FF0808ɪ&#FF0707ʟ&#FF0707ɪ&#FF0707ᴢ&#FF0606ᴢ&#FF0606ᴀ&#FF0505ʙ&#FF0505ɪ&#FF0404ʟ&#FF0404ᴇ &#FF0404ꜱ&#FF0404ᴏ&#FF0404ʟ&#FF0404ᴏ &#FF0404ᴅ&#FF0404ᴀ &#FF0404ᴜ&#FF0404ɴ &#FF0404ᴘ&#FF0404ʟ&#FF0404ᴀ&#FF0404ʏ&#FF0404ᴇ&#FF0404ʀ"));
    }

    @Override
    public void sendUsage(CommandSender sender, String usage) {
        sender.sendMessage(Locale.translate("&#EE3131ꜱ&#EF3232ɪ&#F03232ɴ&#F23333ᴛ&#F33333ᴀ&#F43434ꜱ&#F53434ꜱ&#F73535ɪ &#F93636ᴇ&#FA3737ʀ&#FB3737ʀ&#FD3838ᴀ&#FE3838ᴛ&#FF3939ᴀ: %usage%"));
    }

    @Override
    public void sendExecutionError(CommandSender sender, Exception e) {
        sender.sendMessage(Locale.translate("&#EE3131ᴇ&#EF3232ʀ&#F03232ʀ&#F23333ᴏ&#F33333ʀ&#F43434ᴇ&#F53434! &#F83636ʀ&#F93636ɪ&#FA3737ᴘ&#FB3737ʀ&#FD3838ᴏ&#FE3838ᴠ&#FF3939ᴀ"));
        e.printStackTrace();
    }

}
