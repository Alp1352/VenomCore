package com.venom.venomcore.plugin.menu.internal.item;

import com.venom.venomcore.plugin.item.ItemBuilder;
import com.venom.venomcore.plugin.menu.internal.animations.Animation;
import com.venom.venomcore.plugin.menu.internal.item.action.ClickAction;
import com.venom.venomcore.plugin.menu.internal.item.action.Result;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MenuItem {
    private final ConcurrentHashMap<ClickType, ClickAction> actions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ClickType, MenuItem> itemsForClick = new ConcurrentHashMap<>();
    private ItemBuilder item;
    private Sound sound;
    private Animation animation;

    public MenuItem(ItemBuilder item) {
        this.item = item;
    }


    public void setAnimation(Animation animation) {
        animation.createAnimation(item.getName());
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setItem(ItemBuilder item) {
        this.item = item;

        animation.getAnimations().clear();
        animation.createAnimation(item.getName());
    }

    public ItemBuilder getItem() {
        return item;
    }

    public void addItemForClick(ClickType type, MenuItem item) {
        itemsForClick.put(type, item);
    }

    public MenuItem getItemForClick(ClickType type) {
        return itemsForClick.get(type);
    }

    public void addAction(ClickAction action, ClickType... type) {
        for (ClickType clickType : type) {
            actions.put(clickType, action);
        }
    }

    public ClickAction getAction(ClickType type) {
        ClickAction action = actions.get(type);
        if (action == null) {
            action = details -> Result.DENY_CLICK;
        }
        return action;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public Sound getSound() {
        return sound;
    }

    public boolean hasAnimation() {
        return animation != null;
    }

    public boolean hasSound() {
        return sound != null;
    }

    @Deprecated
    private String setPlaceholder(String str, Map<String, String> map) {
        if (map == null)
            return str;
        for (Map.Entry<String, String> entr : map.entrySet()) {
            str = str.replace(entr.getKey(), entr.getValue());
        }
        return str;
    }

    @Deprecated
    public void setPlaceholder(Map<String, String> map) {
        if (item.getLore() != null) {
            List<String> list = item.getLore().stream().map(x -> setPlaceholder(x, map)).collect(Collectors.toList());
            item.setLore(list);
        }
        if (item.getName() != null) {
            item.setName(setPlaceholder(item.getName(), map));
        }
    }

}