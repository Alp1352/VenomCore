package com.venom.nms.v1_8_R1.NBT;

import org.bukkit.inventory.ItemStack;

public class NBTCore implements com.venom.nms.core.NBT.NBTCore {
    @Override
    public com.venom.nms.core.NBT.NBTItem toNBT(ItemStack item) {
        return new com.venom.nms.v1_8_R1.NBT.NBTItem(item);
    }

}
