package com.venom.venomcore.nms.core.bossbar;

import org.bukkit.entity.Player;

public interface BossBar {
    void show();

    void show(Player p);

    default void update(String text, double percentage) {
        update(text);
        update(percentage);
    }

    void hide();

    void hide(Player p);

    void update(String text);

    void update(double percentage);

    double getPercentage();

    String getText();
}
