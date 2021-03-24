package com.venom.nms.v1_8_R2.BossBar;

import org.bukkit.plugin.java.JavaPlugin;

public class BossBarCore implements com.venom.nms.core.BossBar.BossBarCore {
    @Override
    public BossBar createBossBar(JavaPlugin plugin, String text, int percentage) {
        return new BossBar(plugin, text, percentage);
    }

    @Override
    public BossBar createBossBar(JavaPlugin plugin, String text) {
        return createBossBar(plugin, text, 100);
    }
}
