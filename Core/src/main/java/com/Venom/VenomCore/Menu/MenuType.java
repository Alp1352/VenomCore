package com.Venom.VenomCore.Menu;
import org.bukkit.event.inventory.InventoryType;

public enum MenuType {
    CHEST, ANVIL, HOPPER, CRAFTING_TABLE, BREWING, FURNACE, DISPENSER;

    @Override
    public String toString() {
        return this.name();
    }

    public InventoryType toBukkit() {
        try {
            return InventoryType.valueOf(name());
        } catch (IllegalArgumentException e) {
            if (this == MenuType.CRAFTING_TABLE) {
                return InventoryType.WORKBENCH;
            }
        }

        return InventoryType.CHEST;
    }
}
