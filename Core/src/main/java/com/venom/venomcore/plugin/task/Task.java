package com.venom.venomcore.plugin.task;

import org.bukkit.Bukkit;

public abstract class Task {
    protected int taskID;

    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
