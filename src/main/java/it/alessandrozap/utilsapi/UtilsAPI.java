package it.alessandrozap.utilsapi;

import it.alessandrozap.utilsapi.logger.Logger;
import it.alessandrozap.utilsapi.managers.commands.CommandManager;
import it.alessandrozap.utilsapi.managers.listeners.ListenersManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private boolean initialized = false;
    @Getter
    private List<Class<?>> packageClassesList = new ArrayList<>();

    public UtilsAPI(JavaPlugin plugin, String prefix) throws Exception {
        if (plugin == null) throw new Exception("Plugin cannot be null!");
        this.plugin = plugin;
        this.prefix = prefix;

        Logger.setConsolePrefix(prefix);
        UtilsAPI.instance = this;
        init();
    }

    public void init() {
        try {
            String basePackage = plugin.getClass().getPackageName();
            ClassLoader pluginClassLoader = plugin.getClass().getClassLoader();

            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(basePackage, pluginClassLoader))
                    .addClassLoaders(pluginClassLoader)
                    .setScanners(Scanners.SubTypes.filterResultsBy(s -> true))
            );

            Set<String> allClassNames = reflections.getAll(Scanners.SubTypes);
            for (String className : allClassNames) {
                try {
                    Class<?> clazz = Class.forName(className, false, pluginClassLoader);
                    if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) packageClassesList.add(clazz);
                } catch (ClassNotFoundException | NoClassDefFoundError ignored) {}
            }

            listenersManager = new ListenersManager();
            commandManager = new CommandManager();
            this.initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (listenersManager != null) listenersManager.unregisterAll();
        Bukkit.getScheduler().cancelTasks(plugin);
        if (commandManager != null) commandManager.reset();
        packageClassesList.clear();
        UtilsAPI.instance = null;
        initialized = false;
    }
}
