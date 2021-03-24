package com.Venom.VenomCore.External.Placeholder;

import com.Venom.VenomCore.External.Hook;
import org.bukkit.entity.Player;

public abstract class PlaceholderHook implements Hook {
    public abstract String setPlaceholders(Player player, String text);
}
