package com.venom.venomcore.plugin.external.placeholder;

import com.venom.venomcore.plugin.external.HookManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaceholderManager {
    private static final HookManager<PlaceholderHook> MANAGER = new HookManager<>(PlaceholderHook.class);
    private final JavaPlugin plugin;

    public PlaceholderManager(JavaPlugin plugin) {
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
    public static HookManager<PlaceholderHook> getManager() {
        return MANAGER;
    }

    /**
     * Gets the current hook.
     * If all hooks in the system are disabled,
     * this will return null.
     * @return The current hook.
     */
    public PlaceholderHook getPlaceholderHook() {
        return MANAGER.getCurrentHook(plugin);
    }
}
