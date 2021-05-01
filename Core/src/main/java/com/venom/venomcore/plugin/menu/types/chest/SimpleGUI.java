package com.venom.venomcore.plugin.menu.types.chest;

import com.venom.venomcore.plugin.chat.Color;
import com.venom.venomcore.plugin.menu.GUI;
import com.venom.venomcore.plugin.menu.MenuType;
import com.venom.venomcore.plugin.menu.internal.containers.Container;
import com.venom.venomcore.plugin.menu.internal.utils.MenuUtils;
import com.venom.venomcore.plugin.plugin.VenomPlugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

/**
 * @author Alp Beji
 * A simple GUI which supports animations.
 */
public abstract class SimpleGUI extends GUI {
    private final Container upperContainer;
    private final Inventory upperInventory;

    public SimpleGUI(VenomPlugin plugin,  String menuName, String title, int size) {
        super(plugin, menuName);
        this.upperInventory = Bukkit.createInventory(this, size, Color.translate(title));
        this.upperContainer = new Container(size);
    }

    public SimpleGUI(VenomPlugin plugin, String menuName) {
        this(plugin, menuName, MenuUtils.getTitle(plugin, menuName), MenuUtils.getSize(plugin, menuName));
    }

    @Override
    public SimpleGUI construct() {
        super.construct();
        return this;
    }

    @Override
    protected Inventory getUpperInventory() {
        return upperInventory;
    }

    @Override
    public Container getUpperContainer() {
        return upperContainer;
    }

    @Override
    public MenuType getType() {
        return MenuType.CHEST;
    }

}
