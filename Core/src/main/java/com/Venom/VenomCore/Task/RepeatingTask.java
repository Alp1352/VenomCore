package com.Venom.VenomCore.Task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RepeatingTask extends Task implements Runnable {

    private final int taskId;

    public RepeatingTask(JavaPlugin plugin, int arg1, int arg2) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, arg1, arg2);
    }

    @Override
    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

}
