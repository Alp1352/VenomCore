package com.venom.venomcore.Menu.Internal.Animations.DefaultAnimations;

import com.venom.venomcore.Menu.Internal.Containers.Container;

import java.util.HashMap;

public abstract class SwitchAnimation {

    protected final int size;
    protected int targetSize;

    public SwitchAnimation(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public int getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(int targetSize) {
        this.targetSize = targetSize;
    }

    public abstract int getStayTicks();

    public abstract int getTotalTicks();

    public abstract HashMap<Integer, Container> getInventories();
}
