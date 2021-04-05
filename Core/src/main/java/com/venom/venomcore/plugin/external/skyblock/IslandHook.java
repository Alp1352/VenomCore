package com.venom.venomcore.plugin.external.skyblock;

import com.venom.venomcore.plugin.compatibility.CompatibleBiome;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class IslandHook {
    /**
     * Get the biome of this island.
     * @return The biome.
     */
    public abstract CompatibleBiome getBiome();

    /**
     * Get the center location of this island.
     * @return The center location.
     */
    public abstract Location getCenter();

    /**
     * Get the maximum location of this island.
     * @return The maximum location.
     */
    public abstract Location maxLoc();

    /**
     * Get the minimum location of this island.
     * @return The minimum location.
     */
    public abstract Location minLoc();

    /**
     * Get the spawn point of this island.
     * @return The spawn point.
     */
    public abstract Location getSpawnPoint();

    /**
     * Transfer this island to a new player.
     * @param newPlayer The player to transfer the island to.
     */
    public abstract void transferIsland(Player newPlayer);

    /**
     * Make a player visit this island.
     * @param player The player to visit this island.
     */
    public abstract void visitIsland(Player player);

    /**
     * Get the owner of this island.
     * @return The owners unique id.
     */
    public abstract UUID getOwner();
}
