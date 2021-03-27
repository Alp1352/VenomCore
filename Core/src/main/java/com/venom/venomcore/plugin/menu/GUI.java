package com.venom.venomcore.plugin.menu;

import com.venom.venomcore.plugin.item.ItemUtils;
import com.venom.venomcore.plugin.menu.internal.animations.Animation;
import com.venom.venomcore.plugin.menu.internal.animations.Frame;
import com.venom.venomcore.plugin.menu.internal.animations.premade.SwitchAnimation;
import com.venom.venomcore.plugin.menu.internal.animations.premade.VenomAnimationHorizontal;
import com.venom.venomcore.plugin.menu.internal.animations.utils.OpenAnimationUtils;
import com.venom.venomcore.plugin.menu.internal.containers.Container;
import com.venom.venomcore.plugin.menu.internal.item.MenuItem;
import com.venom.venomcore.plugin.menu.internal.item.action.ActionDetails;
import com.venom.venomcore.plugin.menu.internal.item.action.ClickAction;
import com.venom.venomcore.plugin.menu.internal.utils.MenuUtils;
import com.venom.venomcore.plugin.nms.NMSManager;
import com.venom.venomcore.plugin.plugin.VenomPlugin;
import com.venom.venomcore.plugin.task.RepeatingTask;
import com.venom.venomcore.plugin.task.Task;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Alp Beji
 * @see Container
 * The abstract class of all the GUI types.
 */
@SuppressWarnings("unused")
public abstract class GUI {

    private boolean lock = false;
    private boolean closable = true;
    private boolean switching = false;
    private boolean titleChange = false;

    protected final List<Task> tasks = new ArrayList<>();

    private SwitchAnimation switchAnimation;

    protected List<Player> viewers = new CopyOnWriteArrayList<>();

    private final MenuUtils utils;
    private final VenomPlugin plugin;

    public GUI(VenomPlugin plugin, String menuName) {
        this.utils = new MenuUtils(plugin, menuName, this);
        this.plugin = plugin;
    }

    /**
     * Opens the GUI.
     * @param p The player who will see the GUI.
     */
    public void open(Player p) {
        p.openInventory(getUpperInventory());
        viewers.add(p);
    }

    /**
     * Synchronizes the container with the GUI.
     * Call this method if you set another item in the container
     * while the GUI is open.
     */
    public void update() {
        for (int i = 0; i < getUpperContainer().getSize(); i++) {
            if (getUpperContainer().get(i) != null) {
                getUpperInventory().setItem(i, getUpperContainer().get(i).getItem().toItemStack());
            }
        }
    }

    /**
     * Closes the GUI for every player viewing it.
     * If you want only a player to close it, use the
     * closeInventory method of the player class.
     */
    public void close() {
        viewers.forEach(HumanEntity::closeInventory);
        viewers.clear();
    }

    /**
     * Synchronizes the container with the inventory, and
     * starts playing all the animations.
     * @return The GUI instance.
     */
    public GUI construct() {
        if (!isSwitching()) {
            update();
        }
        getUpperContainer().getFrames().forEach(frame -> runFrame(frame, getUpperContainer()));
        runItemAnimations(getUpperContainer());
        return this;
    }

    /**
     * @return The inventory of the GUI.
     */
    protected abstract Inventory getUpperInventory();

    /**
     * @return The Container representing the upper inventory.
     */
    public abstract Container getUpperContainer();

    public void onClick(InventoryClickEvent e) {
        onClick(e, getUpperContainer());
    }

    protected void onClick(InventoryClickEvent e, Container container) {
        MenuItem item = container.get(e.getSlot());

        if (item == null || !ItemUtils.isEqual(item.getItem().toItemStack(), e.getCurrentItem())) {
            item = loopAndFindItem(container, e.getSlot(), e.getCurrentItem());
        }

        if (item == null || isLocked()) {
            e.setCancelled(true);
            return;
        }

        Player p = (Player) e.getWhoClicked();

        ActionDetails details = new ActionDetails(p, this);

        ClickAction clickAction = item.getAction(e.getClick());
        boolean cancel = clickAction.run(details).isCancelled();
        e.setCancelled(cancel);

        p.playSound(p.getLocation(), item.getSound(), 1f, 1f);

        MenuItem itemForClick = item.getItemForClick(e.getClick());
        if (itemForClick != null && !details.isItemForClickCancelled()) {
            getUpperContainer().set(itemForClick, e.getSlot());
            update();
        }
    }

