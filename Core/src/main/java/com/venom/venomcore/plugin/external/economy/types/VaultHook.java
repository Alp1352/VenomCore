package com.venom.venomcore.plugin.external.economy.types;

import com.venom.venomcore.plugin.external.economy.EconomyHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class VaultHook extends EconomyHook {
    private final Economy econ = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();

    @Override
    public boolean deposit(OfflinePlayer p, double amount) {
        return econ.depositPlayer(p, amount).transactionSuccess();
    }

    @Override
    public boolean withdraw(OfflinePlayer p, double amount) {
        return econ.withdrawPlayer(p, amount).transactionSuccess();
    }

    @Override
    public boolean has(OfflinePlayer p, double amount) {
        return econ.has(p, amount);
    }

    @Override
    public double getBalance(OfflinePlayer p) {
        return econ.getBalance(p);
    }

    @Override
    public boolean hasAccount(OfflinePlayer p) {
        return econ.hasAccount(p);
    }

    @Override
    public void createAccount(OfflinePlayer p) {
        econ.createPlayerAccount(p);
    }

    @Override
    public String getName() {
        return "Vault";
    }
}
