package it.alessandrozap.utilsapi.logger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
public enum LogType {
    ERROR(ChatColor.RED), WARN(ChatColor.YELLOW), INFO(ChatColor.WHITE), START(ChatColor.DARK_RED);

    @Getter
    private final ChatColor color;
}