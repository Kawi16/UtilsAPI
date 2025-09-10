package it.alessandrozap.utilsapi.managers.listeners;

import it.alessandrozap.utilsapi.UtilsAPI;
import it.alessandrozap.utilsapi.logger.LogType;
import it.alessandrozap.utilsapi.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.Bukkit.getServer;

public class ListenersManager {

    public long registerAll(boolean outputTime) {
        String pluginPackage = getClass().getPackage().getName();
        Set<Class<? extends ListenerImpl>> listenerClasses = new HashSet<>();
        long startTime = System.currentTimeMillis();
        int countListener = 0;

        for(Class<?> clazz : UtilsAPI.getInstance().getPackageClassesList()) {
            if(ListenerImpl.class.isAssignableFrom(clazz)) listenerClasses.add(clazz.asSubclass(ListenerImpl.class));
        }

        for (Class<? extends ListenerImpl> clazz : listenerClasses) {
            if (clazz.equals(ListenerImpl.class)) continue;
            try {
                ListenerImpl listener = clazz.getDeclaredConstructor().newInstance();
                if(register(listener)) countListener++;
            } catch (Exception e) {
                Logger.log("Error loading listener: " + clazz.getSimpleName() + " - If you see this, open an issue on GitHub (https://github.com/Kawi16)", LogType.ERROR);
                e.printStackTrace();
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        if(outputTime) Logger.log(countListener +  " listeners registered in " + duration + " ms.", LogType.INFO);
        else Logger.log(countListener +  " listeners registered.", LogType.INFO);
        return duration;
    }

    public boolean register(ListenerImpl listener) {
        if (listener.load()) {
            boolean allDependenciesLoaded = true;
            for(int i = 0; i < listener.dependencies().length && allDependenciesLoaded; i++) {
                Plugin plugin = Bukkit.getPluginManager().getPlugin(listener.dependencies()[i]);
                if(plugin == null || !plugin.isEnabled()) allDependenciesLoaded = false;
            }
            if(allDependenciesLoaded) getServer().getPluginManager().registerEvents(listener, UtilsAPI.getInstance().getPlugin());
            return allDependenciesLoaded;
        }
        return false;
    }

    public void unregisterAll() {
        HandlerList.unregisterAll(UtilsAPI.getInstance().getPlugin());
    }
}
