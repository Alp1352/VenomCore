package com.venom.venomcore.plugin.menu.types;

import com.venom.venomcore.nms.core.anvil.AnvilContainer;
import com.venom.venomcore.plugin.menu.GUI;
import com.venom.venomcore.plugin.menu.MenuType;
import com.venom.venomcore.plugin.menu.engine.MenuHolder;
import com.venom.venomcore.plugin.menu.internal.containers.Container;
import com.venom.venomcore.plugin.menu.internal.item.action.ActionDetails;
import com.venom.venomcore.plugin.menu.internal.item.action.ClickAction;
import com.venom.venomcore.plugin.menu.internal.item.action.Result;
import com.venom.venomcore.plugin.menu.internal.utils.MenuUtils;
import com.venom.venomcore.plugin.nms.NMSManager;
import com.venom.venomcore.plugin.plugin.VenomPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * @author Alp Beji
 * @apiNote An anvil gui for String prompts.
 * Uses NMS since creating anvil inventories with bukkit causes issues.
 * @see AnvilContainer
 * @see com.venom.venomcore.nms.core.anvil.AnvilCore
 */
public abstract class AnvilGUI extends GUI {
    private final VenomPlugin plugin;
    private final Container upperContainer;
    private final String title;

    private Inventory upperInventory;
    private AnvilContainer container;

    private ClickAction action;
    private Sound sound;

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
        viewers.add(p);

        container = NMSManager.getAnvil().createAnvil(p, new MenuHolder(this), title);
        upperInventory = container.toBukkit();

        construct();
        container.open();
    }

    @Override
    public AnvilGUI construct() {
        if (upperInventory != null) {
            super.construct();
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
    public void setSoundAfterRename(Sound sound) {
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
        // Check if the player renamed the item.
        if (e.getSlot() != 2 || upperContainer.get(2) != null) {
            super.onClick(e);
            return;
        }

        if (e.getCurrentItem() == null)
            return;

        Player player = (Player) e.getWhoClicked();
        player.playSound(player.getLocation(), sound, 1f, 1f);

        Result result = action != null ? action.run(new ActionDetails(player, this)) : null;
        e.setCancelled(result == null || result.isCancelled());
    }
}
