package com.venom.venomcore.Menu.Types.Chest;

import com.venom.venomcore.Chat.Color;
import com.venom.venomcore.Menu.Engine.MenuHolder;
import com.venom.venomcore.Menu.GUI;
import com.venom.venomcore.Menu.Internal.Containers.Container;
import com.venom.venomcore.Menu.Internal.Utils.MenuUtils;
import com.venom.venomcore.Menu.MenuType;
import com.venom.venomcore.Plugin.VenomPlugin;
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
        this.upperInventory = Bukkit.createInventory(new MenuHolder(this), size, Color.translate(title));
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
