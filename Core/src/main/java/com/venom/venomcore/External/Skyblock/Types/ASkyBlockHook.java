package com.venom.venomcore.External.Skyblock.Types;

import com.venom.venomcore.External.Skyblock.IslandHook;
import com.venom.venomcore.External.Skyblock.SkyBlockHook;
import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.schematics.Schematic;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ASkyBlockHook extends SkyBlockHook {
    @Override
    public void createIsland(Player p, @Nullable String schematic) {
        if (schematic == null) {
            ASkyBlock.getPlugin().getIslandCmd().newIsland(p);
        } else {
            Schematic pasteSchem = ASkyBlock.getPlugin().getIslandCmd().getSchematics(p, true).stream().filter(paste -> paste.getName().equalsIgnoreCase(schematic)).findFirst().orElse(null);
            ASkyBlock.getPlugin().getIslandCmd().newIsland(p, pasteSchem);
        }
    }

    @Override
    public void deleteIsland(Player p) {
        ASkyBlock.getPlugin().deletePlayerIsland(p.getUniqueId(), true);
    }

    @Override
    public void resetIsland(Player p) {
        ASkyBlock.getPlugin().resetPlayer(p);
    }

    @Override
    public int getIslandCount() {
        return ASkyBlockAPI.getInstance().getIslandCount();
    }

    @Override
    public IslandHook getIslandAt(Location location) {
        return new ASkyBlockIsland(ASkyBlockAPI.getInstance().getIslandAt(location));
    }

    @Override
    public IslandHook getIslandOwnedBy(UUID uuid) {
        return new ASkyBlockIsland(ASkyBlockAPI.getInstance().getIslandOwnedBy(uuid));
    }

    @Override
    public World getIslandWorld() {
        return ASkyBlockAPI.getInstance().getIslandWorld();
    }

    @Override
    public UUID getOwner(Location location) {
        return ASkyBlockAPI.getInstance().getOwner(location);
    }

    @Override
    public boolean playerIsOnIsland(Player player) {
        return ASkyBlockAPI.getInstance().playerIsOnIsland(player);
    }

    @Override
    public String getName() {
        return "ASkyBlock";
    }
}
