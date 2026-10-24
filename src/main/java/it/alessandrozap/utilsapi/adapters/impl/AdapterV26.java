package it.alessandrozap.utilsapi.adapters.impl;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

import java.util.List;
import java.util.Locale;

public class AdapterV26 extends AdapterV1_20 {
    @Override
    public String getWorldId(World world) {
        NamespacedKey k = keyOf(world);
        if (k == null) return super.getWorldId(world);
        return k.getNamespace() + "/" + k.getKey();
    }

    @Override
    public World findWorld(String id) {
        if (id == null || id.isEmpty()) return null;

        NamespacedKey key = parseKey(id);
        if (key != null) {
            World w = Bukkit.getWorld(key);
            if (w != null) return w;
        }

        World w = super.findWorld(id);
        if (w != null) return w;

        List<World> worlds = Bukkit.getWorlds();
        if (worlds.isEmpty()) return null;

        String main = worlds.get(0).getName();
        NamespacedKey legacy = null;
        if (id.equalsIgnoreCase(main)) legacy = NamespacedKey.minecraft("overworld");
        else if (id.equalsIgnoreCase(main + "_nether")) legacy = NamespacedKey.minecraft("the_nether");
        else if (id.equalsIgnoreCase(main + "_the_end")) legacy = NamespacedKey.minecraft("the_end");
        if (legacy != null) {
            w = Bukkit.getWorld(legacy);
            if (w != null) return w;
        }

        String path = id.substring(id.lastIndexOf('/') + 1).toLowerCase(Locale.ROOT);
        for (World world : worlds) {
            NamespacedKey k = keyOf(world);
            if (k != null && k.getKey().equals(path)) return world;
        }
        return null;
    }

    private static NamespacedKey keyOf(World world) {
        return ((Object) world) instanceof Keyed keyed ? keyed.getKey() : null;
    }

    private static NamespacedKey parseKey(String id) {
        int s = id.indexOf('/');
        String raw = s > 0 ? id.substring(0, s) + ":" + id.substring(s + 1) : id;
        if (raw.indexOf(':') <= 0) return null;
        try {
            return NamespacedKey.fromString(raw.toLowerCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
