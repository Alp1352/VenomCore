package com.venom.venomcore.plugin.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.venom.venomcore.nms.core.nbt.NBTItem;
import com.venom.venomcore.nms.core.nbt.NBTList;
import com.venom.venomcore.plugin.chat.Color;
import com.venom.venomcore.plugin.compatibility.CompatibleMaterial;
import com.venom.venomcore.plugin.nms.NMSManager;
import com.venom.venomcore.plugin.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Alp Beji
 * @apiNote A class for building Items.
 * Supports 1.8 - 1.16.5
 */
@SuppressWarnings("unused")
public class ItemBuilder {
    private ItemStack item;
    private ItemMeta itemMeta;

    private static Field profileField;
    private static Method profileMethod;

    static {
        try {
            Class<?> clazz = NMSManager.getCraftBukkitClass("inventory.CraftMetaSkull");
            if (ServerVersion.isServerVersionHigherOrEqual(ServerVersion.v1_15_R1)) {
                profileMethod = clazz.getDeclaredMethod("setProfile", GameProfile.class);
                profileMethod.setAccessible(true);
            } else {
                profileField = clazz.getDeclaredField("profile");
                profileField.setAccessible(true);
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a builder with a bukkit ItemStack.
     * @param item The ItemStack.
     */
    public ItemBuilder(ItemStack item) {
        if (item != null) {
            this.item = item;
            this.itemMeta = item.getItemMeta();
        }
    }

    /**
     * Creates a builder with bukkit material.
     * @param material The material.
     */
    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    /**
     * Creates a builder with a XMaterial.
     * @param material The material.
     */
    public ItemBuilder(CompatibleMaterial material) {
        this(material.parseItem());
    }

    /**
     * Creates an item builder with base64 encoded texture.
     * @param base64 The texture.
     */
    public ItemBuilder(String base64) {
        ItemStack head = CompatibleMaterial.PLAYER_HEAD.parseItem();

        SkullMeta meta;

        // This should never happen
        if (head == null)
            return;

        if (!head.hasItemMeta()) {
            meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(head.getType());
        } else {
            meta = (SkullMeta) head.getItemMeta();
        }

        if (meta == null) return;

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", base64));
        try {
            if (ServerVersion.isServerVersionHigherOrEqual(ServerVersion.v1_15_R1)) {
                profileMethod.invoke(meta, profile);
            } else {
                profileField.set(meta, profile);
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        this.item = head;
        this.itemMeta = meta;
    }

    /**
     * Sets the name of the item.
     * @param name The name.
     * @return The builder instance.
     */
    public ItemBuilder setName(String name) {
        itemMeta.setDisplayName(Color.translate(name));
        return this;
    }

    /**
     * Gets the display name of the item.
     * @return The name.
     */
    public String getName() {
        return itemMeta.getDisplayName();
    }

    /**
     * Sets the amount of the item.
     * @param amount The amount.
     * @return The builder instance.
     */
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Adds an enchantment to the item.
     * @param enchantment Enchantment to add.
     * @param level The enchantment level.
     * @return The builder instance.
     * @deprecated Use addUnsafeEnchantment.
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        return addUnsafeEnchantment(enchantment, level);
    }

    /**
     * Adds an unsafe enchantment to the item.
     * @param enchantment Enchantment to add.
     * @param level The enchantment level.
     * @return The builder instance.
     */
    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Removes an enchantment from the item.
     * @param enchantment Enchantment to remove.
     * @return The builder instance.
     */
    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        item.removeEnchantment(enchantment);
        return this;
    }

    /**
     * Checks if the given enchantments exists in the item.
     * @param enchantment The enchantment to check.
     * @return True if the item contains the enchantment.
     */
    public boolean containsEnchantment(Enchantment enchantment) {
        return item.containsEnchantment(enchantment);
    }

    /**
     * Sets the item flags.
     * @param flags The flags.
     * @return The builder instance.
     */
    public ItemBuilder setItemFlags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    /**
     * Makes the item unbreakable.
     * @param unbreakable The boolean.
     * @return The builder instance.
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        if (ServerVersion.isServerVersionHigherOrEqual(ServerVersion.v1_11_R1)) {
            itemMeta.setUnbreakable(unbreakable);
        } else {
            try {
                Method method = itemMeta.getClass().getMethod("spigot");
                Object spigot = method.invoke(itemMeta);
                Method setUnbreakable = spigot.getClass().getMethod("setUnbreakable", Boolean.class);
                setUnbreakable.invoke(spigot, unbreakable);
            } catch (Exception ignored) {}
        }
        return this;
    }

    /**
     * Colorizes and sets the lore in the item.
     * @param lore The lore.
     * @return The builder instance.
     */
    public ItemBuilder setLore(String... lore) {
        List<String> list = new ArrayList<>();
        for (String s : lore) {
            list.add(Color.translate(s));
        }
        itemMeta.setLore(list);
        return this;
    }

    /**
     * Adds a line to the items lore.
     * @param line The line to add.
     * @return The builder instance.
     */
    public ItemBuilder addLoreLine(String line) {
        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(Color.translate(line));
        itemMeta.setLore(lore);
        return this;
    }

    /**
     * Gets the lore of the item.
     * @return The lore.
     */
    public List<String> getLore() {
        return itemMeta.getLore();
    }

    /**
     * Colorizes and sets the lore in the item.
     * @param lore The lore.
     * @return The builder instance.
     */
    public ItemBuilder setLore(List<String> lore) {
        List<String> list = new ArrayList<>();
        for (String s : lore) {
            list.add(Color.translate(s));
        }
        itemMeta.setLore(list);
        return this;
    }

    /**
     * Makes the item glow.
     * @param glow Glow.
     * @return The builder instance.
     */
    public ItemBuilder setGlowing(boolean glow) {
        // Prevent losing the ItemMeta.
        this.item.setItemMeta(itemMeta);
        NBTItem item = toNBT();
        if (glow) {
            if (ServerVersion.isServerVersionLowerThan(ServerVersion.v1_10_R1)) {
                NBTList list = item.createList();
                item.set("ench", list);
                this.item = item.toBukkit();
            } else {
                addUnsafeEnchantment(Enchantment.LUCK, 1);
                setItemFlags(ItemFlag.HIDE_ENCHANTS);
                this.item.setItemMeta(itemMeta);
            }
        } else {
            item.remove("ench");
            this.item = item.toBukkit();
        }
        this.itemMeta = this.item.getItemMeta(); // Just in case.
        return this;
    }

    /**
     * Sets the skull owner. The player should have played in the server.
     * @param owner The owning player.
     */
    public void setSkullOwner(Player owner) {
        if (!(itemMeta instanceof SkullMeta))
            return;

        SkullMeta skullMeta = (SkullMeta) itemMeta;
        if (ServerVersion.isServerVersionLowerThan(ServerVersion.v1_12_R1)) {
            skullMeta.setOwner(owner.getName());
        } else {
            skullMeta.setOwningPlayer(owner);
        }
    }

    /**
     * Gets the me.alp135.NmsCore.NBT API.
     * @return The NBTItem instance.
     */
    public NBTItem toNBT() {
        return NMSManager.getNBT().toNBT(toItemStack());
    }

    /**
     * Sets the durability of the item.
     * @param durability The durability.
     */
    public void setDurability(short durability) {
        if (ServerVersion.isServerVersionHigherOrEqual(ServerVersion.v1_13_R1) && item.getItemMeta() instanceof Damageable) {
            Damageable damageable = (Damageable) itemMeta;
            damageable.setDamage(durability);
        } else {
            item.setDurability(durability);
        }
    }

    /**
     * Sets placeholders for this item.
     * @param map The placeholders.
     */
    public void setPlaceholder(Map<String, String> map) {
        if (getLore() != null) {
            List<String> list = getLore().stream().map(x -> setPlaceholder(x, map)).collect(Collectors.toList());
            setLore(list);
        }
        if (getName() != null) {
            setName(setPlaceholder(getName(), map));
        }
    }


    private String setPlaceholder(String str, Map<String, String> map) {
        if (map == null)
            return str;
        for (Map.Entry<String, String> entr : map.entrySet()) {
            str = str.replace(entr.getKey(), entr.getValue());
        }
        return str;
    }

    /**
     * Builds the item.
     * @return The built ItemStack.
     */
    public ItemStack toItemStack() {
        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * Generates a skull with base64 encoding.
     * @param base64 The texture.
     * @return The builder instance.
     */
    @Deprecated
    public static ItemBuilder generateSkull(String base64) {
        return new ItemBuilder(base64);
    }
}

