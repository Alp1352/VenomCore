package com.venom.venomcore.plugin.compatibility;

import com.venom.venomcore.plugin.item.ItemBuilder;
import com.venom.venomcore.plugin.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.lang.reflect.Method;

/**
 * @author Alp Beji
 * @apiNote A class for version compatible hands.
 * Supports 1.7 - 1.16.5
 */
public abstract class CompatibleHand {

    private static final boolean SUPPORT_API = ServerVersion.isServerVersionHigherOrEqual(ServerVersion.v1_9_R1);
    private static final boolean SUPPORT_DAMAGE = ServerVersion.isServerVersionHigherOrEqual(ServerVersion.v1_13_R1);
    private final Player p;

    private CompatibleHand(Player p) {
        this.p = p;
    }

    /**
     * Sets the item in the selected players selected hand.
     * @param item The new item.
     */
    public abstract void setItem(ItemStack item);

    /**
     * Sets the item in the selected players selected hand.
     * @param item The new item.
     */
    public void setItem(ItemBuilder item) {
        setItem(item.toItemStack());
    }

    /**
     * Takes a specified amount of item in the players hand.
     * @param amount The amount that will be taken.
     */
    public void takeItem(int amount) {
        ItemStack item = getItem();
        if (item == null)
            return;

        int left = item.getAmount() - amount;
        if (left > 0) {
            item.setAmount(left);
            setItem(item);
        } else {
            removeItem();
        }
    }

    /**
     * Takes one item in the players hand.
     * If the amount becomes 0, it removes the item.
     * Uses the{@link #takeItem(int) takeItem} to take one item.
     */
    public void takeItem() {
        takeItem(1);
    }

    /**
     * Damages the item in hand.
     * If the item breaks, calls the event and plays a sound.
     * @param damage The amount of damage that will be dealt.
     */
    public void damageItem(short damage) {
        ItemStack item = getItem();
        if (item == null)
            return;

        ItemMeta meta = item.getItemMeta();
        int maxDurability = item.getType().getMaxDurability();
        int newDurability;
        if (SUPPORT_DAMAGE) {
            if (!(meta instanceof Damageable))
                return;

            Damageable damageable = (Damageable) meta;
            damageable.setDamage(damageable.getDamage() + damage);
            item.setItemMeta(meta);
            newDurability = damageable.getDamage();
        } else {
            item.setDurability((short) (item.getDurability() + damage));
            newDurability = item.getDurability();
        }

        setItem(item);

        if (newDurability >= maxDurability) {
            breakItem();
        }
    }

    /**
     * Drops the item in hand on the ground naturally.
     */
    public void dropItem() {
        ItemStack item = getItem();
        if (item == null)
            return;

        Location location = p.getLocation().clone();
        Vector direction = location.getDirection().clone();
        World world = p.getWorld();

        world.dropItemNaturally(location.add(direction.multiply(2)), item);

        removeItem();
    }

    /**
     * Removes the item in hand.
     */
    public void removeItem() {
        setItem((ItemStack) null);
    }

    /**
     * Breaks the item in hand.
     * Calls the PlayerItemBreakEvent, Plays ITEM_BREAK sound, removes the item.
     */
    public void breakItem() {
        ItemStack item = getItem();
        if (item == null)
            return;

        PlayerItemBreakEvent event = new PlayerItemBreakEvent(p, item);
        Bukkit.getPluginManager().callEvent(event);

        CompatibleSound.ENTITY_ITEM_BREAK.play(p);
        removeItem();
    }

    /**
     * Gets the item in hand.
     * @return The item in hand.
     */
    public abstract ItemStack getItem();

    /**
     * Gets the hand of a player.
     * @param p The player to get the hand of.
     * @param type The type of the hand we will be getting from the player.
     * @return The hand instance.
     */
    public static CompatibleHand getHandOf(Player p, HandType type) {
        return type == HandType.MAIN_HAND ? new MainHand(p) : new OffHand(p);
    }

    /**
     * Gets the used hand in an event.
     * Event can be any event that has a getHand method.
     * Like PlayerInteractEvent.
     * @param p The player.
     * @param event The event.
     * @return The hand instance.
     */
    public static CompatibleHand getUsedHandIn(Player p, Object event) {
        EquipmentSlot slot;
        HandType type;

        Class<?> clazz = event.getClass();
        try {
            Method method = clazz.getMethod("getHand");
            method.setAccessible(true);
            slot = (EquipmentSlot) method.invoke(event);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        type = (slot == EquipmentSlot.HAND) ? HandType.MAIN_HAND : HandType.OFF_HAND;
        return getHandOf(p, type);
    }

    /**
     * Gets the used hand in an event.
     * The player of the event will be used, so
     * if you want to specify the player,
     * use the {@link #getUsedHandIn(Player, Object) getUsedHandIn} method.
     * @param event The event.
     * @return The hand instance.
     */
    public static CompatibleHand getUsedHandIn(Object event) {
        Player p;
        Class<?> clazz = event.getClass();
        try {
            Method method = clazz.getMethod("getPlayer");
            method.setAccessible(true);
            p = (Player) method.invoke(event);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return getUsedHandIn(p, event);
    }

    public boolean isSupportingAPI() {
        return SUPPORT_API;
    }

    /**
     * Each enum represents a player hand.
     */
    public enum HandType {
        MAIN_HAND, OFF_HAND
    }
    /**
     * The Off Hand of the player.
     * Supports 1.9.2 - 1.16.5
     */
    private static class OffHand extends CompatibleHand {
        private final PlayerInventory inv;
        public OffHand(Player p) {
            super(p);
            this.inv = p.getInventory();
        }

        public void setItem(ItemStack item) {
            if (SUPPORT_API) {
                inv.setItemInOffHand(item);
            }
        }

        public ItemStack getItem() {
            return SUPPORT_API ? inv.getItemInOffHand() : null;
        }
    }

    /**
     * The Main Hand of the player.
     * Supports 1.7.2 - 1.16.5
     */
    private static class MainHand extends CompatibleHand {
        private final PlayerInventory inv;
        public MainHand(Player p) {
            super(p);
            this.inv = p.getInventory();
        }

        public void setItem(ItemStack item) {
            if (SUPPORT_API) {
                inv.setItemInMainHand(item);
            } else {
                inv.setItemInHand(item);
            }
        }

        public ItemStack getItem() {
            return SUPPORT_API ? inv.getItemInMainHand() : inv.getItemInHand();
        }
    }
}
