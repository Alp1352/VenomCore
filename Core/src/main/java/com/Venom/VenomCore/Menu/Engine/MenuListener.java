package com.Venom.VenomCore.Menu.Engine;

import com.Venom.VenomCore.Menu.GUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        GUI gui = check(e.getInventory());
        if (gui != null) {
            gui.onClick(e);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        GUI gui = check(e.getInventory());
        if (gui != null) {
            gui.onClose(e);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        GUI gui = check(e.getInventory());
        if (gui != null) {
            gui.onDrag(e);
        }
    }

    private GUI check(Inventory inv) {
        if (inv.getHolder() == null || !(inv.getHolder() instanceof MenuHolder))
            return null;

        MenuHolder holder = (MenuHolder) inv.getHolder();
        return holder.getGUI();
    }
}
