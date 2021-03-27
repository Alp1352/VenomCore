package com.venom.venomcore.plugin.menu.internal.builders;

import com.venom.venomcore.plugin.menu.internal.animations.Frame;
import com.venom.venomcore.plugin.menu.internal.containers.Container;
import com.venom.venomcore.plugin.menu.internal.item.MenuItem;
import org.bukkit.Sound;

public class FrameBuilder {
    private final Frame frame;
    public FrameBuilder(Container container, int slot) {
        this.frame = new Frame(container, slot);
    }

    public FrameBuilder addItem(MenuItem item) {
        frame.addItem(item);
        return this;
    }

    public FrameBuilder setTimes(int times) {
        frame.setTimes(times);
        return this;
    }

    public FrameBuilder setTickBetweenItems(int tickBetweenItems) {
        frame.setTicksBetweenItems(tickBetweenItems);
        return this;
    }

    public FrameBuilder setSound(Sound sound) {
        frame.setSound(sound);
        return this;
    }

    public Frame build() {
        return frame;
    }
}