    private MenuItem loopAndFindItem(Container container, int slot, ItemStack item) {
        for (Frame frame : container.getFrames()) {
            if (frame.getSlot() != slot) {
                continue;
            }

            for (MenuItem menuItem : frame.getItems()) {
                if (ItemUtils.isEqual(item, menuItem.getItem().toItemStack())) {
                    return menuItem;
                }
            }
        }

        return null;
    }

    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (!isClosable() && !isSwitching()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> open(p), 10L);
        } else if (isClosable()) {
            viewers.remove(p);
            tasks.forEach(Task::cancel);
        }
    }

    public void onDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }

    public abstract MenuType getType();

    public boolean isLocked() {
        return lock;
    }

    public void setLocked(boolean lock) {
        this.lock = lock;
    }

    public boolean isClosable() {
        return closable;
    }

    public void setClosable(boolean close) {
        closable = close;
    }

    public boolean isSwitching() {
        return switching;
    }

    protected void setSwitching(boolean switching) {
        this.switching = switching;
    }

    public boolean isTitleChanged() {
        return titleChange;
    }

    public void setTitleChanged(boolean changed) {
        titleChange = changed;
    }

    public SwitchAnimation getSwitchAnimation() {
        return switchAnimation;
    }

    public void setSwitchAnimation(SwitchAnimation animation) {
        switchAnimation = animation;
    }

    public boolean isClosed() {
        return viewers.isEmpty() || getUpperInventory() == null;
    }

    public MenuUtils getUtils() {
        return utils;
    }

    public List<Player> getViewers() {
        return new ArrayList<>(viewers);
    }

    public int getSize() { return getUpperInventory().getSize(); }

    public void clear() {
        getUpperInventory().clear();
        getUpperContainer().clear();
    }

    /**
     * Opens a new GUI with the switch animation.
     * @see VenomAnimationHorizontal
     * @see OpenAnimationUtils
     * @param p The player switching the GUI.
     * @param gui The new GUI.
     */
    public void switchMenu(Player p, GUI gui) {
        if (getType() != MenuType.CHEST || getSwitchAnimation() == null) {
            gui.construct().open(p);
            return;
        }

        this.setSwitching(true);
        this.setLocked(true);

        gui.setSwitching(true);
        gui.setLocked(true);

        tasks.forEach(Task::cancel);

        getSwitchAnimation().setTargetSize(gui.getSize());

        new RepeatingTask(plugin, 0, 1) {
            int ticks = 1;
            final HashMap<Integer, Container> items = getSwitchAnimation().getInventories();
            GUI currentGUI = GUI.this;
            @Override
            public void run() {
                if (getSwitchAnimation().getTotalTicks() == ticks) {
                    GUI.this.setLocked(false);
                    GUI.this.setSwitching(false);

                    gui.setLocked(false);
                    gui.setSwitching(false);

                    gui.construct().open(p);
                    cancel();
                    return;
                }

                if (ticks == getSwitchAnimation().getStayTicks()) {
                    currentGUI = gui;
                    currentGUI.open(p);
                }

                currentGUI.getUpperInventory().clear();
                currentGUI.update();

                for (int i = 0; i < currentGUI.getSize(); i++) {
                    MenuItem item = items.get(ticks).get(i);

                    if (item != null) {
                        currentGUI.getUpperInventory().setItem(i, item.getItem().toItemStack());
                    }
                }

                ticks = ticks + 1;
            }
        };
    }

    /**
     * Changes a title of a GUI without opening a new Inventory.
     * @param newTitle The new title.
     * @param ticks Duration of the new title.
     */
    public void changeTitle(String newTitle, int ticks) {
        viewers.forEach(player -> NMSManager.getTitleUpdater().updateTitle(player, newTitle));

        if (ticks != -1) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> viewers.forEach(player -> {
                player.openInventory(getUpperInventory());
                setTitleChanged(false);
            }), ticks);
        }
    }

    public void changeTitle(String newTitle) {
        changeTitle(newTitle, -1);
    }

    /**
     * Animates a frame.
     * @param frame The frame to animate.
     */
    public void runFrame(Frame frame, Container container) {
        new RepeatingTask(plugin, frame.getTicksBetweenItems(), frame.getTicksBetweenItems()) {
            int times = 0;
            int item = 0;
            @Override
            public void run() {
                if (isClosed() || frame.getTimes() == times || isLocked() || frame.getItems().isEmpty()) {
                    cancel();
                    return;
                }

                if (item == frame.getItems().size()) {
                    times = times + 1;
                    item = 0;
                }

                container.set(frame.getItems().get(item), frame.getSlot());
                update();

                if (frame.getSound() != null) {
                    viewers.forEach(player -> player.playSound(player.getLocation(), frame.getSound(), 1f, 1f));
                }
                item = item + 1;
            }
        };
    }

    public void runItemAnimations(Container container) {
        for (int i = 0; i < container.getSize(); i++) {
            MenuItem item = container.get(i);
            if (item == null || item.getItem().toItemStack().getType() == Material.AIR || item.getAnimation() == null)
                continue;

            Animation animation = item.getAnimation();

            Animation.AnimationType type = animation.getType();
            int delay = type.getDelay();
            int between = type.getBetween();

            RepeatingTask task = new RepeatingTask(plugin, delay, between) {
                int loop = 0;
                @Override
                public void run() {

                    item.getItem().setName(animation.getAnimations().get(loop));
                    update();

                    viewers.forEach(Player::updateInventory);

                    loop = loop + 1;

                    if (animation.getAnimations().size() == loop) {
                        loop = 0;
                    }
                }
            };

            tasks.add(task);
        }
    }

}
