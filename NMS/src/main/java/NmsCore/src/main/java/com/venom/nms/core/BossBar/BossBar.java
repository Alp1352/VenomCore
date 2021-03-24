package com.venom.nms.core.BossBar;

import org.bukkit.entity.Player;

public interface BossBar {
    void show();

    void show(Player p);

    void update(String text, int percentage);

    void hide();

    void hide(Player p);
}
