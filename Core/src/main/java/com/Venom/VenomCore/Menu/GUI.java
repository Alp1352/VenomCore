package com.Venom.VenomCore.Menu;

import com.Venom.VenomCore.Menu.Internal.Animations.Animation;
import com.Venom.VenomCore.Menu.Internal.Animations.DefaultAnimations.SwitchAnimation;
import com.Venom.VenomCore.Menu.Internal.Animations.DefaultAnimations.VenomAnimationHorizontal;
import com.Venom.VenomCore.Menu.Internal.Animations.Frame;
import com.Venom.VenomCore.Menu.Internal.Animations.Utils.OpenAnimationUtils;
import com.Venom.VenomCore.Menu.Internal.Containers.Container;
import com.Venom.VenomCore.Menu.Internal.Item.MenuItem;
import com.Venom.VenomCore.Menu.Internal.Utils.MenuUtils;
import com.Venom.VenomCore.NMS.NMSManager;
import com.Venom.VenomCore.Plugin.VenomPlugin;
import com.Venom.VenomCore.Task.RepeatingTask;
import com.Venom.VenomCore.Task.Task;
import com.Venom.VenomCore.VenomCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

    protected List<Player> viewers = Collections.synchronizedList(new ArrayList<>());

    private final MenuUtils utils;

    public GUI(VenomPlugin plugin, String menuName) {
        this.utils = new MenuUtils(plugin, menuName, this);
    }

    /**
     * Opens the GUI.
     * @param p The player who will see the GUI.
     */
    public abstract void open(Player p);

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
    public abstract void close();

    /**
     * Synchronizes the container with the inventory, and
     * starts playing all the animations.
     * @return The GUI instance.
     */
    public abstract GUI construct();

    /**
     * @return The inventory of the GUI.
     */
    protected abstract Inventory getUpperInventory();

    /**
     * @return The Container representing the upper inventory.
     */
    public abstract Container getUpperContainer();

    public abstract void onClick(InventoryClickEvent e);

    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (!isClosable() && !isSwitching()) {
            Bukkit.getScheduler().runTaskLater(VenomCore.getPlugin(VenomCore.class), () -> open(p), 10L);
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

    public int getSize() { return getUpperInventory().getSize(); }

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

        new RepeatingTask(JavaPlugin.getPlugin(VenomCore.class), 0, 1) {
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
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(VenomCore.class), () -> viewers.forEach(player -> {
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
        new RepeatingTask(JavaPlugin.getPlugin(VenomCore.class), frame.getTicksBetweenItems(), frame.getTicksBetweenItems()) {
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

            RepeatingTask task = new RepeatingTask(JavaPlugin.getPlugin(VenomCore.class), delay, between) {
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
