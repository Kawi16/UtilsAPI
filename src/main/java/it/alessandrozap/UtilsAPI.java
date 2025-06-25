package it.alessandrozap;

import it.alessandrozap.managers.commands.CommandManager;
import it.alessandrozap.managers.listeners.ListenersManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class UtilsAPI {

    @Getter
    private static UtilsAPI instance;
    @Getter
    private ListenersManager listenersManager;
    @Getter
    private CommandManager commandManager;
    @Getter
    private final JavaPlugin plugin;
    @Getter
    private final String prefix;
    @Getter
    private boolean inizialized = false;
    @Getter
    private List<Class> packageClassesList = new ArrayList<>();
    @Getter
    private List<File> resourceList = new ArrayList<>();

    public UtilsAPI(JavaPlugin plugin, String prefix) throws Exception {
        if(plugin == null) throw new Exception();
        this.plugin = plugin;
        this.prefix = prefix;

        UtilsAPI.instance = this;
        init();
    }

    public void init() {
        /*
            I use this for not cycle 2 times (1 time for listeners and 1 time for commands)
         */
        String path = plugin.getClass().getPackage().getName().replace(".", "/") + "/";
        try {
            JarFile jar = new JarFile(new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()));
            Enumeration<JarEntry> entries = jar.entries();
            while(entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if(name.startsWith(path) && name.endsWith(".class")) {
                    String className = name.replace("/", ".").replace(".class", "");
                    try {
                        Class<?> clazz = Class.forName(className, true, plugin.getClass().getClassLoader());
                        packageClassesList.add(clazz);
                    } catch(Exception ignored) {}
                }
            }
            listenersManager = new ListenersManager();
            commandManager = new CommandManager();
            this.inizialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if(listenersManager != null) listenersManager.unregisterAll();
        Bukkit.getScheduler().cancelTasks(plugin);
        if(commandManager != null) commandManager.reset();
        packageClassesList.clear();
        resourceList.clear();
        UtilsAPI.instance = null;
        inizialized = false;
    }

}
