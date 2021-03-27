package com.venom.venomcore.nms.core.nbt;

import java.util.UUID;

public interface NBTCompound {
    NBTCompound set(String tag, String value);

    NBTCompound set(String tag, Integer value);

    NBTCompound set(String tag, Double value);

    NBTCompound set(String tag, Byte value);

    NBTCompound set(String tag, Float value);

    NBTCompound set(String tag, Short value);

    NBTCompound set(String tag, Long value);

    NBTCompound set(String tag, int[] value);

    NBTCompound set(String tag, byte[] value);

    NBTCompound set(String tag, UUID uuid);

    NBTCompound set(String tag, NBTList list);

    String getString(String tag);

    Integer getInteger(String tag);

    Double getDouble(String tag);

    Byte getByte(String tag);

    Float getFloat(String tag);

    Short getShort(String tag);

    Long getLong(String tag);

    int[] getIntArray(String tag);

    byte[] getByteArray(String tag);

    UUID getUUID(String tag);

    boolean has(String tag);

    NBTCompound remove(String tag);
}
