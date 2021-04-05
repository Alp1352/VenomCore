package com.venom.venomcore.plugin.external.economy;

import com.venom.venomcore.plugin.external.HookManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyManager {
    private static final HookManager<EconomyHook> MANAGER = new HookManager<>(EconomyHook.class);
    private final JavaPlugin plugin;
    public EconomyManager(JavaPlugin plugin) {
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
    public static HookManager<EconomyHook> getManager() {
        return MANAGER;
    }

    /**
     * Gets the current hook.
     * If all hooks in the system are disabled,
     * this will return null.
     * @return The current hook.
     */
    public EconomyHook getEconomy() {
        return MANAGER.getCurrentHook(plugin);
    }

}
