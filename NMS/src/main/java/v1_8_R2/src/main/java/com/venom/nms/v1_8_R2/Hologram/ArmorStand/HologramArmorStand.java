package com.venom.nms.v1_8_R2.Hologram.ArmorStand;

import org.bukkit.Location;

public interface HologramArmorStand {
    void setHologramStandName(String name);

    int getStandID();

    void setPos(double x, double y, double z);

    void setPos(Location loc);
}
