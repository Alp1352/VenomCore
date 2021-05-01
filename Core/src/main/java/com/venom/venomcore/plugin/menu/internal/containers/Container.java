package com.venom.venomcore.plugin.menu.internal.containers;

import com.venom.venomcore.plugin.menu.internal.animations.Frame;
import com.venom.venomcore.plugin.menu.internal.containers.panes.PaneSelector;
import com.venom.venomcore.plugin.menu.internal.item.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class Container implements Iterable<MenuItem> {

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

    public boolean isFree(int slot) {
        return isEmpty(slot) && !isReserved(slot);
    }

    public boolean isEmpty(int slot) {
        return get(slot) == null;
    }

    public boolean isFull(int slot) {
        return get(slot) != null;
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

    public boolean isReserved(int slot) {
        return reservedSlots.contains(slot);
    }

    public Collection<MenuItem> getContents() {
        return items.values();
    }

    public int getSize() {
        return size;
    }

    public int getNextFreeSlot() {
        return getNextFreeSlot(0);
    }

    public int getNextFreeSlot(int beginIndex) {
        return IntStream.range(beginIndex, size)
                .filter(this::isFree)
                .findAny()
                .orElse(-1);
    }

    public int getFreeSlotCount() {
        return (int) IntStream.range(0, size)
                .boxed()
                .filter(this::isFree)
                .count();
    }

    @NotNull
    @Override
    public Iterator<MenuItem> iterator() {
        return items.values()
                .iterator();
    }
}
