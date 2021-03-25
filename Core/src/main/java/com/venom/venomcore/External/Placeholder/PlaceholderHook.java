package com.venom.venomcore.External.Placeholder;

import com.venom.venomcore.External.Hook;
import org.bukkit.entity.Player;

public abstract class PlaceholderHook implements Hook {
    public abstract String setPlaceholders(Player player, String text);
}
