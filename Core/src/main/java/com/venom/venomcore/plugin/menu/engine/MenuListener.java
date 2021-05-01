package com.venom.venomcore.plugin.menu.engine;

import com.venom.venomcore.plugin.menu.GUI;
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
        return (inv.getHolder() != null && inv.getHolder() instanceof GUI) ? ((GUI) inv.getHolder()) : null;
    }
}
