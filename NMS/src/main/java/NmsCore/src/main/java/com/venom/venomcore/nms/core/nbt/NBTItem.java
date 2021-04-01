package com.venom.venomcore.nms.core.nbt;
import org.bukkit.inventory.ItemStack;

public interface NBTItem extends NBTCompound {
    NBTList createList();

    ItemStack toBukkit();
}
