package com.Venom.VenomCore.Menu.Types.Chest;

import com.Venom.VenomCore.Chat.Color;
import com.Venom.VenomCore.Item.ItemUtils;
import com.Venom.VenomCore.Menu.Engine.MenuHolder;
import com.Venom.VenomCore.Menu.GUI;
import com.Venom.VenomCore.Menu.Internal.Animations.Frame;
import com.Venom.VenomCore.Menu.Internal.Containers.Container;
import com.Venom.VenomCore.Menu.Internal.Item.Action.ActionDetails;
import com.Venom.VenomCore.Menu.Internal.Item.Action.ClickAction;
import com.Venom.VenomCore.Menu.Internal.Item.Action.Result;
import com.Venom.VenomCore.Menu.Internal.Item.MenuItem;
import com.Venom.VenomCore.Menu.Internal.Utils.MenuUtils;
import com.Venom.VenomCore.Menu.MenuType;
import com.Venom.VenomCore.Plugin.VenomPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alp Beji
 * @param <Object> the object that placed items going to represent.
 * A GUI that supports multiple pages.
 * @apiNote If you want the GUI to skip slots, simply use the reserveSlot method of the Container class.
 * @see GUI
 * @see Container
 */
public abstract class PagedGUI<Object> extends GUI {
    private final Inventory upperInventory;
    private final Container upperContainer;
    private static final int PAGE_NUMBER = 0;
    private final VenomPlugin plugin;
    private int currentPage;
    private final ConcurrentHashMap<Integer, Container> pages = new ConcurrentHashMap<>();

    private final MenuItem backButton;
    private final MenuItem nextButton;
    private boolean itemsPlaced = false;

    public PagedGUI(VenomPlugin plugin, String menuName, String title, int size) {
        super(plugin, menuName);
        this.plugin = plugin;
        this.upperInventory = Bukkit.createInventory(new MenuHolder(this), size, Color.translate(title));
        this.upperContainer = new Container(size);
        currentPage = PAGE_NUMBER;
        pages.put(PAGE_NUMBER, upperContainer);
        this.backButton = getBackButton();
        this.nextButton = getNextButton();

        ClickAction next = details -> {
            currentPage = currentPage + 1;
            update();
            return Result.DENY_CLICK;
        };

        nextButton.addAction(next, ClickType.LEFT, ClickType.RIGHT);

        ClickAction back = details -> {
            currentPage = currentPage - 1;
            update();
            return Result.DENY_CLICK;
        };

        backButton.addAction(back, ClickType.LEFT, ClickType.RIGHT);
    }

    public PagedGUI(VenomPlugin plugin, String menuName) {
        this(plugin, menuName, MenuUtils.getTitle(plugin, menuName), MenuUtils.getSize(plugin, menuName));
    }


    @Override
    public void open(Player p) {
        if (isSwitching() && !isItemsPlaced()) {
            placeItems();
        }

        p.openInventory(getUpperInventory());
        viewers.add(p);
    }

    @Override
    public void update() {
        Container container = pages.get(currentPage);

        upperInventory.clear();

        for (int i = 0; i < container.getSize(); i++) {
            if (container.get(i) == null)
                continue;

            upperInventory.setItem(i, container.get(i).getItem().toItemStack());
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
    public PagedGUI<Object> construct() {
        if (!isItemsPlaced()) {
            placeItems();
        }
        if (!isSwitching()) {
            update();
        }
        upperContainer.getFrames().forEach(frame -> runFrame(frame, getUpperContainer()));
        runItemAnimations(getUpperContainer());
        return this;
    }

    public void placeItems() {
        int page = PAGE_NUMBER;
        List<Object> objects = new ArrayList<>(objectsToPlace());

        double pageAmount = Math.ceil((double) objects.size() / (double) upperContainer.getFreeSlotCount());
        for (int i = 1; i < pageAmount; i++) {
            Container container = new Container(upperContainer.getSize());
            pages.get(PAGE_NUMBER).copy(container);
            pages.put(i, container);
        }

        for (Integer integer : pages.keySet()) {
            Container currentPage = pages.get(integer);
            if (pages.get(integer + 1) != null) {
                currentPage.set(nextButton, nextButtonSlot());
            }

            if (pages.get(integer - 1) != null) {
                currentPage.set(backButton, backButtonSlot());
            }
        }


        Container currentContainer = pages.get(page);

        int a = 0;
        while (currentContainer.getNextFreeSlot() != -1) {
            if (a < objects.size()) {
                Object object = objects.get(a);
                currentContainer.set(getItemForObject(object), currentContainer.getNextFreeSlot());
                if (currentContainer.getNextFreeSlot() == -1) {
                    page = page + 1;
                    currentContainer = pages.get(page);
                    if (currentContainer == null)
                        break;
                }
                a = a + 1;
            } else {
                break;
            }
        }
        itemsPlaced = true;
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

    @Override
    public void onClick(InventoryClickEvent e) {
        MenuItem item;

        Container container = pages.get(currentPage);

        if (container.get(e.getSlot()) == null || !ItemUtils.isEqual(container.get(e.getSlot()).getItem().toItemStack(), e.getCurrentItem())) {
            item = loopAndFindItem(e.getSlot(), e.getCurrentItem());
        } else {
            item = container.get(e.getSlot());
        }

        if (item == null || isLocked()) {
            e.setCancelled(true);
            return;
        }

        Player p = (Player) e.getWhoClicked();

        ActionDetails details = new ActionDetails(p, this);

        if (item.getSound() != null) {
            p.playSound(p.getLocation(), item.getSound(), 1f, 1f);
        }

        e.setCancelled(item.getAction(e.getClick()).run(details).isCancelled());

        MenuItem itemForClick = item.getItemForClick(e.getClick());
        if (itemForClick != null && !details.isItemForClickCancelled()) {
            container.set(itemForClick, e.getSlot());
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

    public boolean isItemsPlaced() {
        return itemsPlaced;
    }

    public abstract MenuItem getItemForObject(Object object);

    public abstract List<Object> objectsToPlace();

    public abstract MenuItem getBackButton();

    public abstract MenuItem getNextButton();

    public abstract int nextButtonSlot();

    public abstract int backButtonSlot();

}
