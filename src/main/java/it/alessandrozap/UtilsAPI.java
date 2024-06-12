package it.alessandrozap;

import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

public class UtilsAPI {

    @Getter
    private static UtilsAPI instance;

    @Getter
    private final JavaPlugin plugin;
    private boolean inizialized = false;

    private Listener listener;

    public UtilsAPI(JavaPlugin plugin) throws Exception {
        if(plugin == null) throw new Exception();
        this.plugin = plugin;

        UtilsAPI.instance = this;
    }

    public void init() {
        //this.plugin.getServer().getPluginManager().registerEvents();
        this.inizialized = true;
    }

    /*
    public void unload() {
        UtilsAPI.getScheduler().cancelTasks();
        HandlerList.unregisterAll(this.listener);
    }
    */


    /*
    public static TaskScheduler getScheduler() {
        return UniversalScheduler.getScheduler(InventoryAPI.getInstance().getPlugin());
    }

    public static InventoryAPI getInstance() {
        return InventoryAPI.instance;
    }*/

}
