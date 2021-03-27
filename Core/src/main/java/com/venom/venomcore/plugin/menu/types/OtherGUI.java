package com.venom.venomcore.plugin.menu.types;

import com.venom.venomcore.plugin.menu.GUI;
import com.venom.venomcore.plugin.menu.MenuType;
import com.venom.venomcore.plugin.menu.engine.MenuHolder;
import com.venom.venomcore.plugin.menu.internal.containers.Container;
import com.venom.venomcore.plugin.menu.internal.utils.MenuUtils;
import com.venom.venomcore.plugin.plugin.VenomPlugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

/**
 * @author Alp Beji
 * @apiNote A class used for other GUI types.
 * You can use all the types in the MenuType enum.
 * If you want to use anvil, use the AnvilGUI instead.
 * @see MenuType
 */
public abstract class OtherGUI extends GUI {
    private final Inventory upperInventory;
    private final Container upperContainer;
    private final MenuType type;
    public OtherGUI(VenomPlugin plugin, String menuName, String title, MenuType type) {
        super(plugin, menuName);
        this.type = type;
        this.upperInventory = Bukkit.createInventory(new MenuHolder(this), type.toBukkit(), title);
        this.upperContainer = new Container(upperInventory.getSize());
    }

    public OtherGUI(VenomPlugin plugin, String menuName, MenuType type) {
        this(plugin, menuName, MenuUtils.getTitle(plugin, menuName), type);
    }

    @Override
    public OtherGUI construct() {
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
        return type;
    }
}
