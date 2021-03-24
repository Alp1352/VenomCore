package com.Venom.VenomCore.External.Skyblock.Types;

import com.Venom.VenomCore.External.Skyblock.IslandHook;
import com.Venom.VenomCore.External.Skyblock.SkyBlockHook;
import com.iridium.iridiumskyblock.User;
import com.iridium.iridiumskyblock.managers.IslandManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class IridiumSkyBlockHook extends SkyBlockHook {
    @Override
    public void createIsland(Player p, @Nullable String schematic) {
        IslandManager.createIsland(p);
    }

    @Override
    public void deleteIsland(Player p) {
        IslandManager.removeIsland(User.getUser(p.getUniqueId()).getIsland());
    }

    @Override
    public void resetIsland(Player p) {

    }

    @Override
    public int getIslandCount() {
        return IslandManager.getLoadedIslands().size();
    }

    @Override
    public IslandHook getIslandAt(Location location) {
        return null;
    }

    @Override
    public IslandHook getIslandOwnedBy(UUID uuid) {
        return null;
    }

    @Override
    public World getIslandWorld() {
        return IslandManager.getWorld();
    }

    @Override
    public UUID getOwner(Location location) {
        return UUID.fromString(IslandManager.getIslandViaLocation(location).owner);
    }

    @Override
    public boolean playerIsOnIsland(Player player) {
        return IslandManager.getIslandViaLocation(player.getLocation()) != null;
    }

    @Override
    public String getName() {
        return "IridiumSkyBlock";
    }
}
