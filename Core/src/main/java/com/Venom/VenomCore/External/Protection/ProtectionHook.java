package com.Venom.VenomCore.External.Protection;
import com.Venom.VenomCore.External.Hook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class ProtectionHook implements Hook {
    public abstract boolean canPlace(Player player, Location location);

    public abstract boolean canBreak(Player player, Location location);

    public abstract boolean canInteract(Player player, Location location);
}
