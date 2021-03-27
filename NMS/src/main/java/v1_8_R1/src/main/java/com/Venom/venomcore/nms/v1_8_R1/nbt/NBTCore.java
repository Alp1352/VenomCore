package com.venom.venomcore.nms.v1_8_R1.nbt;

import org.bukkit.inventory.ItemStack;

public class NBTCore implements com.venom.venomcore.nms.core.nbt.NBTCore {
    @Override
    public com.venom.venomcore.nms.core.nbt.NBTItem toNBT(ItemStack item) {
        return new com.venom.venomcore.nms.v1_8_R1.nbt.NBTItem(item);
    }

}
