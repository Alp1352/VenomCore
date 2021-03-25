package com.Venom.VenomCore.Menu.Types.Chest;

import com.Venom.VenomCore.Chat.Color;
import com.Venom.VenomCore.Compatibility.CompatibleMaterial;
import com.Venom.VenomCore.Item.ItemBuilder;
import com.Venom.VenomCore.Item.ItemUtils;
import com.Venom.VenomCore.Menu.Engine.MenuHolder;
import com.Venom.VenomCore.Menu.GUI;
import com.Venom.VenomCore.Menu.Internal.Animations.Frame;
import com.Venom.VenomCore.Menu.Internal.Containers.Container;
import com.Venom.VenomCore.Menu.Internal.Item.Action.ActionDetails;
import com.Venom.VenomCore.Menu.Internal.Item.Action.ClickAction;
import com.Venom.VenomCore.Menu.Internal.Item.MenuItem;
import com.Venom.VenomCore.Menu.Internal.Utils.MenuUtils;
import com.Venom.VenomCore.Menu.MenuType;
import com.Venom.VenomCore.Plugin.VenomPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

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
