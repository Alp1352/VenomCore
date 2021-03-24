package com.venom.nms.v1_12_R1.NBT;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

public class NBTCompound implements com.venom.nms.core.NBT.NBTCompound {
    private NBTTagCompound master;

    public NBTCompound() {
        master = new NBTTagCompound();
    }

    public NBTCompound set(String tag, String value) {
        master.set(tag, new NBTTagString(value));
        return this;
    }

    public NBTCompound set(String tag, Integer value) {
        master.set(tag, new NBTTagInt(value));
        return this;
    }

    public NBTCompound set(String tag, Double value) {
        master.set(tag, new NBTTagDouble(value));
        return this;
    }

    public NBTCompound set(String tag, Byte value) {
        master.set(tag, new NBTTagByte(value));
        return this;
    }

    public NBTCompound set(String tag, Float value) {
        master.set(tag, new NBTTagFloat(value));
        return this;
    }

    public NBTCompound set(String tag, Short value) {
        master.set(tag, new NBTTagShort(value));
        return this;
    }

    public NBTCompound set(String tag, Long value) {
        master.set(tag, new NBTTagLong(value));
        return this;
    }

    public NBTCompound set(String tag, int[] value) {
        master.set(tag, new NBTTagIntArray(value));
        return this;
    }

    public NBTCompound set(String tag, byte[] value) {
        master.set(tag, new NBTTagByteArray(value));
        return this;
    }

    public NBTCompound set(String tag, UUID uuid) {
        master.setString(tag, uuid.toString());
        return this;
    }

    public NBTCompound set(String tag, com.venom.nms.core.NBT.NBTList list) {
        NBTList nbtList = (NBTList) list;
        master.set(tag, nbtList.getList());
        return this;
    }

    public String getString(String tag) {
        return master.getString(tag);
    }

    public Integer getInteger(String tag) {
        return master.getInt(tag);
    }

    public Double getDouble(String tag) {
        return master.getDouble(tag);
    }

    public Byte getByte(String tag) {
        return master.getByte(tag);
    }

    public Float getFloat(String tag) {
        return master.getFloat(tag);
    }

    public Short getShort(String tag) {
        return master.getShort(tag);
    }

    public Long getLong(String tag) {
        return master.getLong(tag);
    }

    public int[] getIntArray(String tag) {
        return master.getIntArray(tag);
    }

    public byte[] getByteArray(String tag) {
        return master.getByteArray(tag);
    }

    public UUID getUUID(String tag) {
        UUID uuid;
        try {
            uuid = UUID.fromString(master.getString(tag));
        } catch (IllegalArgumentException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Bir hata olustu!");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Discordumuzdan yardım alabilirsiniz!");
            return UUID.randomUUID();
        }
        return uuid;
    }

    public boolean has(String tag) {
        return master.hasKey(tag);
    }

    public NBTCompound remove(String tag) {
        master.remove(tag);
        return this;
    }

    public NBTTagCompound getMaster() {
        return master;
    }

    public void setMaster(NBTTagCompound compound) {
        this.master = compound;
    }
}
