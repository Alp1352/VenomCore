package com.venom.venomcore.External.Skyblock;

import com.venom.venomcore.External.Hook;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class SkyBlockHook implements Hook {

    public abstract void createIsland(Player p, @Nullable String schematic);

    public abstract void deleteIsland(Player p);

    public abstract void resetIsland(Player p);

    public abstract int getIslandCount();

    public abstract IslandHook getIslandAt(Location location);

    public abstract IslandHook getIslandOwnedBy(UUID uuid);

    public abstract World getIslandWorld();

    public abstract UUID getOwner(Location location);

    public abstract boolean playerIsOnIsland(Player player);
}
