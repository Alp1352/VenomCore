package com.venom.venomcore.plugin.menu;

import com.venom.venomcore.plugin.compatibility.CompatibleSound;
import com.venom.venomcore.plugin.item.ItemUtils;
import com.venom.venomcore.plugin.menu.internal.animations.Animation;
import com.venom.venomcore.plugin.menu.internal.animations.Frame;
import com.venom.venomcore.plugin.menu.internal.animations.premade.SwitchAnimation;
import com.venom.venomcore.plugin.menu.internal.animations.premade.VenomAnimationHorizontal;
import com.venom.venomcore.plugin.menu.internal.animations.utils.OpenAnimationUtils;
import com.venom.venomcore.plugin.menu.internal.containers.Container;
import com.venom.venomcore.plugin.menu.internal.item.MenuItem;
import com.venom.venomcore.plugin.menu.internal.item.action.ActionDetails;
import com.venom.venomcore.plugin.menu.internal.utils.MenuUtils;
import com.venom.venomcore.plugin.nms.NMSManager;
import com.venom.venomcore.plugin.plugin.VenomPlugin;
import com.venom.venomcore.plugin.task.RepeatingTask;
import com.venom.venomcore.plugin.task.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author Alp Beji
 * @see Container
 * The abstract class of all the GUI types.
 */
@SuppressWarnings("unused")
public abstract class GUI implements InventoryHolder {

    private boolean lock = false;
    private boolean closable = true;
    private boolean switching = false;
    private boolean titleChange = false;

    protected final List<Task> tasks = new ArrayList<>();

    private SwitchAnimation switchAnimation;

    protected final List<Player> viewers = new CopyOnWriteArrayList<>();

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
     * Call this method if you used the {@link Container#set(MenuItem, int)} method.
     * After calling this, all the updates on the container will be shown to the user.
     */
    public void update() {
        Container container = getUpperContainer();
        IntStream.range(0, getSize())
                .filter(container::isFull)
                .forEach(slot -> getUpperInventory().setItem(slot, container.get(slot).getItem().toItemStack()));
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
        setup();
        if (!isSwitching()) {
            update();
        }

        getUpperContainer().getFrames()
                .forEach(this::runFrame);

        runItemAnimations();
        return this;
    }

    /**
     * @return The inventory of the GUI.
     */
    protected abstract Inventory getUpperInventory();

    @NotNull
    @Override
    public Inventory getInventory() {
        return getUpperInventory();
    }

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
        ActionDetails details = new ActionDetails(p, e.getSlot(),this);

        e.setCancelled(
                item.getAction(e.getClick()) // Get the action.
                        .run(details) // Run the action. This will return a Result.
                        .isCancelled() // Check if the click is cancelled. If true, we cancel the event and vice versa.
        );

        p.playSound(p.getLocation(), item.getSound(), 1f, 1f);

        MenuItem itemForClick = item.getItemForClick(e.getClick());
        if (itemForClick != null && !details.isItemForClickCancelled()) {
            container.set(itemForClick, e.getSlot());
            update();
        }
    }

    private MenuItem loopAndFindItem(Container container, int slot, ItemStack item) {
        return container.getFrames().stream()
                .filter(frame -> frame.getSlot() == slot)
                .map(Frame::getItems)
                .flatMap(Collection::stream)
                .filter(menuItem -> ItemUtils.isEqual(item, menuItem))
                .findAny()
                .orElse(null);
    }

    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if (isClosable()) {
            viewers.remove(p);
            if (viewers.isEmpty()) {
                tasks.forEach(Task::cancel);
                tasks.clear();
            }
        } else if (!isSwitching()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> open(p), 10L);
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
        return Collections.unmodifiableList(viewers);
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
        if (getType() != MenuType.CHEST || gui.getType() != MenuType.CHEST || getSwitchAnimation() == null) {
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
            final AtomicInteger tick = new AtomicInteger(1);
            final HashMap<Integer, Container> items = getSwitchAnimation().getInventories();
            GUI currentGUI = GUI.this;
            @Override
            public void run() {
                if (getSwitchAnimation().getTotalTicks() == tick.get()) {
                    GUI.this.setLocked(false);
                    GUI.this.setSwitching(false);

                    gui.setLocked(false);
                    gui.setSwitching(false);

                    gui.construct().open(p);
                    cancel();
                    return;
                }

                if (tick.get() == getSwitchAnimation().getStayTicks()) {
                    currentGUI = gui;
                    currentGUI.open(p);
                }

                Container container = items.get(tick.getAndIncrement());
                Inventory inv = currentGUI.getUpperInventory();

                currentGUI.getUpperInventory().clear();
                currentGUI.update();

                IntStream.range(0, currentGUI.getSize())
                        .filter(container::isFull)
                        .forEach(slot -> inv.setItem(slot, container.get(slot).getItem().toItemStack()));
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
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                viewers.forEach(this::open);
                setTitleChanged(false);
            }, ticks);
        }
    }

    public void changeTitle(String newTitle) {
        changeTitle(newTitle, -1);
    }

    /**
     * Animates a frame.
     * @param frame The frame to animate.
     */
    public void runFrame(Frame frame) {
        runFrame(frame, getUpperContainer());
    }

    /**
     * Animates a frame.
     * @param frame The frame to animate.
     * @param container The container where the frame will be animated in.
     */
    public void runFrame(Frame frame, Container container) {
        new RepeatingTask(plugin, 0, frame.getTicksBetweenItems()) {
            final AtomicInteger timesLoop = new AtomicInteger(0);
            final AtomicInteger itemLoop = new AtomicInteger(0);
            final List<MenuItem> items = frame.getItems();
            final CompatibleSound sound = frame.getSound() != null ? CompatibleSound.matchCompatibleSound(frame.getSound()) : null;
            @Override
            public void run() {
                if (isClosed() || isLocked() || frame.getTimes() == timesLoop.get() || items.isEmpty()) {
                    cancel();
                    return;
                }

                if (itemLoop.compareAndSet(items.size(), 0)) {
                    timesLoop.getAndIncrement();
                }

                container.set(items.get(itemLoop.getAndIncrement()), frame.getSlot());
                update();

                if (sound != null) {
                    viewers.forEach(sound::play);
                }
            }
        };
    }

    public void runItemAnimations() {
        runItemAnimations(getUpperContainer());
    }

    public void runItemAnimations(Container container) {
        IntStream.range(0, container.getSize())
                .filter(container::isFull)
                .mapToObj(container::get)
                .filter(MenuItem::hasAnimation)
                .forEach(item -> {
                    Animation animation = item.getAnimation();
                    Animation.AnimationType type = animation.getType();
                    List<String> animations = animation.getAnimations();

                    Task task = new RepeatingTask(plugin, type.getDelay(), type.getBetween()) {
                        final AtomicInteger loop = new AtomicInteger(0);
                        @Override
                        public void run() {
                            item.getItem().setName(animations.get(loop.getAndIncrement()));
                            update();
                            viewers.forEach(Player::updateInventory);
                            loop.compareAndSet(animations.size(), 0);
                        }
                    };

                    tasks.add(task);
                });
    }

    /**
     * Override this method to add GUI items.
     */
    public void setup() {}

}
