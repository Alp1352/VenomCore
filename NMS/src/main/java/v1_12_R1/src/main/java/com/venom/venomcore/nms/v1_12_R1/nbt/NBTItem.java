package com.venom.venomcore.nms.v1_12_R1.nbt;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTItem extends NBTCompound implements com.venom.venomcore.nms.core.nbt.NBTItem {
    private final net.minecraft.server.v1_12_R1.ItemStack item;
    private final NBTTagCompound master;

    public NBTItem(ItemStack item) {
        this.item = CraftItemStack.asNMSCopy(item);
        this.master = (this.item.hasTag()) ? this.item.getTag() : new NBTTagCompound();
        super.setMaster(this.master);
    }

    public com.venom.venomcore.nms.core.nbt.NBTList createList() {
        return new NBTList();
    }

    public ItemStack toBukkit() {
        item.setTag(master);
        return CraftItemStack.asBukkitCopy(item);
    }

}
