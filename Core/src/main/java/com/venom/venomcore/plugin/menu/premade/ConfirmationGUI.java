package com.venom.venomcore.plugin.menu.premade;

import com.venom.venomcore.plugin.compatibility.CompatibleMaterial;
import com.venom.venomcore.plugin.compatibility.CompatibleSound;
import com.venom.venomcore.plugin.item.ItemBuilder;
import com.venom.venomcore.plugin.menu.internal.animations.Animation;
import com.venom.venomcore.plugin.menu.internal.item.MenuItem;
import com.venom.venomcore.plugin.menu.internal.item.action.ClickAction;
import com.venom.venomcore.plugin.menu.types.chest.SimpleGUI;
import com.venom.venomcore.plugin.plugin.VenomPlugin;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.Nullable;

public class ConfirmationGUI extends SimpleGUI {
    private ClickAction onConfirm;
    private ClickAction onDeny;

    public ConfirmationGUI(VenomPlugin plugin, String title, @Nullable ClickAction onConfirm, @Nullable ClickAction onDeny) {
        super(plugin, null, title, 27);
        this.onConfirm = onConfirm;
        this.onDeny = onDeny;
        setup();
    }

    private void setup() {
        // Emerald
        setupConfirm();
        // Redstone
        setupDeny();
        // Window
        setupGlass();
    }

    private void setupConfirm() {
        ItemBuilder item = new ItemBuilder(CompatibleMaterial.EMERALD_BLOCK.parseItem());
        item.setGlowing(true).setName("ONAYLA");
        Animation animation = new Animation(Animation.AnimationType.WAVE, ChatColor.GREEN, ChatColor.WHITE);
        animation.setBold(true);

        MenuItem menuItem = new MenuItem(item);
        menuItem.setAnimation(animation);
        menuItem.setSound(CompatibleSound.ENTITY_EXPERIENCE_ORB_PICKUP.parseSound());
        if (onConfirm != null) {
            menuItem.addAction(onConfirm, ClickType.LEFT, ClickType.RIGHT);
        }

        getUpperContainer().set(menuItem, 15);
    }

    private void setupDeny() {
        ItemBuilder item = new ItemBuilder(CompatibleMaterial.REDSTONE_BLOCK.parseItem());
        item.setGlowing(true).setName("REDDET");
        Animation animation = new Animation(Animation.AnimationType.WAVE, ChatColor.RED, ChatColor.WHITE);
        animation.setBold(true);

        MenuItem menuItem = new MenuItem(item);
        menuItem.setAnimation(animation);
        menuItem.setSound(CompatibleSound.ENTITY_BAT_DEATH.parseSound());
        if (onDeny != null) {
            menuItem.addAction(onDeny, ClickType.LEFT, ClickType.RIGHT);
        }

        getUpperContainer().set(menuItem, 11);
    }

    private void setupGlass() {
        getUpperContainer().reserve(10, 12, 13, 14, 16);
        MenuItem item = new MenuItem(new ItemBuilder(CompatibleMaterial.BLACK_STAINED_GLASS_PANE.parseItem()));
        getUpperContainer().fill(item);
    }

    public void setOnConfirm(ClickAction onConfirm) {
        this.onConfirm = onConfirm;
    }

    public void setOnDeny(ClickAction onDeny) {
        this.onDeny = onDeny;
    }
}
