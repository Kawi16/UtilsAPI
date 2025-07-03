package it.alessandrozap.utilsapi.managers.listeners;

import org.bukkit.event.Listener;

public interface ListenerImpl extends Listener {

    default boolean load() {
        return true;
    }
}