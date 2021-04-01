package com.venom.venomcore.plugin.external.economy;

import com.venom.venomcore.plugin.external.Hook;
import org.bukkit.OfflinePlayer;

public abstract class EconomyHook implements Hook {
    public abstract boolean deposit(OfflinePlayer p, double amount);

    public abstract boolean withdraw(OfflinePlayer p, double amount);

    public abstract boolean has(OfflinePlayer p, double amount);

    public abstract double getBalance(OfflinePlayer p);

    public abstract boolean hasAccount(OfflinePlayer p);

    public abstract void createAccount(OfflinePlayer p);
}