package com.Venom.VenomCore.Menu.Types;

import com.Venom.VenomCore.Compatibility.CompatibleSound;
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
import com.Venom.VenomCore.NMS.NMSManager;
import com.Venom.VenomCore.Plugin.VenomPlugin;
import com.venom.nms.core.Anvil.AnvilContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Alp Beji
 * @apiNote An anvil gui for String prompts.
 * Uses NMS since creating anvil inventories with bukkit causes issues.
 * @see AnvilContainer
 * @see com.venom.nms.core.Anvil.AnvilCore
 */
public abstract class AnvilGUI extends GUI {
    private final VenomPlugin plugin;
    private final Container upperContainer;
    private final String title;

    private Inventory upperInventory;
    private AnvilContainer container;

    private ClickAction action;
    private CompatibleSound sound;

    public AnvilGUI(VenomPlugin plugin, String menuName, String title) {
        super(plugin, menuName);
        this.plugin = plugin;
        this.title = title;
        this.upperContainer = new Container(3);
    }

    public AnvilGUI(VenomPlugin plugin, String menuName) {
        this(plugin, menuName, MenuUtils.getTitle(plugin, menuName));
    }

    @Override
    public void open(Player p) {
        if (!viewers.isEmpty() && !viewers.contains(p)) {
            close();
        }

        viewers.add(p);
        container = NMSManager.getAnvil().createAnvil(p, new MenuHolder(this), title);
        upperInventory = container.toBukkit();
        construct();
        container.open();
    }

    @Override
    public void close() {
        for (int i = 0; i < viewers.size(); i++) {
            viewers.get(i).closeInventory();
        }
        viewers.clear();
    }

    @Override
    public AnvilGUI construct() {
        if (getUpperInventory() != null) {
            update();
            getUpperContainer().getFrames().forEach(frame -> runFrame(frame, getUpperContainer()));
            runItemAnimations(getUpperContainer());
        }
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
    public MenuType getType() {
        return MenuType.ANVIL;
    }

    /**
     * Sets the action that will be ran
     * after the player renames the item.
     * @param action The action that will run.
     */
    public void setActionAfterRename(ClickAction action) {
        this.action = action;
    }

    /**
     * Sets the sound that will play
     * after the player renames the item.
     * @param sound The sound that will run.
     */
    public void setSoundAfterRename(CompatibleSound sound) {
        this.sound = sound;
    }

    /**
     * Gets the rename text.
     * This is for the entered string prompt.
     * @return The rename text.
     */
    public String getRenameText() {
        return container.getRenameText();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.ANVIL)
            return;

        // Check if the player renamed the item.
        if (e.getSlot() == 2 && getUpperContainer().get(2) == null || e.getCurrentItem() != null && e.getCurrentItem().equals(getUpperContainer().get(2).getItem().toItemStack())) {
            Result result = null;
            if (action != null) {
                result = action.run(new ActionDetails((Player) e.getWhoClicked(), this));
            }

            if (sound != null) {
                sound.play(e.getWhoClicked());
            }

            boolean cancel = true;
            if (result != null) {
                cancel = result.isCancelled();
            }
            e.setCancelled(cancel);

            return;
        }

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
}
