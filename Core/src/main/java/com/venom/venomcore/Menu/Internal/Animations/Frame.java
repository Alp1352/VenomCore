package com.venom.venomcore.Menu.Internal.Animations;

import com.venom.venomcore.Menu.Internal.Containers.Container;
import com.venom.venomcore.Menu.Internal.Item.MenuItem;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Frame {
    private final Container container;
    private int slot;
    //
    private int times;
    private int ticksBetweenItems;
    private Sound sound;
    private final List<MenuItem> items = Collections.synchronizedList(new ArrayList<>());
    
    public Frame(Container container, int slot) {
        this.container = container;
        this.slot = slot;
        container.reserve(slot);
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void setSlot(int slot) {
        container.unreserve(this.slot);
        container.reserve(slot);
        this.slot = slot;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public Container getContainer() {
        return container;
    }

    public int getSlot() {
        return slot;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getTimes() {
        return times;
    }

    public void setTicksBetweenItems(int ticksBetweenItems) {
        this.ticksBetweenItems = ticksBetweenItems;
    }

    public int getTicksBetweenItems() {
        return ticksBetweenItems;
    }

}
