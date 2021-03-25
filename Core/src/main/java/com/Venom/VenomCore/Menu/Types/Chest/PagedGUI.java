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
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
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

        this.upperInventory = Bukkit.createInventory(new MenuHolder(this), size, Color.translate(title));

        this.upperContainer = new Container(size);

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

        super.open(p);
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
    public PagedGUI<Object> construct() {
        if (!isItemsPlaced()) {
            placeItems();
        }
        super.construct();
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

        int index = 0;
        while (currentContainer != null && index < objects.size() && currentContainer.getNextFreeSlot() != -1) {
            Object object = objects.get(index);
            currentContainer.set(getItemForObject(object), currentContainer.getNextFreeSlot());

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

    public abstract MenuItem getItemForObject(Object object);

    public abstract List<Object> objectsToPlace();

    public abstract MenuItem getBackButton();

    public abstract MenuItem getNextButton();

    public abstract int nextButtonSlot();

    public abstract int backButtonSlot();

}
