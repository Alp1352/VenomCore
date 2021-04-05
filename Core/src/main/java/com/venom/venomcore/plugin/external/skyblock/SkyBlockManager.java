package com.venom.venomcore.plugin.external.skyblock;

import com.venom.venomcore.plugin.external.HookManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyBlockManager {
    private static final HookManager<SkyBlockHook> MANAGER = new HookManager<>(SkyBlockHook.class);
    private final JavaPlugin plugin;

    public SkyBlockManager(JavaPlugin plugin) {
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
    public static HookManager<SkyBlockHook> getManager() {
        return MANAGER;
    }

    /**
     * Gets the current hook.
     * If all hooks in the system are disabled,
     * this will return null.
     * @return The current hook.
     */
    public SkyBlockHook getSkyBlock() {
        return MANAGER.getCurrentHook(plugin);
    }
}
