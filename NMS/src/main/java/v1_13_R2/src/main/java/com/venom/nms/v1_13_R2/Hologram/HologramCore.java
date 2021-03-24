package com.venom.nms.v1_13_R2.Hologram;


import com.venom.nms.core.Hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class HologramCore implements com.venom.nms.core.Hologram.HologramCore {

    @Override
    public Hologram createHologram(JavaPlugin plugin, Location location, String... text) {
        return new com.venom.nms.v1_13_R2.Hologram.Hologram(plugin, location, Arrays.asList(text));
    }

    @Override
    public Hologram createHologram(JavaPlugin plugin, Player p, String... text) {
        return createHologram(plugin, p.getLocation(), text);
    }
}
