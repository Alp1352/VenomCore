package com.venom.venomcore.External;

import org.bukkit.Bukkit;

public interface Hook {
    /**
     * Gets the name of the hook.
     * @return The name of the hook.
     */
    String getName();

    /**
     * Checks if the plugin is enabled or not.
     * @return True if the plugin is enabled and ready to hook.
     */
    default boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled(getName());
    }
}
