package com.venom.venomcore.plugin.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class BukkitExecutor implements Executor {
    private final JavaPlugin plugin;
    private final BukkitScheduler scheduler;
    public BukkitExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
        this.scheduler = Bukkit.getScheduler();
    }
    
    @Override
    public void execute(@NotNull Runnable runnable) {
        scheduler.runTask(plugin, runnable);
    }
}
