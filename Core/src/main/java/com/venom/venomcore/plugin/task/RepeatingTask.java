package com.venom.venomcore.plugin.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RepeatingTask extends Task implements Runnable {

    public RepeatingTask(JavaPlugin plugin, int arg1, int arg2) {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, arg1, arg2);
    }
}
