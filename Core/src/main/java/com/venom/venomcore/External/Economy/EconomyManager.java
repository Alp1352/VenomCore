package com.venom.venomcore.External.Economy;

import com.venom.venomcore.External.HookManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class EconomyManager {
    private static final HookManager<EconomyHook> manager = new HookManager<>(EconomyHook.class);

    private final JavaPlugin plugin;
    public EconomyManager(JavaPlugin plugin) {
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
    public static HookManager<EconomyHook> getManager() {
        return manager;
    }

    /**
     * Gets the current hook.
     * If all hooks in the system are disabled,
     * this will return null.
     * @return The current hook.
     */
    public EconomyHook getEconomy() {
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
     * Gets the balance of a player.
     * @param player The player.
     * @return The balance.
     */
    public double getBalance(OfflinePlayer player) {
        if (!manager.isEnabled())
            return 0;
        return manager.getCurrentHook(plugin).getBalance(player);
    }

    /**
     * Checks if the player has a certain amount of balance.
     * @param player The player to check.
     * @param cost The amount to check.
     * @return True if the players balance is higher than or equal to the entered value.
     */
    public boolean hasBalance(OfflinePlayer player, double cost) {
        return manager.isEnabled() && manager.getCurrentHook(plugin).has(player, cost);
    }

    /**
     * Withdraws an entered amount of money from the player.
     * @param player The player to withdraw money from.
     * @param cost The amount to withdraw.
     * @return True if the transaction was successfull.
     */
    public boolean withdraw(OfflinePlayer player, double cost) {
        return manager.isEnabled() && manager.getCurrentHook(plugin).withdraw(player, cost);
    }

    /**
     * Deposits an entered amount of money to the players account.
     * @param player The player to deposit money to.
     * @param amount The amount to deposit.
     * @return True if the transaction was successfull.
     */
    public boolean deposit(OfflinePlayer player, double amount) {
        return manager.isEnabled() && manager.getCurrentHook(plugin).deposit(player, amount);
    }

}
