package com.Venom.VenomCore.Menu.Engine;
import com.Venom.VenomCore.Menu.GUI;
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
