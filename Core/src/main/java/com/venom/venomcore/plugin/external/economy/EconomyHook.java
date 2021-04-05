package com.venom.venomcore.plugin.external.economy;

import com.venom.venomcore.plugin.external.Hook;
import org.bukkit.OfflinePlayer;

public abstract class EconomyHook implements Hook {
    /**
     * Deposits an entered amount of money to the players account.
     * @param player The player to deposit money to.
     * @param cost The amount to deposit.
     * @return True if the transaction was successfull.
     */
    public abstract boolean deposit(OfflinePlayer player, double cost);

    /**
     * Withdraws an entered amount of money from the player.
     * @param player The player to withdraw money from.
     * @param cost The amount to withdraw.
     * @return True if the transaction was successfull.
     */
    public abstract boolean withdraw(OfflinePlayer player, double cost);

    /**
     * Checks if the player has a certain amount of balance.
     * @param player The player to check.
     * @param cost The amount to check.
     * @return True if the players balance is higher than or equal to the entered value.
     */
    public abstract boolean has(OfflinePlayer player, double cost);

    /**
     * Gets the balance of a player.
     * @param player The player.
     * @return The balance.
     */
    public abstract double getBalance(OfflinePlayer player);

    /**
     * Check whether the player has an account or not.
     * @param player The player to check.
     * @return True if the player has played before and has an account.
     */
    public abstract boolean hasAccount(OfflinePlayer player);

    /**
     * Create an account for an offline player.
     * @param player The player to create the account for.
     */
    public abstract void createAccount(OfflinePlayer player);
}
