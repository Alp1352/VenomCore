package com.Venom.VenomCore.External.Placeholder;

import com.Venom.VenomCore.External.HookManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PlaceholderManager {
    private static final HookManager<PlaceholderHook> manager = new HookManager<>(PlaceholderHook.class);

    private final JavaPlugin plugin;
    public PlaceholderManager(JavaPlugin plugin) {
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
     */
    public static void load(JavaPlugin plugin) {
        manager.load(plugin);
    }

    /**
     * Gets the manager.
     * @return The manager.
     */
    public static HookManager<PlaceholderHook> getManager() {
        return manager;
    }

    /**
     * Gets the current hook.
     * If all hooks in the system are disabled,
     * this will return null.
     * @return The current hook.
     */
    public PlaceholderHook getPlaceholderHook() {
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
     * Sets the placeholders in a string.
     * @param player The player that will be used to get placeholder values.
     * @param text The string to replace.
     * @return The string after the placeholders are replaced.
     */
    public String setPlaceholders(Player player, String text) {
        return getPlaceholderHook().setPlaceholders(player, text);
    }
}
