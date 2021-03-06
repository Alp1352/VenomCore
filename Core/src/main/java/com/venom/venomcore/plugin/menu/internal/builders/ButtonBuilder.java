package com.venom.venomcore.plugin.menu.internal.builders;

import com.venom.venomcore.plugin.item.ItemBuilder;
import com.venom.venomcore.plugin.menu.internal.animations.Animation;
import com.venom.venomcore.plugin.menu.internal.item.MenuItem;
import com.venom.venomcore.plugin.menu.internal.item.action.ClickAction;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;

public class ButtonBuilder {
    private final MenuItem item;
    public ButtonBuilder(ItemBuilder builder) {
        this.item = new MenuItem(builder);
    }

    public ButtonBuilder withAction(ClickAction action, ClickType type) {
        item.addAction(action, type);
        return this;
    }

    public ButtonBuilder withSound(Sound sound) {
        item.setSound(sound);
        return this;
    }

    public ButtonBuilder withPlaceholders(HashMap<String, String> placeholders) {
        item.setPlaceholder(placeholders);
        return this;
    }

    public ButtonBuilder withAnimation(Animation animation) {
        item.setAnimation(animation);
        return this;
    }

    public MenuItem build() {
        return item;
    }
}
