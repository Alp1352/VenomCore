package com.venom.nms.v1_12_R1.NBT;
import net.minecraft.server.v1_12_R1.NBTTagList;

public class NBTList implements com.venom.nms.core.NBT.NBTList {
    private final NBTTagList list;
    public NBTList() {
        this.list = new NBTTagList();
    }

    public void set(com.venom.nms.core.NBT.NBTCompound compound) {
        NBTCompound compound1 = (NBTCompound) compound;
        list.add(compound1.getMaster());
    }

    public com.venom.nms.core.NBT.NBTCompound createCompound() {
        return new NBTCompound();
    }

    public NBTTagList getList() {
        return list;
    }
}
