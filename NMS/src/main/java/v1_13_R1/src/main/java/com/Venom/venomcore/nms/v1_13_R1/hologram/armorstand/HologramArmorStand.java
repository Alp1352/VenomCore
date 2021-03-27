package com.venom.venomcore.nms.v1_13_R1.hologram.armorstand;

import org.bukkit.Location;

public interface HologramArmorStand {
    void setHologramStandName(String name);

    int getStandID();

    void setPos(double x, double y, double z);

    void setPos(Location loc);
}
