package com.Venom.VenomCore.Menu.Types;

import com.Venom.VenomCore.Menu.Engine.MenuHolder;
import com.Venom.VenomCore.Menu.GUI;
import com.Venom.VenomCore.Menu.Internal.Containers.Container;
import com.Venom.VenomCore.Menu.Internal.Utils.MenuUtils;
import com.Venom.VenomCore.Menu.MenuType;
import com.Venom.VenomCore.Menu.Types.Chest.SimpleGUI;
import com.Venom.VenomCore.Plugin.VenomPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
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
