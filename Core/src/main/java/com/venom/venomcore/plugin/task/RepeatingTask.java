package com.venom.venomcore.plugin.task;

import org.bukkit.plugin.Plugin;

public abstract class RepeatingTask extends Task implements Runnable {
    public RepeatingTask(Plugin plugin, int arg1, int arg2) {
        this.taskID = Task.SCHEDULER.scheduleSyncRepeatingTask(plugin, this, arg1, arg2);
    }
}
