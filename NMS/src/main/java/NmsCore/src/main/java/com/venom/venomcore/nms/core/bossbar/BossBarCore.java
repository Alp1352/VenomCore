package com.venom.venomcore.nms.core.bossbar;

import org.bukkit.plugin.java.JavaPlugin;

public interface BossBarCore {
    BossBar createBossBar(JavaPlugin plugin, String text, int percentage);

    BossBar createBossBar(JavaPlugin plugin, String text);
}
