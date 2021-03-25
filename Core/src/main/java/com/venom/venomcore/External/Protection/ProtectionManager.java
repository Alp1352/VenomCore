package com.venom.venomcore.External.Protection;

import com.venom.venomcore.External.HookManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ProtectionManager {
    private static final HookManager<ProtectionHook> manager = new HookManager<>(ProtectionHook.class);
    private final JavaPlugin plugin;
    public ProtectionManager(JavaPlugin plugin) {
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
    public static HookManager<ProtectionHook> getManager() {
        return manager;
    }

    /**
     * Gets the current hook.
     * If all hooks in the system are disabled,
     * this will return null.
     * @return The current hook.
     */
    public ProtectionHook getProtection() {
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
     * Checks if the player can build in the given location.
     * @param player The player to check.
     * @param location The location to check.
     * @return True if the player can place blocks.
     */
    public boolean canPlace(Player player, Location location) {
        return getProtection().canPlace(player, location);
    }

    /**
     * Checks if the player can break the block in the given location.
     * @param player The player to check.
     * @param location The location to check.
     * @return True if the player can break blocks.
     */
    public boolean canBreak(Player player, Location location) {
        return getProtection().canBreak(player, location);
    }

    /**
     * Checks if the player can interact with a block in the given location.
     * @param player The player to check.
     * @param location The location to check.
     * @return True if the player can interact with blocks in the given location.
     */
    public boolean canInteract(Player player, Location location) {
        return getProtection().canInteract(player, location);
    }
}
