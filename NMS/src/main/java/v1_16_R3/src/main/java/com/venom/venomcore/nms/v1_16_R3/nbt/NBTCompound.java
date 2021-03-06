package com.venom.venomcore.nms.v1_16_R3.nbt;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

public class NBTCompound implements com.venom.venomcore.nms.core.nbt.NBTCompound {
    private NBTTagCompound master;

    public NBTCompound() {
        master = new NBTTagCompound();
    }

    public NBTCompound set(String tag, String value) {
        master.setString(tag, value);
        return this;
    }

    public NBTCompound set(String tag, Integer value) {
        master.setInt(tag, value);
        return this;
    }

    public NBTCompound set(String tag, Double value) {
        master.setDouble(tag, value);
        return this;
    }

    public NBTCompound set(String tag, Byte value) {
        master.setByte(tag, value);
        return this;
    }

    public NBTCompound set(String tag, Float value) {
        master.setFloat(tag, value);
        return this;
    }

    public NBTCompound set(String tag, Short value) {
        master.setShort(tag, value);
        return this;
    }

    public NBTCompound set(String tag, Long value) {
        master.setLong(tag, value);
        return this;
    }

    public NBTCompound set(String tag, int[] value) {
        master.setIntArray(tag, value);
        return this;
    }

    public NBTCompound set(String tag, byte[] value) {
        master.setByteArray(tag, value);
        return this;
    }

    public NBTCompound set(String tag, UUID uuid) {
        master.setString(tag, uuid.toString());
        return this;
    }

    public NBTCompound set(String tag, com.venom.venomcore.nms.core.nbt.NBTList list) {
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
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Discordumuzdan yard??m alabilirsiniz!");
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
