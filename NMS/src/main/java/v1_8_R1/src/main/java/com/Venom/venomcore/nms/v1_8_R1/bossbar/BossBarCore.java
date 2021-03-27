package com.venom.venomcore.nms.v1_8_R1.bossbar;

import org.bukkit.plugin.java.JavaPlugin;

public class BossBarCore implements com.venom.venomcore.nms.core.bossbar.BossBarCore {
    @Override
    public BossBar createBossBar(JavaPlugin plugin, String text, int percentage) {
        return new BossBar(plugin, text, percentage);
    }

    @Override
    public BossBar createBossBar(JavaPlugin plugin, String text) {
        return createBossBar(plugin, text, 100);
    }
}
