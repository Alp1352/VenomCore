package com.venom.venomcore.plugin.menu.engine;
import com.venom.venomcore.plugin.menu.GUI;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuHolder implements InventoryHolder {
    private final GUI gui;
    public MenuHolder(GUI gui) {
        this.gui = gui;
    }

    public GUI getGUI() {
        return gui;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
