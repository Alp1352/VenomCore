package com.venom.venomcore.External.Protection.Types;

import com.songoda.ultimateclaims.UltimateClaims;
import com.songoda.ultimateclaims.claim.Claim;
import com.songoda.ultimateclaims.member.ClaimPerm;
import com.venom.venomcore.External.Protection.ProtectionHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class UltimateClaimsHook extends ProtectionHook {
    @Override
    public boolean canPlace(Player player, Location location) {
        return checkPerm(player, location, ClaimPerm.PLACE);
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        return checkPerm(player, location, ClaimPerm.BREAK);
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        return checkPerm(player, location, ClaimPerm.INTERACT);
    }

    private boolean checkPerm(Player p, Location loc, ClaimPerm perm) {
        Claim claim = UltimateClaims.getInstance().getClaimManager().getClaim(loc.getChunk());
        if (claim == null)
            return true;

        return claim.playerHasPerms(p, perm);
    }


    @Override
    public String getName() {
        return "UltimateClaims";
    }
}
