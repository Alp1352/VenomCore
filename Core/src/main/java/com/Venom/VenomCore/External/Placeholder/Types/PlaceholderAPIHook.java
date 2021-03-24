package com.Venom.VenomCore.External.Placeholder.Types;

import com.Venom.VenomCore.External.Placeholder.PlaceholderHook;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook extends PlaceholderHook {
    @Override
    public String setPlaceholders(Player player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    @Override
    public String getName() {
        return "PlaceholderAPI";
    }
}
