package com.venom.venomcore.nms.core.hologram;

import java.util.function.Consumer;

public interface HologramTimer {
    void start();

    void stop();

    int getSeconds();

    int getTime(Time time);

    Status getStatus();

    void setSeconds(int seconds);

    void setRepeating(boolean repeating);

    void setFreezed(boolean freeze);

    void setStatusText(Status status, String text);

    void setOnStatusChange(Consumer<Status> action);

    boolean isRepeating();

    boolean isFreezed();

    boolean isStarted();

    boolean isStopped();
}
