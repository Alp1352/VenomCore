package com.venom.venomcore.plugin.task;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class Task {
    protected static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();
    protected int taskID;

    public void cancel() {
        SCHEDULER.cancelTask(taskID);
    }
}
