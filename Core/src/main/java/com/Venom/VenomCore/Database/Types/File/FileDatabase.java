package com.Venom.VenomCore.Database.Types.File;

import de.leonhard.storage.internal.FlatFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("unused")
public abstract class FileDatabase {
    private final FlatFile file;
    public FileDatabase(FlatFile file) {
        this.file = file;
    }

    public ItemStack getItemStack(String key) throws IOException {
        return itemStackFromBase64(file.getString(key));
    }

    public void setItemStack(String key, ItemStack item) {
        file.set(key, itemStackToBase64(item));
    }

    public void setItemStackList(String key, List<ItemStack> items) {
        List<String> serialized = new ArrayList<>();
        for (ItemStack item : items)
            serialized.add(itemStackToBase64(item));
        file.set(key, serialized);
    }

    public List<ItemStack> getItemStackList(String key) throws IOException {
        List<String> list = file.getStringList(key);
        List<ItemStack> items = new ArrayList<>();
        for (String s : list)
            items.add(itemStackFromBase64(s));
        return items;
    }

    public void setItemStackArray(String key, ItemStack[] array) {
        file.set(key, itemStackArrayToBase64(array));
    }

    public ItemStack[] getItemStackArray(String key) throws IOException {
        String s = file.getString(key);
        return itemStackArrayFromBase64(s);
    }

    public void setInventory(String key, Inventory inventory) {
        file.set(key, toBase64(inventory));
    }

    public Inventory getInventory(String key) throws IOException {
        return fromBase64(file.getString(key));
    }

    public Location getLocation(String key) {
        String serialized = file.getString(key);
        String[] split = serialized.split(",");
        Location location = new Location(Bukkit.getWorld(split[5]),Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2]));
        location.setYaw(Integer.parseInt(split[3]));
        location.setPitch(Integer.parseInt(split[4]));
        return location;
    }

    public List<Location> getLocationList(String key) {
        List<Location> locations = new ArrayList<>();
        List<String> list = file.getStringList(key);
        for (String o : list) {
            String[] split = o.split(",");
            Location location = new Location(Bukkit.getWorld(split[5]),Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2]));
            location.setYaw(Integer.parseInt(split[3]));
            location.setPitch(Integer.parseInt(split[4]));
            locations.add(location);
        }
        return locations;
    }

    public void setLocation(String key, Location location) {
        file.set(key, Math.round(location.getX()) + "," + Math.round(location.getY()) + "," + Math.round(location.getZ()) + "," + Math.round(location.getYaw()) + "," + Math.round(location.getPitch()) + "," + location.getWorld().getName());
    }

    public void setLocationList(String key, List<Location> locations) {
        List<String> serialized = new ArrayList<>();
        locations.forEach(location -> serialized.add(Math.round(location.getX()) + "," + Math.round(location.getY()) + "," + Math.round(location.getZ()) + "," + Math.round(location.getYaw()) + "," + Math.round(location.getPitch()) + "," + location.getWorld().getName()));
        file.set(key, serialized);
    }

    private static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());
        return new String[] { content, armor };
    }

    private static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (int i = 0; i < items.length; i++)
                dataOutput.writeObject(items[i]);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Kaydetmede bir sorun oldu.", e);
        }
    }

    private static String itemStackToBase64(ItemStack item) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Kaydetmede bir sorun oldu.", e);
        }
    }

    private static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inventory.getSize());
            for (int i = 0; i < inventory.getSize(); i++)
                dataOutput.writeObject(inventory.getItem(i));
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Kaydetmede bir sorun oldu.", e);
        }
    }

    private static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack)dataInput.readObject());
            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Okumada bir sorun oldu.", e);
        }
    }

    private static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++)
                items[i] = (ItemStack)dataInput.readObject();
            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Okumada bir sorun oldu.", e);
        }
    }

    private static ItemStack itemStackFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack)dataInput.readObject();
            dataInput.close();
            return item;
        } catch (ClassNotFoundException e) {
            throw new IOException("Okumada bir sorun oldu.", e);
        }
    }

    public FlatFile toSimplixStorage() {
        return file;
    }
}
