package com.Venom.VenomCore.External.Skyblock.Types;

import com.Venom.VenomCore.Compatibility.CompatibleBiome;
import com.Venom.VenomCore.External.Skyblock.IslandHook;
import com.iridium.iridiumskyblock.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class IridiumSkyBlockIsland extends IslandHook {
    private final Island island;
    public IridiumSkyBlockIsland(Island island) {
        this.island = island;
    }

    @Override
    public CompatibleBiome getBiome() {
        assert island.biome.getBiome() != null;
        return CompatibleBiome.matchCompatibleBiome(island.biome.getBiome());
    }

    @Override
    public Location getCenter() {
        return island.center;
    }

    @Override
    public Location maxLoc() {
        return island.pos2;
    }

    @Override
    public Location minLoc() {
        return island.pos1;
    }

    @Override
    public Location getSpawnPoint() {
        return island.home;
    }

    @Override
    public void transferIsland(Player newPlayer) {
        island.setOwner(Bukkit.getOfflinePlayer(getOwner()));
    }

    @Override
    public void visitIsland(Player player) {
        island.teleportHome(player);
    }

    @Override
    public UUID getOwner() {
        return UUID.fromString(island.owner);
    }
}
