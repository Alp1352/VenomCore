package com.venom.venomcore.nms.core.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public interface HologramCore {
    Hologram createHologram(JavaPlugin plugin, Location location, String... text);

    Hologram createHologram(JavaPlugin plugin, Player p, String... text);
}
