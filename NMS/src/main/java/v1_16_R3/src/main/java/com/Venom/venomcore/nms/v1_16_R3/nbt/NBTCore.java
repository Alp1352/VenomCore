package com.venom.venomcore.nms.v1_16_R3.nbt;

import org.bukkit.inventory.ItemStack;

public class NBTCore implements com.venom.venomcore.nms.core.nbt.NBTCore {
    @Override
    public com.venom.venomcore.nms.core.nbt.NBTItem toNBT(ItemStack item) {
        return new NBTItem(item);
    }

}
