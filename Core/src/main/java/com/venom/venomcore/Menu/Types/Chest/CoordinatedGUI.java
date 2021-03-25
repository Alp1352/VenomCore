package com.venom.venomcore.Menu.Types.Chest;

import com.venom.venomcore.Chat.Color;
import com.venom.venomcore.Item.ItemBuilder;
import com.venom.venomcore.Menu.Engine.MenuHolder;
import com.venom.venomcore.Menu.GUI;
import com.venom.venomcore.Menu.Internal.Containers.Container;
import com.venom.venomcore.Menu.Internal.Item.MenuItem;
import com.venom.venomcore.Menu.Internal.Utils.MenuUtils;
import com.venom.venomcore.Menu.MenuType;
import com.venom.venomcore.Plugin.VenomPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.awt.geom.Point2D;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alp Beji
 * A gui where players can move around freely.
 * Do note that this is right now not stable.
 */
public abstract class CoordinatedGUI extends GUI {
    private final Inventory upperInventory;
    private final CoordinatedContainer upperContainer;

    public CoordinatedGUI(VenomPlugin plugin, String menuName, String title, int size) {
        super(plugin, menuName);
        this.upperInventory = Bukkit.createInventory(new MenuHolder(this), size, Color.translate(title));
        this.upperContainer = new CoordinatedContainer(size);
    }

    public CoordinatedGUI(VenomPlugin plugin, String menuName) {
        this(plugin, menuName, MenuUtils.getTitle(plugin, menuName), MenuUtils.getSize(plugin, menuName));
    }

    @Override
    public CoordinatedGUI construct() {
        update();
        upperContainer.getItems().values().forEach(coordinatedItem -> upperInventory.setItem(toSlot(coordinatedItem.getX(), coordinatedItem.getY()), coordinatedItem.getItem().toItemStack()));
        getUpperContainer().getFrames().forEach(frame -> runFrame(frame, getUpperContainer()));
        runItemAnimations(getUpperContainer());
        return this;
    }

    @Override
    protected Inventory getUpperInventory() {
        return upperInventory;
    }

    @Override
    public CoordinatedContainer getUpperContainer() {
        return upperContainer;
    }

    @Override
    public MenuType getType() {
        return MenuType.CHEST;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem().getType() == Material.AIR && upperContainer.get(e.getSlot()) == null) {
            onEmptySlotClick(e);
            e.setCancelled(true);
        }
    }

    private void onEmptySlotClick(InventoryClickEvent e) {
        int clickedX = (e.getSlot() % 9) + 1;
        int clickedY = (Math.floorDiv(e.getSlot(), 9)) + 1;
        boolean proceed = false;

        for (CoordinatedItem item : getUpperContainer().getItems().values()) {
            if (Point2D.distance(item.getX(), item.getY(), clickedX, clickedY) <= 5.0D) {
                proceed = true;
            }
        }

        if (!proceed) {
            return;
        }

        getUpperInventory().clear();
        for (CoordinatedItem item : getUpperContainer().getItems().values()) {
            // TODO newY and newX wrong, fix them
            int newX = item.getX() - (5 - clickedX);
            int newY = item.getY() - (3 - clickedY);

            item.setX(newX);
            item.setY(newY);

            int slot = toSlot(newX, newY);

            if (slot > 0 && slot < getUpperContainer().getSize()) {
                getUpperInventory().setItem(slot, item.getItem().toItemStack());
            }
        }

        update();
    }

    private int toSlot(int x, int y) {
        return ((y - 1) * 9) + x - 1;
    }

    public static class CoordinatedItem extends MenuItem {
        int x;
        int y;
        public CoordinatedItem(ItemBuilder item, int x, int y) {
            super(item);
            this.x = x;
            this.y = y;
        }


        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public static class CoordinatedContainer extends Container {
        private final ConcurrentHashMap<Integer, CoordinatedItem> items = new ConcurrentHashMap<>();
        public CoordinatedContainer(int size) {
            super(size);
        }

        public void setCoordinatedItem(CoordinatedItem item) {
            items.put(toSlot(item.getX(), item.getY()), item);
        }

        private ConcurrentHashMap<Integer, CoordinatedItem> getItems() {
            return items;
        }

        private int toSlot(int x, int y) {
            return ((y - 1) * 9) + x - 1;
        }
    }
}
