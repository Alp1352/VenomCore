package com.venom.venomcore.plugin.item;

import com.venom.venomcore.plugin.compatibility.CompatibleMaterial;
import com.venom.venomcore.plugin.menu.internal.item.MenuItem;
import com.venom.venomcore.plugin.server.ServerVersion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils {

    public static boolean isEqual(ItemStack first, ItemStack second) {
        if (first == null || second == null)
            return false;


        ItemStack skull = CompatibleMaterial.PLAYER_HEAD.parseItem();
        if (skull == null) return false;

        if (first.getType() == skull.getType() &&
                second.getType() == skull.getType() &&
                first.hasItemMeta() &&
                second.hasItemMeta() &&
                first.getItemMeta() != null &&
                second.getItemMeta() != null) {

               SkullMeta firstMeta = (SkullMeta) first.getItemMeta();
               SkullMeta secondMeta = (SkullMeta) second.getItemMeta();

               return ServerVersion.isServerVersionHigherOrEqual(ServerVersion.v1_12_R1) &&
                       firstMeta.getOwningPlayer() != null &&
                       secondMeta.getOwningPlayer() != null ? firstMeta.getOwningPlayer().equals(secondMeta.getOwningPlayer()) :

                       firstMeta.getOwner() != null &&
                       secondMeta.getOwner() != null ?
                       firstMeta.getOwner().equals(secondMeta.getOwner()) : first.equals(second);

        }

        return first.equals(second);
    }

    public static boolean isEqual(ItemStack item, MenuItem menuItem) {
        return isEqual(menuItem.getItem().toItemStack(), item);
    }

    public static boolean isEqual(MenuItem menuItem, MenuItem item) {
        return menuItem.equals(item);
    }
}
