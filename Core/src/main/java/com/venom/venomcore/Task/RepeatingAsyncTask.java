package com.venom.venomcore.Task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RepeatingAsyncTask extends Task implements Runnable {
    private final int taskId;

    public RepeatingAsyncTask(JavaPlugin plugin, int arg1, int arg2) {
        taskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this, arg1, arg2);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
