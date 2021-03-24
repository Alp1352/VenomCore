package com.Venom.VenomCore.Menu.Engine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();

        if (inv.getHolder() == null || !(inv.getHolder() instanceof MenuHolder))
            return;

        MenuHolder holder = (MenuHolder) inv.getHolder();

        holder.getGUI().onClick(e);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();

        if (inv.getHolder() == null || !(inv.getHolder() instanceof MenuHolder))
            return;

        MenuHolder holder = (MenuHolder) inv.getHolder();

        holder.getGUI().onClose(e);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        Inventory inv = e.getInventory();

        if (inv.getHolder() == null || !(inv.getHolder() instanceof MenuHolder))
            return;

        MenuHolder holder = (MenuHolder) inv.getHolder();

        holder.getGUI().onDrag(e);
    }
}
