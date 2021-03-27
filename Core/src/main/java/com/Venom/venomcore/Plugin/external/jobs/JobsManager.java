package com.venom.venomcore.plugin.external.jobs;

import com.venom.venomcore.plugin.external.HookManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class JobsManager {
    private static final HookManager<JobsHook> manager = new HookManager<>(JobsHook.class);

    private final JavaPlugin plugin;
    public JobsManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads the manager.
     */
    public void load() {
        manager.load(plugin);
    }

    /**
     * Loads the manager.
     * @param plugin The plugin loading the manager.
     */
    public static void load(JavaPlugin plugin) {
        manager.load(plugin);
    }

    /**
     * Gets the manager.
     * @return The manager.
     */
    public static HookManager<JobsHook> getManager() {
        return manager;
    }

    /**
     * Gets the current hook.
     * If all hooks in the system are disabled,
     * this will return null.
     * @return The current hook.
     */
    public JobsHook getJobs() {
        return manager.getCurrentHook(plugin);
    }

    /**
     * Get all the possible plugins.
     * @return All possible plugins.
     */
    public static List<String> getPossiblePlugins() {
        return manager.getPossiblePlugins();
    }

    /**
     * Check if the manager is enabled.
     * @return True if there is at least 1 enabled plugin.
     */
    public static boolean isEnabled() {
        return manager.isEnabled();
    }

    /**
     * Gets the name of the current hook.
     * @return The name of the current hook.
     */
    public static String getName() {
        return manager.getName();
    }

    /**
     * Gets the player hook.
     * @param player The player.
     * @return The player hook.
     */
    public JobsPlayerHook getPlayerHook(Player player) {
        return getJobs().getJobsPlayerHook(player);
    }

    /**
     * Gets all the jobs in the system.
     * @return All the jobs.
     */
    public List<String> getAllJobs() {
        return getJobs().getJobs();
    }
}
