package com.venom.venomcore.plugin.external.placeholder;

import com.venom.venomcore.plugin.external.Hook;
import org.bukkit.entity.Player;

public abstract class PlaceholderHook implements Hook {
    public abstract String setPlaceholders(Player player, String text);
}
