package com.Venom.VenomCore.Task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class BukkitExecutor implements Executor {
    private final JavaPlugin plugin;
    public BukkitExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void execute(@NotNull Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }
}
