package com.venom.venomcore.nms.v1_14_R1.nbt;

import net.minecraft.server.v1_14_R1.NBTTagList;

public class NBTList implements com.venom.venomcore.nms.core.nbt.NBTList {
    private final NBTTagList list;
    public NBTList() {
        this.list = new NBTTagList();
    }

    public void set(com.venom.venomcore.nms.core.nbt.NBTCompound compound) {
        NBTCompound compound1 = (NBTCompound) compound;
        list.add(compound1.getMaster());
    }

    public com.venom.venomcore.nms.core.nbt.NBTCompound createCompound() {
        return new NBTCompound();
    }

    public NBTTagList getList() {
        return list;
    }
}
