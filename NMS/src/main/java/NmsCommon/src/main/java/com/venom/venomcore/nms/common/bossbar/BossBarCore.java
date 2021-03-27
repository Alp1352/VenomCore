package com.venom.venomcore.nms.common.bossbar;

import org.bukkit.plugin.java.JavaPlugin;

public class BossBarCore implements com.venom.venomcore.nms.core.bossbar.BossBarCore {
    @Override
    public BossBar createBossBar(JavaPlugin plugin, String text, int percentage) {
        return new BossBar(text, percentage);
    }

    @Override
    public BossBar createBossBar(JavaPlugin plugin, String text) {
        return createBossBar(plugin, text, 100);
    }
}
