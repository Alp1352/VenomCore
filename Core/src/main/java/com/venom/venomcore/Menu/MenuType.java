package com.venom.venomcore.Menu;
import org.bukkit.event.inventory.InventoryType;

public enum MenuType {
    CHEST(InventoryType.CHEST),
    ANVIL(InventoryType.ANVIL),
    HOPPER(InventoryType.HOPPER),
    CRAFTING_TABLE(InventoryType.WORKBENCH),
    BREWING(InventoryType.BREWING),
    FURNACE(InventoryType.FURNACE),
    DISPENSER(InventoryType.DISPENSER);

    private final InventoryType type;
    MenuType(InventoryType type) {
        this.type = type;
    }

    public InventoryType toBukkit() {
        return type;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
