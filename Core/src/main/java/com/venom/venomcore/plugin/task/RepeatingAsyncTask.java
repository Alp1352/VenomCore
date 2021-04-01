package com.venom.venomcore.plugin.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RepeatingAsyncTask extends Task implements Runnable {
    public RepeatingAsyncTask(JavaPlugin plugin, int arg1, int arg2) {
        taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this, arg1, arg2);
    }
}
