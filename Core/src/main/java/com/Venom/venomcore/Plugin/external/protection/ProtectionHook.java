package com.venom.venomcore.plugin.external.protection;
import com.venom.venomcore.plugin.external.Hook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class ProtectionHook implements Hook {
    public abstract boolean canPlace(Player player, Location location);

    public abstract boolean canBreak(Player player, Location location);

    public abstract boolean canInteract(Player player, Location location);
}
