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
import com.Venom.VenomCore.Task.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alp Beji
 * @see GUI
 * A Double GUI that uses the players inventory as the second GUI.
 */
public abstract class DoubleGUI extends GUI {
    private final VenomPlugin plugin;
    private final Inventory upperInventory;
    private final Container upperContainer;
    private final Container lowerContainer;

    private final ConcurrentHashMap<UUID, ItemStack[]> items = new ConcurrentHashMap<>();

    public DoubleGUI(VenomPlugin plugin, String menuName, String title, int size) {
        super(plugin, menuName);
        this.plugin = plugin;
        this.upperInventory = Bukkit.createInventory(new MenuHolder(this), size, Color.translate(title));
        this.upperContainer = new Container(size);
        this.lowerContainer = new Container(36);
    }

    public DoubleGUI(VenomPlugin plugin, String menuName) {
        this(plugin, menuName, MenuUtils.getTitle(plugin, menuName), MenuUtils.getSize(plugin, menuName));
    }

    @Override
    public void open(Player p) {
        items.put(p.getUniqueId(), p.getInventory().getContents().clone());
        p.getInventory().clear();
        p.openInventory(getUpperInventory());
        if (!viewers.contains(p)) {
            viewers.add(p);
            update();
        }
    }

    @Override
    public synchronized void update() {
        super.update();
        for (int i = 0; i < lowerContainer.getSize(); i++) {
            if (lowerContainer.get(i) != null) {
                for (Player viewer : viewers) {
                    viewer.getInventory().setItem(i, lowerContainer.get(i).getItem().toItemStack());
                }
            }
        }
    }

    @Override
    public void close() {
        for (int i = 0; i < viewers.size(); i++) {
            viewers.get(i).closeInventory();
        }
        viewers.clear();
    }

    @Override
    public DoubleGUI construct() {
        if (!isSwitching()) {
            update();
        }
        getUpperContainer().getFrames().forEach(frame -> runFrame(frame, getUpperContainer()));
        getLowerContainer().getFrames().forEach(frame -> runFrame(frame, getLowerContainer()));
        runItemAnimations(getUpperContainer());
        runItemAnimations(getLowerContainer());
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

    public Container getLowerContainer() {
        return lowerContainer;
    }

    @Override
    public MenuType getType() {
        return MenuType.CHEST;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        MenuItem item;
        Container container = e.getRawSlot() >= getUpperContainer().getSize() ? getLowerContainer() : getUpperContainer();

        if (container.get(e.getSlot()) == null || !ItemUtils.isEqual(container.get(e.getSlot()).getItem().toItemStack(), e.getCurrentItem())) {
            item = loopAndFindItem(e.getSlot(), e.getCurrentItem(), container);
        } else {
            item = container.get(e.getSlot());
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
            container.set(itemForClick, e.getSlot());
            update();
        }
    }

    private MenuItem loopAndFindItem(int slot, ItemStack item, Container container) {
        for (Frame frame : container.getFrames()) {
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
        if (isClosable()) {
            Player p = (Player) e.getPlayer();
            ItemStack[] contents = items.get(p.getUniqueId());

            if (contents != null && !isTitleChanged()) {
                for (int i = 0; i < p.getInventory().getSize(); i++) {
                    p.getInventory().setItem(i, contents[i]);
                }
                items.remove(p.getUniqueId());
                viewers.remove(p);
                tasks.forEach(Task::cancel);
            }
        } else if (!isClosable() && !isSwitching()){
            Bukkit.getScheduler().runTaskLater(plugin, () -> open((Player) e.getPlayer()), 10L);
        }
    }

}
