package com.venom.venomcore.External.Protection.Types;

import br.net.fabiozumbi12.RedProtect.Bukkit.API.RedProtectAPI;
import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import com.venom.venomcore.External.Protection.ProtectionHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RedProtectHook extends ProtectionHook {
    RedProtectAPI api = RedProtect.get().getAPI();
    @Override
    public boolean canPlace(Player player, Location location) {
        Region region = api.getRegion(location);

        if (region == null)
            return true;

        return region.canBuild(player);
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        return canPlace(player, location);
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        Region region = api.getRegion(location);

        if (region == null)
            return true;

        return region.canChest(player);
    }

    @Override
    public String getName() {
        return "RedProtect";
    }
}
