package com.venom.venomcore.External.Skyblock;
import com.venom.venomcore.External.HookManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class SkyBlockManager {
    private static final HookManager<SkyBlockHook> manager = new HookManager<>(SkyBlockHook.class);
    private final JavaPlugin plugin;

    public SkyBlockManager(JavaPlugin plugin) {
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
     * @param plugin The plugin loading this manager.
     */
    public static void load(JavaPlugin plugin) {
        manager.load(plugin);
    }

    /**
     * Gets the manager.
     * @return The manager.
     */
    public static HookManager<SkyBlockHook> getManager() {
        return manager;
    }

    /**
     * Gets the current hook.
     * If all hooks in the system are disabled,
     * this will return null.
     * @return The current hook.
     */
    public SkyBlockHook getSkyBlock() {
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
     * Creates an island for a player.
     * @param player The player that will be the owner of the new island.
     * @param schematic The schematic name to paste. You can leave it null.
     */
    public void createIsland(Player player, @Nullable String schematic) {
        manager.getCurrentHook(plugin).createIsland(player, schematic);
    }

    /**
     * Deletes an island.
     * @param player The owner of the island.
     */
    public void deleteIsland(Player player) {
        manager.getCurrentHook(plugin).deleteIsland(player);
    }

    /**
     * Resets an island.
     * @param player The owner of the island.
     */
    public void resetIsland(Player player) {
        manager.getCurrentHook(plugin).resetIsland(player);
    }

    /**
     * Get the total island count.
     * @return All islands in the server.
     */
    public int getIslandCount() {
        return manager.getCurrentHook(plugin).getIslandCount();
    }

    /**
     * Checks if an island exist in the given location
     * @param location The location to check.
     * @return The created island.
     */
    public IslandHook getIslandAt(Location location) {
        return manager.getCurrentHook(plugin).getIslandAt(location);
    }

    /**
     * Get the island from an owner.
     * @param uuid The owners uuid.
     * @return The island. If player does not own an island, null.
     */
    public IslandHook getIslandOwnedBy(UUID uuid) {
        return manager.getCurrentHook(plugin).getIslandOwnedBy(uuid);
    }

    /**
     * Gets the island world.
     * @return The world that contains all islands.
     */
    public World getIslandWorld() {
        return manager.getCurrentHook(plugin).getIslandWorld();
    }

    /**
     * Get the owner from an island location.
     * @param location The location.
     * @return The owners UUID.
     */
    public UUID getOwner(Location location) {
        return manager.getCurrentHook(plugin).getOwner(location);
    }

    /**
     * Checks if the player is on an island.
     * @param player The player to check.
     * @return True if the player is on an island.
     */
    public boolean playerIsOnIsland(Player player) {
        return manager.getCurrentHook(plugin).playerIsOnIsland(player);
    }
}
