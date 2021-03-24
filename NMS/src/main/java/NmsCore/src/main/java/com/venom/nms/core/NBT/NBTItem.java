package com.venom.nms.core.NBT;
import org.bukkit.inventory.ItemStack;

public interface NBTItem extends NBTCompound {
    NBTList createList();

    ItemStack toBukkit();
}
