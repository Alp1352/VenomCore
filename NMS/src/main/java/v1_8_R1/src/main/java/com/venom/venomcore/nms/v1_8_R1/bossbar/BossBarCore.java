package com.venom.venomcore.nms.v1_8_R1.bossbar;

import com.venom.venomcore.nms.core.bossbar.BarColor;
import com.venom.venomcore.nms.core.bossbar.BarStyle;
import com.venom.venomcore.nms.core.bossbar.BossBar;
import org.bukkit.plugin.Plugin;

public class BossBarCore implements com.venom.venomcore.nms.core.bossbar.BossBarCore {

    @Override
    public BossBar createBossBar(Plugin plugin, String text, double percentage, BarColor color, BarStyle style) {
        return new com.venom.venomcore.nms.v1_8_R1.bossbar.BossBar(plugin, text, percentage);
    }
}
