package com.venom.venomcore.plugin.external.skyblock;

import com.venom.venomcore.plugin.external.Hook;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class SkyBlockHook implements Hook {

    /**
     * Create an island for a player.
     * @param player The player that will be the owner of the new island.
     * @param schematic The schematic name to paste. You can leave it null.
     */
    public abstract void createIsland(Player player, @Nullable String schematic);

    /**
     * Delete an island of a player.
     * @param player The owner of the island.
     */
    public abstract void deleteIsland(Player player);

    /**
     * Reset an island of a player.
     * @param player The owner of the island.
     */
    public abstract void resetIsland(Player player);

    /**
     * Get the total island count.
     * @return All islands in the server.
     */
    public abstract int getIslandCount();

    /**
     * Get all the schematics.
     * Do note that iridium skyblock does not have any.
     * @return All the schematics names.
     */
    public abstract List<String> getAllSchematics();

    /**
     * Checks if an island exist in the given location
     * @param location The location to check.
     * @return The created island.
     */
    public abstract IslandHook getIslandAt(Location location);

    /**
     * Get the island from an owner.
     * @param uuid The owners uuid.
     * @return The island. If player does not own an island, null.
     */
    public abstract IslandHook getIslandOwnedBy(UUID uuid);

    /**
     * Gets the island world.
     * @return The world that contains all islands.
     */
    public abstract World getIslandWorld();

    /**
     * Get the owner from an island location.
     * @param location The location.
     * @return The owners UUID.
     */
    public abstract UUID getOwner(Location location);

    /**
     * Checks if the player is on an island.
     * @param player The player to check.
     * @return True if the player is on an island.
     */
    public abstract boolean playerIsOnIsland(Player player);
}
