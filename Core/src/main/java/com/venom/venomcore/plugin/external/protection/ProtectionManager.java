package com.venom.venomcore.plugin.external.protection;

import com.venom.venomcore.plugin.external.HookManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ProtectionManager {
    private static final HookManager<ProtectionHook> MANAGER = new HookManager<>(ProtectionHook.class);
    private final JavaPlugin plugin;

    public ProtectionManager(JavaPlugin plugin) {
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
    public static HookManager<ProtectionHook> getManager() {
        return MANAGER;
    }

    /**
     * Gets the current hook.
     * If all hooks in the system are disabled,
     * this will return null.
     * @return The current hook.
     */
    public ProtectionHook getProtection() {
        return MANAGER.getCurrentHook(plugin);
    }
}
