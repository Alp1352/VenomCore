package com.venom.venomcore.plugin.external.economy.types;

import com.venom.venomcore.plugin.external.economy.EconomyHook;
import net.tnemc.core.Reserve;
import net.tnemc.core.economy.EconomyAPI;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;

public class ReserveHook extends EconomyHook {
    private final EconomyAPI api = Reserve.instance().economy();

    @Override
    public boolean deposit(OfflinePlayer p, double amount) {
        return api.addHoldings(p.getUniqueId(), BigDecimal.valueOf(amount));
    }

    @Override
    public boolean withdraw(OfflinePlayer p, double amount) {
        return api.removeHoldings(p.getUniqueId(), BigDecimal.valueOf(amount));
    }

    @Override
    public boolean has(OfflinePlayer p, double amount) {
        return api.hasHoldings(p.getUniqueId(), BigDecimal.valueOf(amount));
    }

    @Override
    public double getBalance(OfflinePlayer p) {
        return api.getHoldings(p.getUniqueId()).doubleValue();
    }

    @Override
    public boolean hasAccount(OfflinePlayer p) {
        return api.hasAccount(p.getUniqueId());
    }

    @Override
    public void createAccount(OfflinePlayer p) {
        api.createAccount(p.getUniqueId());
    }

    @Override
    public String getName() {
        return "Reserve";
    }
}
