package com.venom.venomcore.External.Skyblock.Types;

import com.songoda.skyblock.api.SkyBlockAPI;
import com.songoda.skyblock.api.island.Island;
import com.songoda.skyblock.api.island.IslandEnvironment;
import com.songoda.skyblock.api.island.IslandWorld;
import com.venom.venomcore.Compatibility.CompatibleBiome;
import com.venom.venomcore.External.Skyblock.IslandHook;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FabledSkyBlockIsland extends IslandHook {
    private final Island island;
    private final Location minLocation;
    private final Location maxLocation;

    public FabledSkyBlockIsland(Island island) {
        this.island = island;
        Location islandLocation = island.getLocation(IslandWorld.OVERWORLD, IslandEnvironment.ISLAND);

        World world = islandLocation.getWorld();

        if (world == null) {
            minLocation = null;
            maxLocation = null;
            return;
        }

        minLocation = new Location(world, islandLocation.getBlockX() - island.getRadius(), 0, islandLocation.getBlockZ() - island.getRadius());
        maxLocation = new Location(world, islandLocation.getBlockX() + island.getRadius(), world.getMaxHeight(), islandLocation.getBlockZ() + island.getRadius());
    }

    @Override
    public CompatibleBiome getBiome() {
        return CompatibleBiome.matchCompatibleBiome(island.getBiome());
    }

    @Override
    public Location getCenter() {
        return island.getLocation(IslandWorld.OVERWORLD, IslandEnvironment.ISLAND);
    }

    @Override
    public Location maxLoc() {
        return maxLocation;
    }

    @Override
    public Location minLoc() {
        return minLocation;
    }

    @Override
    public Location getSpawnPoint() {
        return island.getLocation(IslandWorld.OVERWORLD, IslandEnvironment.MAIN);
    }

    @Override
    public void transferIsland(Player newPlayer) {
        SkyBlockAPI.getIslandManager().giveOwnership(island, newPlayer);
    }

    @Override
    public void visitIsland(Player player) {
        SkyBlockAPI.getIslandManager().visitIsland(player, island);
    }

    @Override
    public UUID getOwner() {
        return island.getOwnerUUID();
    }
}
