package com.venom.venomcore.nms.common.bossbar;

import com.venom.venomcore.nms.core.bossbar.BarColor;
import com.venom.venomcore.nms.core.bossbar.BarStyle;
import com.venom.venomcore.nms.core.bossbar.BossBar;
import org.bukkit.plugin.Plugin;

public class BossBarCore implements com.venom.venomcore.nms.core.bossbar.BossBarCore {

    @Override
    public BossBar createBossBar(Plugin plugin, String text, double percentage, BarColor color, BarStyle style) {
        org.bukkit.boss.BarColor bukkitColor = org.bukkit.boss.BarColor.valueOf(color.name());
        org.bukkit.boss.BarStyle bukkitStyle = org.bukkit.boss.BarStyle.valueOf(style.name());
        return new com.venom.venomcore.nms.common.bossbar.BossBar(text, percentage, bukkitColor, bukkitStyle);
    }
}
