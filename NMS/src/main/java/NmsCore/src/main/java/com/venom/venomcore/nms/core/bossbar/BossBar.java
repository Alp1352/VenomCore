package com.venom.venomcore.nms.core.bossbar;

import org.bukkit.entity.Player;

public interface BossBar {
    void show();

    void show(Player p);

    void update(String text, int percentage);

    void hide();

    void hide(Player p);
}
