package it.alessandrozap;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class UtilsAPI {

    @Getter
    private static UtilsAPI instance;
    @Getter
    private final JavaPlugin plugin;
    @Getter
    private final String prefix;
    @Getter
    private boolean inizialized = false;

    public UtilsAPI(JavaPlugin plugin, String prefix) throws Exception {
        if(plugin == null) throw new Exception();
        this.plugin = plugin;
        this.prefix = prefix;

        UtilsAPI.instance = this;
        init();
    }

    public void init() {
        this.inizialized = true;
    }

}
