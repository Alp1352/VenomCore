package com.venom.venomcore.nms.v1_16_R3.hologram;


import com.venom.venomcore.nms.core.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class HologramCore implements com.venom.venomcore.nms.core.hologram.HologramCore {

    @Override
    public Hologram createHologram(JavaPlugin plugin, Location location, String... text) {
        return new com.venom.venomcore.nms.v1_16_R3.hologram.Hologram(plugin, location, Arrays.asList(text));
    }

    @Override
    public Hologram createHologram(JavaPlugin plugin, Player p, String... text) {
        return createHologram(plugin, p.getLocation(), text);
    }
}
