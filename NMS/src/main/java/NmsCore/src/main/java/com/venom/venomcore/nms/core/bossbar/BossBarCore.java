package com.venom.venomcore.nms.core.bossbar;

import org.bukkit.plugin.Plugin;

public interface BossBarCore {
    BossBar createBossBar(Plugin plugin, String text, double percentage, BarColor color, BarStyle style);

    default BossBar createBossBar(Plugin plugin, String text, double percentage, BarColor color) {
        return createBossBar(plugin, text, percentage, color, BarStyle.SOLID);
    }

    default BossBar createBossBar(Plugin plugin, String text, double percentage) {
        return createBossBar(plugin, text, percentage, BarColor.PINK);
    }

    default BossBar createBossBar(Plugin plugin, String text) {
        return createBossBar(plugin, text, 100);
    }
}
