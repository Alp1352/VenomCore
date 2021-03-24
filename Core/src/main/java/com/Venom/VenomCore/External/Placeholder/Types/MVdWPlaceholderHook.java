package com.Venom.VenomCore.External.Placeholder.Types;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import com.Venom.VenomCore.External.Placeholder.PlaceholderHook;
import org.bukkit.entity.Player;

public class MVdWPlaceholderHook extends PlaceholderHook {
    @Override
    public String setPlaceholders(Player player, String text) {
        return PlaceholderAPI.replacePlaceholders(player, text);
    }

    @Override
    public String getName() {
        return "MVdWPlaceholderAPI";
    }
}
