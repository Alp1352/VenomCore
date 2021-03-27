package com.venom.venomcore.plugin.menu.internal.containers.panes;

import com.venom.venomcore.plugin.menu.internal.containers.Container;
import com.venom.venomcore.plugin.menu.internal.item.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Alp Beji
 * Represents an area of a GUI.
 * ToDo : Rotate and flip methods.
 * @see PaneSelector
 */
public class Pane {
    private final Container container;

    private final Integer[] slots;
    private int length;
    private int height;
    private int bigSlot;
    private int smallSlot;

    public Pane(Container container, int slotOne, int slotTwo) {
        this.container = container;
        List<Integer> slots = getSlots(slotOne, slotTwo);
        Integer[] array = new Integer[slots.size()];
        array = slots.toArray(array);
        this.slots = array;
    }

    private List<Integer> getSlots(int firstSlot, int secondSlot) {
        bigSlot = Math.max(firstSlot, secondSlot);
        smallSlot = Math.min(firstSlot, secondSlot);

        List<Integer> slots = new ArrayList<>();
        int height = 0;
        while (smallSlot + (9 * height) <= bigSlot) {
            height = height + 1;
        }
        int a = smallSlot;
        int length = 0;
        while (a != bigSlot) {
            if (a + 9 > container.getSize()) {
                length = length + 1;
                a = smallSlot + length;
                continue;
            }
            a = a + 9;
        }

        for (int i = smallSlot; i <= smallSlot + length; i++) {
            int c;
            int loopedAmount = 0;
            while (loopedAmount < height) {
                c = i;
                c = c + (9 * loopedAmount);
                loopedAmount = loopedAmount + 1;
                slots.add(c);
            }
        }

        this.height = height;
        this.length = length;
        Collections.sort(slots);
        return slots;
    }

    /*
    public void rotate(Degree degree) {
        if (height != length)
            return;

        for (int i = 0; i < (degree.getDegree() / 90); i++) {
            rotate90Degrees();
        }
    }

    // BROKEN
    private void rotate90Degrees() {
        int big = bigSlot;
        HashMap<Integer, MenuItem> oldItems = new HashMap<>();
        for (Integer slot : slots) {
            oldItems.put(slot, container.getItem(slot));
        }
        int loopedAmount = 0;
        while (loopedAmount != (length * height)) {
            int rotationSlot = (big - loopedAmount) - (9 * (length - 1));
            container.setItem(oldItems.get(big - loopedAmount), rotationSlot);
            loopedAmount = loopedAmount + 1;
        }
    }

    // BROKEN
    public void flip(Orientation orientation) {

    }
     */

    public void fillWith(MenuItem item) {
        for (Integer slot : slots) {
            container.set(item, slot);
        }
    }

    public void fillWith(MenuItem item, Integer... exceptions) {
        for (Integer slot : slots) {
            if (!Arrays.asList(exceptions).contains(slot)) {
                container.set(item, slot);
            }
        }
    }

    public enum Degree {
        NINETY(90),
        HUNDRED_AND_EIGHTY(180),
        TWO_HUNDRED_SEVENTY(270),
        THREE_HUNDRED_SIXTY(360);

        int degree;

        Degree(int degree) {
            this.degree = degree;
        }

        public int getDegree() {
            return degree;
        }
    }

    public enum Orientation {
        VERTICAL,
        HORIZONTAL;
    }

}
