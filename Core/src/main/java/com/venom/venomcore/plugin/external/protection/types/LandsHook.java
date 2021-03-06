package com.venom.venomcore.plugin.external.protection.types;

import com.venom.venomcore.plugin.VenomCore;
import com.venom.venomcore.plugin.external.protection.ProtectionHook;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.role.enums.RoleSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LandsHook extends ProtectionHook {
    private final LandsIntegration integration = new LandsIntegration(VenomCore.getPlugin(VenomCore.class));

    @Override
    public boolean canPlace(Player player, Location location) {
        return checkPerm(player, location, RoleSetting.BLOCK_PLACE);
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        return checkPerm(player, location, RoleSetting.BLOCK_BREAK);
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        return checkPerm(player, location, RoleSetting.INTERACT_CONTAINER);
    }

    public boolean checkPerm(Player player, Location location, RoleSetting roleSetting) {
        Area area = integration.getAreaByLoc(location);
        if (area == null)
            return true;

        return area.canSetting(player.getUniqueId(), roleSetting);
    }

    @Override
    public String getName() {
        return "Lands";
    }
}
