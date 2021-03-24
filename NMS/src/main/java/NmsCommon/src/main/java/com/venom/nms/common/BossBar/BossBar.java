package com.venom.nms.common.BossBar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

/**
 * @author Alp Beji
 * @apiNote Because withers start showing particles below 50 percent health,
 * @apiNote percentage below 50 is not supported.
 * A class for sending BossBars in 1.8.8.
 */
public class BossBar implements com.venom.nms.core.BossBar.BossBar {
    private final org.bukkit.boss.BossBar bossBar;
    public BossBar(String text, int percentage) {
        bossBar = Bukkit.createBossBar(text, BarColor.PURPLE, BarStyle.SOLID);
        bossBar.setProgress(percentage);
        bossBar.setVisible(true);
    }

    public void show() {
        Bukkit.getOnlinePlayers().forEach(this::show);
    }

    public void show(Player p) {
        bossBar.addPlayer(p);
    }

    public void update(String text, int percentage) {
        bossBar.setTitle(text);
        bossBar.setProgress(percentage);
    }

    public void hide() {
        bossBar.removeAll();
    }

    public void hide(Player p) {
        bossBar.removePlayer(p);
    }
}
