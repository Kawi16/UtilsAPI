package it.alessandrozap.utilsapi.adapters.impl;

import it.alessandrozap.utilsapi.adapters.VersionAdapter;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class AdapterV1_20 implements VersionAdapter {

    @Override
    public String getWorldId(World world) {
        return world.getName();
    }

    @Override
    public World findWorld(String id) {
        if (id == null || id.isEmpty()) return null;
        World w = Bukkit.getWorld(id);
        if (w == null) {
            int s = id.indexOf('/');
            if (s > 0) w = Bukkit.getWorld(id.substring(s + 1));
        }
        return w;
    }
}
