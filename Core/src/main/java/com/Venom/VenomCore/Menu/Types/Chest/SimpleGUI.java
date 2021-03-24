package com.Venom.VenomCore.Menu.Types.Chest;

import com.Venom.VenomCore.Chat.Color;
import com.Venom.VenomCore.Item.ItemUtils;
import com.Venom.VenomCore.Menu.Engine.MenuHolder;
import com.Venom.VenomCore.Menu.GUI;
import com.Venom.VenomCore.Menu.Internal.Animations.Frame;
import com.Venom.VenomCore.Menu.Internal.Containers.Container;
import com.Venom.VenomCore.Menu.Internal.Item.Action.ActionDetails;
import com.Venom.VenomCore.Menu.Internal.Item.MenuItem;
import com.Venom.VenomCore.Menu.Internal.Utils.MenuUtils;
import com.Venom.VenomCore.Menu.MenuType;
import com.Venom.VenomCore.Plugin.VenomPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Alp Beji
 * A simple GUI which supports animations.
 */
public abstract class SimpleGUI extends GUI {
    private final VenomPlugin plugin;
    private final Container upperContainer;
    private final Inventory upperInventory;


    public SimpleGUI(VenomPlugin plugin,  String menuName, String title, int size) {
        super(plugin, menuName);
        this.plugin = plugin;
        this.upperInventory = Bukkit.createInventory(new MenuHolder(this), size, Color.translate(title));
        this.upperContainer = new Container(size);
    }

    public SimpleGUI(VenomPlugin plugin, String menuName) {
        this(plugin, menuName, MenuUtils.getTitle(plugin, menuName), MenuUtils.getSize(plugin, menuName));
    }

    @Override
    public void open(Player p) {
        p.openInventory(upperInventory);
        viewers.add(p);
    }

    @Override
    public void close() {
        for (int i = 0; i < viewers.size(); i++) {
            viewers.get(i).closeInventory();
        }
        viewers.clear();
    }

    @Override
    public SimpleGUI construct() {
        if (!isSwitching()) {
            update();
        }
        getUpperContainer().getFrames().forEach(frame -> runFrame(frame, getUpperContainer()));
        runItemAnimations(getUpperContainer());
        return this;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        MenuItem item;

        if (upperContainer.get(e.getSlot()) == null || !ItemUtils.isEqual(upperContainer.get(e.getSlot()).getItem().toItemStack(), e.getCurrentItem())) {
            item = loopAndFindItem(e.getSlot(), e.getCurrentItem());
        } else {
            item = upperContainer.get(e.getSlot());
        }

        if (item == null || isLocked()) {
            e.setCancelled(true);
            return;
        }

        Player p = (Player) e.getWhoClicked();

        ActionDetails details = new ActionDetails(p, this);
        e.setCancelled(item.getAction(e.getClick()).run(details).isCancelled());

        if (item.getSound() != null) {
            p.playSound(p.getLocation(), item.getSound(), 1f, 1f);
        }

        MenuItem itemForClick = item.getItemForClick(e.getClick());
        if (itemForClick != null && !details.isItemForClickCancelled()) {
            upperContainer.set(itemForClick, e.getSlot());
            update();
        }
    }

    private MenuItem loopAndFindItem(int slot, ItemStack item) {
        for (Frame frame : upperContainer.getFrames()) {
            if (frame.getSlot() == slot) {
                for (MenuItem items : frame.getItems()) {
                    if (item.equals(items.getItem().toItemStack())) {
                        return items;
                    }
                }
            }
        }
        return null;
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
