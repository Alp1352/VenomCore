package com.venom.venomcore.plugin.external.protection.types;

import com.venom.venomcore.plugin.external.protection.ProtectionHook;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GriefPreventionHook extends ProtectionHook {
    private final DataStore store = GriefPrevention.getPlugin(GriefPrevention.class).dataStore;

    @Override
    public boolean canPlace(Player player, Location location) {
        Claim claim = getClaim(location);
        if (claim == null)
            return true;


        return claim.allowBuild(player, location.getBlock().getType()) == null;
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        Claim claim = getClaim(location);

        if (claim == null)
            return true;

        return claim.allowBreak(player, location.getBlock().getType()) == null;
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        Claim claim = getClaim(location);
        if (claim == null) {
            return true;
        }
        return claim.allowContainers(player) == null;
    }

    public Claim getClaim(Location location) {
        return store.getClaimAt(location, true, null);
    }

    @Override
    public String getName() {
        return "GriefPrevention";
    }
}
