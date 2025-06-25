package it.alessandrozap.managers.holograms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface IHologram {
    /**
     * Get the hologram's unique identifier.
     *
     * @return The hologram ID
     */
    String getId();

    /**
     * Show the hologram to a player.
     *
     * @param player Player to show the hologram to
     */
    void show(Player player);

    /**
     * Hide the hologram from a player.
     *
     * @param player Player to hide the hologram from
     */
    void hide(Player player);

    /**
     * Check if the hologram is visible to a specific player.
     *
     * @param player Player to check visibility for
     * @return {@code true} if the hologram is visible to the player, {@code false} otherwise
     */
    boolean isVisibleTo(Player player);

    /**
     * Delete the hologram permanently.
     */
    void delete();

    /**
     * Add a text line to the hologram.
     *
     * @param line Text line to add
     */
    void addLine(String line);

    /**
     * Remove a text line at the specified index.
     *
     * @param index Index of the line to remove
     */
    void removeLine(int index);

    /**
     * Set a text line at the specified index.
     *
     * @param index Index of the line to be set
     * @param line New line
     */
    void setLine(int index, String line);

    /**
     * Update the hologram's text lines.
     *
     * @param lines New list of lines for the hologram
     */
    void setLines(List<String> lines);

    /**
     * Get the current location of the hologram.
     *
     * @return The hologram location
     */
    Location getLocation();

    /**
     * Set a new location for the hologram.
     *
     * @param location New location for the hologram
     */
    void setLocation(Location location);
}
