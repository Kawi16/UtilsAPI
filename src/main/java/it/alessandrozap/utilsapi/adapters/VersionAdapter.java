package it.alessandrozap.utilsapi.adapters;

import it.alessandrozap.utilsapi.adapters.impl.AdapterV1_20;
import it.alessandrozap.utilsapi.adapters.impl.AdapterV26;
import org.bukkit.World;

public interface VersionAdapter {

    /**
     * @param world World to identify
     * @return World id to store (e.g. in configs), resolvable with {@link #findWorld(String)}
     */
    String getWorldId(World world);

    /**
     * Finds a world by its id. Accepts both legacy names ("world", "world_nether")
     * and namespaced keys ("minecraft:overworld", "minecraft/the_nether").
     * @param id World id
     * @return The world found, or null
     */
    World findWorld(String id);

    /**
     * @return Adapter for the server version, does not require {@link it.alessandrozap.utilsapi.UtilsAPI} to be initialized
     */
    static VersionAdapter get() {
        return VersionAdapterHolder.INSTANCE;
    }

    static VersionAdapter of(ServerVersion version) {
        if (version.isAtLeast(26, 2)) return new AdapterV26();
        return new AdapterV1_20();
    }
}