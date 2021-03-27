package com.venom.venomcore.plugin.external.placeholder.types;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import com.venom.venomcore.plugin.external.placeholder.PlaceholderHook;
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
