package com.venom.nms.v1_8_R1.Hologram;


import com.venom.nms.core.Hologram.Hologram;
import com.venom.nms.v1_8_R1.Hologram.ArmorStand.CustomArmorStand;
import net.minecraft.server.v1_8_R1.EntityTypes;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

public class HologramCore implements com.venom.nms.core.Hologram.HologramCore {

    static {
        try {
            Field name = EntityTypes.class.getDeclaredField("d");
            Field ids = EntityTypes.class.getDeclaredField("f");

            name.setAccessible(true);
            ids.setAccessible(true);

            Map<Class<?>, String> names = (Map<Class<?>, String>) name.get(null);
            names.put(CustomArmorStand.class, "ArmorStand");
            Map<Class<?>, Integer> idMap = (Map<Class<?>, Integer>) ids.get(null);
            idMap.put(CustomArmorStand.class, 30);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Hologram createHologram(JavaPlugin plugin, Location location, String... text) {
        return new com.venom.nms.v1_8_R1.Hologram.Hologram(plugin, location, Arrays.asList(text));
    }

    @Override
    public Hologram createHologram(JavaPlugin plugin, Player p, String... text) {
        return createHologram(plugin, p.getLocation(), text);
    }
}
