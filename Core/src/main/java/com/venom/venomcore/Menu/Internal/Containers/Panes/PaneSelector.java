package com.venom.venomcore.Menu.Internal.Containers.Panes;

import com.venom.venomcore.Menu.Internal.Containers.Container;

public class PaneSelector {
    private final Container container;
    public PaneSelector(Container container) {
        this.container = container;
    }

    public Pane select(int slotOne, int slotTwo) {
        return new Pane(container, slotOne, slotTwo);
    }

    public Pane select(int x, int y, int height, int length) {
        int slotOne = (y - 1) * 9 + (x - 1);
        int slotTwo = (slotOne + length - 1) + (9 * (height - 1));
        return new Pane(container, slotOne, slotTwo);
    }
}
