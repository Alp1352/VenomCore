package com.venom.venomcore.External.Skyblock.Types;

import com.songoda.skyblock.api.SkyBlockAPI;
import com.songoda.skyblock.api.island.IslandManager;
import com.songoda.skyblock.api.structure.Structure;
import com.songoda.skyblock.api.structure.StructureManager;
import com.songoda.skyblock.island.IslandWorld;
import com.venom.venomcore.External.Skyblock.IslandHook;
import com.venom.venomcore.External.Skyblock.SkyBlockHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class FabledSkyBlockHook extends SkyBlockHook {
    private final IslandManager islandManager = SkyBlockAPI.getIslandManager();
    private final StructureManager structureManager = SkyBlockAPI.getStructureManager();

    @Override
    public void createIsland(Player p, @Nullable String schematic) {
        List<Structure> structures = structureManager.getStructures();
        Structure pasteStructure = structures.stream().filter(structure -> structure.getName().equalsIgnoreCase(schematic)).findFirst().orElse(null);
        islandManager.createIsland(p, pasteStructure);
    }

    @Override
    public void deleteIsland(Player p) {
        islandManager.deleteIsland(islandManager.getIsland(p),true);
    }

    @Override
    public void resetIsland(Player p) {
        islandManager.resetIsland(islandManager.getIsland(p));
    }

    @Override
    public int getIslandCount() {
        return islandManager.getIslands().size();
    }

    @Override
    public IslandHook getIslandAt(Location location) {
        return new FabledSkyBlockIsland(SkyBlockAPI.getIslandManager().getIslandAtLocation(location));
    }

    @Override
    public IslandHook getIslandOwnedBy(UUID uuid) {
        return new FabledSkyBlockIsland(SkyBlockAPI.getIslandManager().getIsland(Bukkit.getPlayer(uuid)));
    }

    @Override
    public World getIslandWorld() {
        return SkyBlockAPI.getImplementation().getWorldManager().getWorld(IslandWorld.Normal);
    }

    @Override
    public UUID getOwner(Location location) {
        return islandManager.getIslandAtLocation(location).getOwnerUUID();
    }

    @Override
    public boolean playerIsOnIsland(Player player) {
        return islandManager.isPlayerAtAnIsland(player);
    }

    @Override
    public String getName() {
        return "FabledSkyBlock";
    }
}
