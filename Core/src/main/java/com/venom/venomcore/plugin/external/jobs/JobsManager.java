package com.venom.venomcore.plugin.external.jobs;

import com.venom.venomcore.plugin.external.HookManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class JobsManager {
    private static final HookManager<JobsHook> MANAGER = new HookManager<>(JobsHook.class);
    private final JavaPlugin plugin;

    public JobsManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads the manager.
     */
    public static void load() {
        MANAGER.load();
    }

    /**
     * Gets the manager.
     * @return The manager.
     */
    public static HookManager<JobsHook> getManager() {
        return MANAGER;
    }

    /**
     * Gets the current hook.
     * If all hooks in the system are disabled,
     * this will return null.
     * @return The current hook.
     */
    public JobsHook getJobs() {
        return MANAGER.getCurrentHook(plugin);
    }

    /**
     * Gets the player hook.
     * @param player The player.
     * @return The player hook.
     */
    public JobsPlayerHook getPlayerHook(Player player) {
        return getJobs().getJobsPlayerHook(player);
    }
}
