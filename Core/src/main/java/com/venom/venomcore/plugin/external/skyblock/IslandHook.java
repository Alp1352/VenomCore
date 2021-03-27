package com.venom.venomcore.plugin.external.skyblock;

import com.venom.venomcore.plugin.compatibility.CompatibleBiome;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class IslandHook {
    public abstract CompatibleBiome getBiome();

    public abstract Location getCenter();

    public abstract Location maxLoc();

    public abstract Location minLoc();

    public abstract Location getSpawnPoint();

    public abstract void transferIsland(Player newPlayer);

    public abstract void visitIsland(Player player);

    public abstract UUID getOwner();
}
