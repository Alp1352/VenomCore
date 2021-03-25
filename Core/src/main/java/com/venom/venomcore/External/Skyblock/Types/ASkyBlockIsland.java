package com.venom.venomcore.External.Skyblock.Types;

import com.venom.venomcore.Compatibility.CompatibleBiome;
import com.venom.venomcore.External.Skyblock.IslandHook;
import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.Settings;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ASkyBlockIsland extends IslandHook {
    private final Island island;
    private final Location maxLocation;
    private final Location minLocation;

    public ASkyBlockIsland(Island island) {
        this.island = island;
        minLocation = new Location(island.getCenter().getWorld(), island.getMinX(), 0, island.getMinZ());
        maxLocation = new Location(island.getCenter().getWorld(), island.getCenter().getBlockX() + Settings.islandDistance / 2, Settings.islandHeight, island.getCenter().getBlockZ() + Settings.islandDistance / 2);
    }

    @Override
    public CompatibleBiome getBiome() {
        return CompatibleBiome.matchCompatibleBiome(island.getBiome());
    }

    @Override
    public Location getCenter() {
        return island.getCenter();
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
        return island.getSpawnPoint();
    }

    @Override
    public void transferIsland(Player newPlayer) {
        ASkyBlock.getPlugin().getGrid().transferIsland(island.getOwner(), newPlayer.getUniqueId());
    }

    @Override
    public void visitIsland(Player player) {
        player.teleport(island.getCenter());
    }

    @Override
    public UUID getOwner() {
        return island.getOwner();
    }
}
