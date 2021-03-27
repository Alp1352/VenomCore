package com.venom.venomcore.nms.core.nbt;
import org.bukkit.inventory.ItemStack;

public interface NBTCore {
    NBTItem toNBT(ItemStack item);
}
