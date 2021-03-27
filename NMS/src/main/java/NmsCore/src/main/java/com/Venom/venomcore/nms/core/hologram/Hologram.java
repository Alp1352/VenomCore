package com.venom.venomcore.nms.core.hologram;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public interface Hologram {
    /**
     * Creates the hologram.
     * @return The instance of the created hologram.
     */
    Hologram create();

    /**
     * Shows all lines of the hologram to all players.
     */
    void show();

    /**
     * Shows a line of the hologram to all players.
     * @param line The line to show.
     */
    void show(int line);

    /**
     * Shows a hologram to a player.
     * @param player The player who will see the hologram.
     */
    void show(Player player);

    /**
     * Shows a line of the hologram to a player.
     * @param player The player who will see the hologram.
     * @param line The line to show.
     */
    void show(Player player, int line);

    /**
     * Hides all lines of the hologram from all players.
     */
    void hide();

    /**
     * Hides a line of the hologram from all players.
     * @param line The line to hide.
     */
    void hide(int line);

    /**
     * Hides the hologram from a player.
     * @param player The player who no longer can see the hologram.
     */
    void hide(Player player);

    /**
     * Hides a line of the hologram from a player.
     * @param player The player who no longer can see the line of the hologram.
     * @param line The line to hide.
     */
    void hide(Player player, int line);

    /**
     * Removes the hologram and its entities.
     */
    void destroy();

    /**
     * Removes a line of the hologram and its entity.
     * @param line The line to remove.
     */
    void destroy(int line);

    /**
     * Updates all lines of the hologram.
     */
    void update();

    /**
     * Updates a line of the hologram.
     * @param line The line to update.
     */
    void update(int line);

    /**
     * Teleports the hologram.
     * @param location The location to teleport the hologram to.
     */
    void teleport(Location location);

    /**
     * @return True if the hologram is created, false if the hologram needs to be created.
     */
    boolean isCreated();

    /**
     * @return The current location of the hologram.
     */
    Location getLocation();

    /**
     * Sets all the lines of the hologram.
     * @param lines New lines.
     */
    void setLines(List<String> lines);

    /**
     * Sets all the lines of the hologram.
     * @param text New lines of the hologram.
     */
    void setLines(String... text);

    /**
     * Sets a line of the hologram.
     * @param line The line to set.
     * @param text The new line.
     */
    void setLine(int line, String text);

    /**
     * Adds a line to the hologram.
     * @param text The text to add.
     */
    void addLine(String text);

    HologramTimer createTimer(int seconds);

    /**
     * @return All the lines in the hologram.
     */
    List<String> getLines();

    /**
     * If you decided to edit this, you should always call the update method.
     * @return All Armor Stand entities in the hologram.
     */
    List<LivingEntity> toBukkitEntities();
}
