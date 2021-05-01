package com.venom.venomcore.plugin.menu.types.chest;

import com.venom.venomcore.plugin.chat.Color;
import com.venom.venomcore.plugin.menu.GUI;
import com.venom.venomcore.plugin.menu.MenuType;
import com.venom.venomcore.plugin.menu.internal.containers.Container;
import com.venom.venomcore.plugin.menu.internal.item.MenuItem;
import com.venom.venomcore.plugin.menu.internal.item.action.ClickAction;
import com.venom.venomcore.plugin.menu.internal.item.action.Result;
import com.venom.venomcore.plugin.menu.internal.utils.MenuUtils;
import com.venom.venomcore.plugin.plugin.VenomPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

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

    private final ConcurrentHashMap<Integer, Container> pages = new ConcurrentHashMap<>();

    private static final int PAGE_NUMBER = 0;
    private int currentPage = PAGE_NUMBER;

    private final MenuItem backButton;
    private final MenuItem nextButton;

    private boolean itemsPlaced = false;

    public PagedGUI(VenomPlugin plugin, String menuName, String title, int size) {
        super(plugin, menuName);

        this.upperInventory = Bukkit.createInventory(this, size, Color.translate(title));

        this.upperContainer = new Container(size);

        pages.put(PAGE_NUMBER, upperContainer);

        this.backButton = new MenuItem(getUtils().getItem(getBackName()));
        this.nextButton = new MenuItem(getUtils().getItem(getNextName()));

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
            place();
        }

        super.open(p);
    }

    @Override
    public void update() {
        Container container = pages.get(currentPage);
        upperInventory.clear();

        IntStream.range(0, getSize())
                .filter(container::isFull)
                .forEach(slot -> upperInventory.setItem(slot, container.get(slot).getItem().toItemStack()));
    }

    @Override
    public PagedGUI<Object> construct() {
        setup();

        if (!isItemsPlaced()) {
            place();
        }

        if (!isSwitching()) {
            update();
        }

        getUpperContainer().getFrames()
                .forEach(this::runFrame);

        runItemAnimations();
        return this;
    }

    public void place() {
        int page = PAGE_NUMBER;
        List<Object> objects = new ArrayList<>(objects());

        double pageAmount = Math.ceil((double) objects.size() / (double) upperContainer.getFreeSlotCount());

        for (int i = 1; i < pageAmount; i++) {
            Container container = new Container(upperContainer.getSize());
            pages.get(PAGE_NUMBER).copy(container);
            pages.put(i, container);
        }

        pages.keySet().forEach(integer -> {
            Container current = pages.get(integer);

            if (pages.get(integer + 1) != null) { // There is a next page.
                current.set(nextButton, getUtils().getSlotOf(getNextName()));
            }

            if (pages.get(integer - 1) != null) { // There is a back page.
                current.set(backButton, getUtils().getSlotOf(getBackName()));
            }
        });


        Container currentContainer = pages.get(page);

        int index = 0;
        while (currentContainer != null && index < objects.size() && currentContainer.getNextFreeSlot() != -1) {
            Object object = objects.get(index);
            currentContainer.set(getItemFor(object), currentContainer.getNextFreeSlot());

            if (currentContainer.getNextFreeSlot() == -1) {
                page = page + 1;
                currentContainer = pages.get(page);
            }

            index = index + 1;
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
        super.onClick(e, pages.get(currentPage));
    }

    public boolean isItemsPlaced() {
        return itemsPlaced;
    }

    public abstract MenuItem getItemFor(Object object);

    public abstract Collection<Object> objects();

    public String getBackName() {
        return "back";
    }

    public String getNextName() {
        return "next";
    }

}
