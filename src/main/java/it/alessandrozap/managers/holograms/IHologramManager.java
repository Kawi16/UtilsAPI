package it.alessandrozap.managers.holograms;

import org.bukkit.Location;

import java.util.List;
import java.util.Optional;

public interface IHologramManager {

    /**
     * Create a new hologram at the specified location with the given lines.
     *
     * @param location Location where the hologram will be created
     * @param lines Text lines to display in the hologram
     * @return The created hologram instance
     */
    IHologram createHologram(Location location, List<String> lines);

    /**
     * Retrieve a hologram by its unique identifier.
     *
     * @param id The ID of the hologram to retrieve
     * @return An {@link Optional} containing the hologram if found, or empty if not found
     */
    Optional<IHologram> getHologram(String id);

    /**
     * Remove the specified hologram.
     *
     * @param hologram The hologram instance to remove
     */
    void removeHologram(IHologram hologram);
}