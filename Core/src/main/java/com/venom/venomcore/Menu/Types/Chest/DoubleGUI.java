package com.venom.venomcore.Menu.Types.Chest;

import com.venom.venomcore.Chat.Color;
import com.venom.venomcore.Menu.Engine.MenuHolder;
import com.venom.venomcore.Menu.GUI;
import com.venom.venomcore.Menu.Internal.Containers.Container;
import com.venom.venomcore.Menu.Internal.Item.MenuItem;
import com.venom.venomcore.Menu.Internal.Utils.MenuUtils;
import com.venom.venomcore.Menu.MenuType;
import com.venom.venomcore.Plugin.VenomPlugin;
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
    private final Inventory upperInventory;
    private final Container upperContainer;
    private final Container lowerContainer;

    private final ConcurrentHashMap<UUID, ItemStack[]> items = new ConcurrentHashMap<>();

    public DoubleGUI(VenomPlugin plugin, String menuName, String title, int size) {
        super(plugin, menuName);
        this.upperInventory = Bukkit.createInventory(new MenuHolder(this), size, Color.translate(title));
        this.upperContainer = new Container(size);
        this.lowerContainer = new Container(36);
    }

    public DoubleGUI(VenomPlugin plugin, String menuName) {
        this(plugin, menuName, MenuUtils.getTitle(plugin, menuName), MenuUtils.getSize(plugin, menuName));
    }

    @Override
    public void open(Player p) {
        items.putIfAbsent(p.getUniqueId(), p.getInventory().getContents().clone());
        p.getInventory().clear();

        super.open(p);
        update();
    }

    @Override
    public void update() {
        super.update();
        for (int i = 0; i < lowerContainer.getSize(); i++) {
            MenuItem item = lowerContainer.get(i);
            if (item != null) {
                for (Player viewer : viewers) {
                    viewer.getInventory().setItem(i, item.getItem().toItemStack());
                }
            }
        }
    }

    @Override
    public DoubleGUI construct() {
        super.construct();

        lowerContainer.getFrames().forEach(frame -> runFrame(frame, lowerContainer));
        runItemAnimations(lowerContainer);
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
        super.onClick(e, e.getRawSlot() >= upperContainer.getSize() ? lowerContainer : upperContainer);
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        super.onClose(e);
        if (isClosable()) {
            Player player = (Player) e.getPlayer();
            ItemStack[] contents = items.get(player.getUniqueId());
            player.getInventory().setContents(contents);

            items.remove(player.getUniqueId());
        }
    }

}
