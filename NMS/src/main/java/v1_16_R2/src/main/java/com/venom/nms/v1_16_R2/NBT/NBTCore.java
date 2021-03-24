package com.venom.nms.v1_16_R2.NBT;

import org.bukkit.inventory.ItemStack;

public class NBTCore implements com.venom.nms.core.NBT.NBTCore {
    @Override
    public com.venom.nms.core.NBT.NBTItem toNBT(ItemStack item) {
        return new NBTItem(item);
    }

}
