package com.venom.venomcore.Item;

import com.venom.venomcore.Compatibility.CompatibleMaterial;
import com.venom.venomcore.Menu.Internal.Item.MenuItem;
import com.venom.venomcore.Server.ServerVersion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils {

    public static boolean isEqual(ItemStack first, ItemStack second) {
        if (first == null || second == null)
            return false;


        ItemStack skull = CompatibleMaterial.PLAYER_HEAD.parseItem();
        if (skull == null) return false;

        if (first.getType() == skull.getType() && second.getType() == skull.getType() && first.hasItemMeta() && second.hasItemMeta()) {
               SkullMeta firstMeta = (SkullMeta) first.getItemMeta();
               SkullMeta secondMeta = (SkullMeta) second.getItemMeta();
               if (firstMeta != null && secondMeta != null) {
                   if (ServerVersion.isServerVersionHigherOrEqual(ServerVersion.v1_12_R1)) {
                       if (firstMeta.getOwningPlayer() != null) {
                           return firstMeta.getOwningPlayer().equals(secondMeta.getOwningPlayer());
                       }
                   } else {
                       if (firstMeta.getOwner() != null) {
                            return firstMeta.getOwner().equals(secondMeta.getOwner());
                       }
                   }
               }
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
