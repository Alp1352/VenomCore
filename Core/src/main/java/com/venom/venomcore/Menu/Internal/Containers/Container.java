package com.venom.venomcore.Menu.Internal.Containers;

import com.venom.venomcore.Menu.Internal.Animations.Frame;
import com.venom.venomcore.Menu.Internal.Containers.Panes.PaneSelector;
import com.venom.venomcore.Menu.Internal.Item.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
public class Container {

    private final List<Integer> reservedSlots = Collections.synchronizedList(new ArrayList<>());
    private final List<Frame> frames = Collections.synchronizedList(new ArrayList<>());

    private final ConcurrentHashMap<Integer, MenuItem> items = new ConcurrentHashMap<>();

    private final int size;
    private final PaneSelector selector;

    public Container(int size) {
        this.size = size;
        this.selector = new PaneSelector(this);
    }

    public void copy(Container container) {
        container.items.clear();
        container.items.putAll(items);
    }

    public void clear() {
        items.clear();
    }

    public PaneSelector getSelector() {
        return selector;
    }

    public void fill(MenuItem item) {
        fill(0, item);
    }

    public void fill(int startIndex, MenuItem item) {
        fill(startIndex, item, size);
    }

    public void fill(int startIndex, MenuItem item, int endIndex) {
        while (getNextFreeSlot(startIndex) != -1 && getNextFreeSlot() < endIndex) {
            items.put(getNextFreeSlot(startIndex), item);
        }
    }

    public void add(Frame frame) {
        frames.add(frame);
        reserve(frame.getSlot());
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public MenuItem get(int slot) {
        return items.get(slot);
    }

    public void set(MenuItem item, int slot) {
        if (slot >= size)
            return;

        if (item != null) {
            items.put(slot, item);
        } else {
            remove(slot);
        }
    }

    public void remove(int slot) {
        if (slot >= size)
            return;

        items.remove(slot);
    }

    public void reserve(Integer... slot) {
        reservedSlots.addAll(Arrays.asList(slot));
    }

    public void unreserve(Integer... slot) {
        reservedSlots.removeAll(Arrays.asList(slot));
    }

    public List<Integer> getReserved() {
        return reservedSlots;
    }

    public int getSize() {
        return size;
    }

    public int getNextFreeSlot() {
        return getNextFreeSlot(0);
    }

    public int getNextFreeSlot(int beginIndex) {
        for (int i = beginIndex; i < size; i++) {
            if (!reservedSlots.contains(i) && get(i) == null) {
                return i;
            }
        }
        return -1;
    }

    public int getFreeSlotCount() {
        int a = 0;
        for (int i = 0; i < size; i++) {
            if (!reservedSlots.contains(i) && get(i) == null) {
                a = a + 1;
            }
        }
        return a;
    }
}
