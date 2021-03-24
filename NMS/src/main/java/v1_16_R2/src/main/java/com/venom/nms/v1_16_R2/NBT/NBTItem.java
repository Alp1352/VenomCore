package com.venom.nms.v1_16_R2.NBT;

import net.minecraft.server.v1_16_R2.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTItem extends NBTCompound implements com.venom.nms.core.NBT.NBTItem {
    private final net.minecraft.server.v1_16_R2.ItemStack item;
    private final NBTTagCompound master;

    public NBTItem(ItemStack item) {
        this.item = CraftItemStack.asNMSCopy(item);
        this.master = (this.item.hasTag()) ? this.item.getTag() : new NBTTagCompound();
        super.setMaster(this.master);
    }

    public com.venom.nms.core.NBT.NBTList createList() {
        return new NBTList();
    }

    public ItemStack toBukkit() {
        item.setTag(master);
        return CraftItemStack.asBukkitCopy(item);
    }

}
