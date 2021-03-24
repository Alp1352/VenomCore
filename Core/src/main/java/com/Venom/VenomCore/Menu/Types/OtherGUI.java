package com.Venom.VenomCore.Menu.Types;

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
import com.Venom.VenomCore.Task.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
    private final VenomPlugin plugin;
    public OtherGUI(VenomPlugin plugin, String menuName, String title, MenuType type) {
        super(plugin, menuName);
        this.type = type;
        this.plugin = plugin;
        this.upperInventory = Bukkit.createInventory(new MenuHolder(this), type.toBukkit(), title);
        this.upperContainer = new Container(upperInventory.getSize());
    }

    public OtherGUI(VenomPlugin plugin, String menuName, MenuType type) {
        this(plugin, menuName, MenuUtils.getTitle(plugin, menuName), type);
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
    public OtherGUI construct() {
        update();
        getUpperContainer().getFrames().forEach(frame -> runFrame(frame, getUpperContainer()));
        runItemAnimations(getUpperContainer());
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
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (!isClosable() && !isSwitching()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> open(p), 10L);
        } else if (isClosable()) {
            viewers.remove(p);
            tasks.forEach(Task::cancel);
        }
    }

    @Override
    public MenuType getType() {
        return type;
    }
}
